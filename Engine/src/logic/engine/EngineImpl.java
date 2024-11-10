package logic.engine;

import component.archive.api.Archive;
import component.archive.impl.ArchiveImpl;
import component.cell.api.Cell;
import component.cell.api.CellType;
import component.cell.impl.CellImpl;
import component.range.api.Range;
import component.range.impl.RangeImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import dto.cell.CellDTO;
import dto.cell.CellStyleDTO;
import dto.filter.FilterParametersDTO;
import dto.permission.PermissionDTO;
import dto.permission.SentPermissionRequestDTO;
import dto.range.RangeDTO;
import dto.range.RangesDTO;
import dto.returnable.EffectiveValueDTO;
import dto.sheet.*;
import dto.version.VersionChangesDTO;
import jakarta.xml.bind.JAXBException;
import jaxb.converter.api.XMLToSheetConverter;
import jaxb.converter.impl.XMLToSheetConverterImpl;
import logic.filter.Filter;
import logic.graph.GraphSeriesBuilder;
import user.User;
import user.permission.PermissionStatus;
import user.permission.PermissionType;
import logic.sort.Sorter;
import user.request.PermissionRequest;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EngineImpl implements Engine{
    private final User owner;
    private String name;
    private Sheet sheet;
    private Archive archive;
    private final Map<String, PermissionType> usersPermissions;
    private final List<PermissionRequest> allPermissionRequests;
    private final Map<String, Integer> usersActiveVersion;
    private final Map<String, Boolean> isUserUpToDate;
    private final Map<String, Sheet> dynamicAnalysisMap;
    
    private final Object sheetEditLock;
    private final ReadWriteLock usersPermissionLock;
    private final ReadWriteLock allPermissionLock;
    private final ReadWriteLock usersActiveVersionLock;
    private final ReadWriteLock isUserUpToDateLock;
    private final Object dynamicAnalysisLock;
    
    public EngineImpl(User owner) {
        this.owner = owner;
        this.name = null;
        this.sheet = null;
        this.archive = null;
        
        this.usersPermissions = new HashMap<>();
        this.allPermissionRequests = new LinkedList<>();
        this.usersActiveVersion = new HashMap<>();
        this.isUserUpToDate = new HashMap<>();
        this.dynamicAnalysisMap = new HashMap<>();
        
        this.usersPermissions.put(this.owner.getUserName(),PermissionType.OWNER);
        this.allPermissionRequests.add(new PermissionRequest(
                this.owner.getUserName(),
                PermissionType.OWNER,
                PermissionStatus.APPROVED,
                this.allPermissionRequests.size())
        );
        
        this.sheetEditLock = new Object();
        this.usersPermissionLock = new ReentrantReadWriteLock();
        this.allPermissionLock = new ReentrantReadWriteLock();
        this.usersActiveVersionLock = new ReentrantReadWriteLock();
        this.isUserUpToDateLock = new ReentrantReadWriteLock();
        this.dynamicAnalysisLock = new Object();
    }
    
    @Override
    public void loadData(String path) {
        try {
            XMLToSheetConverter converter = new XMLToSheetConverterImpl();
            this.sheet = converter.convert(path);
            this.archive = new ArchiveImpl();
            this.archive.storeInArchive(this.sheet.copySheet());
        } catch (JAXBException | FileNotFoundException e ) {
            throw new RuntimeException("Error loading data from file", e);
        }
    }
    
    @Override
    public void loadDataFromStream(InputStream stream) {
        try {
            XMLToSheetConverter converter = new XMLToSheetConverterImpl();
            this.sheet = converter.convertFromStream(stream);
            this.archive = new ArchiveImpl();
            this.archive.storeInArchive(this.sheet.copySheet());
            this.name = this.sheet.getSheetName();
        } catch (JAXBException | FileNotFoundException e ) {
            throw new RuntimeException("Error loading data from file", e);
        }
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public ColoredSheetDTO getColoredSheetAsDTO(String username) {
        return new ColoredSheetDTO(this.archive.retrieveVersion(this.getUsersActiveVersion(username)));
    }
    
    @Override
    public SheetDTO getSheetAsDTO(String username) {
        return new SheetDTO(this.archive.retrieveVersion(this.getUsersActiveVersion(username)));
    }

    @Override
    public CellDTO getSingleCellData(String cellID, String username) {
        return new CellDTO(
                this.archive.retrieveVersion(this.getUsersActiveVersion(username)).getCell(cellID), cellID);
    }

    @Override
    public void updateSingleCellData(String cellID, String value, String username) {
        Cell cellToUpdate = this.sheet.getCell(cellID);
        boolean isUpdatingEmptyToEmpty = cellToUpdate == null && value.isEmpty();
        boolean isOriginalValueChanged = cellToUpdate != null && !cellToUpdate.getOriginalValue().equals(value);
        
        if (isUpdatingEmptyToEmpty) {
            return;
        }
        
        SheetImpl newSheetVersion = this.sheet.copySheet();
        updateCell(cellID, value, newSheetVersion, username);
        Sheet tempSheet = this.sheet.updateSheet(newSheetVersion, isOriginalValueChanged, username);
        if (!tempSheet.equals(this.sheet)) {
            this.sheet = tempSheet;
            this.updateUserActiveVersion(username);
            this.unUpdateEveryoneElse(username);
            this.archive.storeInArchive(this.sheet.copySheet());
        }
    }
    
    private void unUpdateEveryoneElse(String username) {
        this.usersPermissionLock.writeLock().lock();
        this.usersActiveVersionLock.readLock().lock();
        try {
            this.isUserUpToDate.forEach((currentUser, isUpToDate) -> {
                if (!username.equals(currentUser)
                        || this.usersActiveVersion.get(currentUser) != this.sheet.getVersion()) {
                    this.isUserUpToDate.put(currentUser, false);
                }
            });
        } finally {
            this.usersActiveVersionLock.readLock().unlock();
            this.usersPermissionLock.writeLock().unlock();
        }
    }
    
    private void updateCell(String cellID, String value, Sheet newSheetVersion, String username) {
        Cell cellToUpdate = newSheetVersion.getCell(cellID);
        if (cellToUpdate != null) {
            cellToUpdate.getUsedRanges().forEach(range -> {
                Range currentRange = newSheetVersion.getRanges().get(range);
                if (currentRange != null) {
                    currentRange.reduceUsage();
                }
            });
            cellToUpdate.setOriginalValue(value, newSheetVersion.getVersion() + 1, username);
        } else {
            cellToUpdate = new CellImpl(cellID, value, newSheetVersion.getVersion(), newSheetVersion);
            cellToUpdate.setUpdatedBy(username);
            newSheetVersion.getCells().put(cellToUpdate.getCellId(), cellToUpdate);
        }
    }

    @Override
    public VersionChangesDTO showVersions() {
        return new VersionChangesDTO(this.archive.getAllVersionsChangesList());
    }

    @Override
    public SheetAndRangesDTO getSheetVersionAsDTO(int version, String username) {
        this.usersActiveVersionLock.writeLock().lock();
        this.isUserUpToDateLock.writeLock().lock();
        try {
            this.usersActiveVersion.put(username, version);
            if (this.sheet.getVersion() == version) {
                this.isUserUpToDate.put(username, true);
            }
        } finally {
            this.usersActiveVersionLock.writeLock().unlock();
            this.isUserUpToDateLock.writeLock().unlock();
        }
        
        boolean userCannotEdit = isUserCannotEdit(version, username);
        
        Sheet sheetToRetrieve = this.archive.retrieveVersion(version);
        ColoredSheetDTO coloredSheet = new ColoredSheetDTO(sheetToRetrieve);
        RangesDTO ranges = new RangesDTO(sheetToRetrieve.getRanges());
        return new SheetAndRangesDTO(coloredSheet, ranges, userCannotEdit);
    }
    
    @Override
    public boolean isUserCannotEdit(int version, String username) {
        boolean isGettingLatestVersion = this.archive.retrieveLatestVersion().getVersion() == version;
        boolean userInReaderMode = !this.isPermitted(username);
        
        return userInReaderMode || !isGettingLatestVersion;
    }
    
    @Override
    public boolean isSheetLoaded() {
        return this.sheet != null;
    }

    @Override
    public void loadFromFile(String path) {
        this.archive = Archive.loadFromFile(path);
        this.sheet = this.archive.retrieveLatestVersion();
    }

    @Override
    public void saveToFile(String path) {
        this.archive.saveToFile(path);
    }
    
    @Override
    public RangeDTO addRange(String rangeName, String range) {
        this.sheet.createRange(rangeName, range);
        return new RangeDTO(this.sheet.getRanges().get(rangeName));
    }
    
    @Override
    public void removeRange(String rangeName) {
        this.sheet.deleteRange(rangeName);
    }
    
    @Override
    public RangesDTO getAllRanges(String username) {
        return new RangesDTO(this.archive.retrieveVersion(this.getUsersActiveVersion(username)).getRanges());
    }
    
    @Override
    public void updateCellStyle(CellStyleDTO newCellStyle) {
        String cellID = newCellStyle.getCellID();
        Cell cellToStyle = this.sheet.getCell(cellID);
        if(cellToStyle == null) {
            cellToStyle = new CellImpl(cellID, "", this.sheet.getVersion(), this.sheet);
            this.sheet.getCells().put(cellToStyle.getCellId(), cellToStyle);
        }
        
        cellToStyle.setBackgroundColor(newCellStyle.getBackgroundColor());
        cellToStyle.setTextColor(newCellStyle.getTextColor());
    }
    
    @Override
    public ColoredSheetDTO sortRangeOfCells(String range, List<String> columnsToSortBy, String username) {
        Sheet sortedSheet = this.archive.retrieveVersion(this.getUsersActiveVersion(username)).copySheet();
        Sorter sorter = new Sorter(new RangeImpl("sort", range, sortedSheet), columnsToSortBy);
        
        sorter.sort().getRangeCells().forEach(cell -> sortedSheet.getCells().put(cell.getCellId(), cell));
        
        return new ColoredSheetDTO(sortedSheet);
    }
    
    @Override
    public List<String> getColumnsListOfRange(String range, String username) {
        Sheet copy = this.archive.retrieveVersion(this.getUsersActiveVersion(username)).copySheet();
        Range rangeToFilter = new RangeImpl("range of columns", range, copy);
        
        return rangeToFilter.getColumnsListOfRange();
    }
    
    @Override
    public List<EffectiveValueDTO> getUniqueItemsToFilterBy(String column, String rangeName, String username) {
        Sheet copy = this.archive.retrieveVersion(this.getUsersActiveVersion(username)).copySheet();
        Range range = new RangeImpl("range of unique items", rangeName, copy);
        
        return this.getUniqueItemsInColumn(column, range);
    }
    
    @Override
    public SheetMetaDataDTO getSheetMetaData(String currentUserName) {
        PermissionType permission;
        
        this.usersPermissionLock.readLock().lock();
        try {
            permission = this.usersPermissions.get(currentUserName);
        } finally {
            usersPermissionLock.readLock().unlock();
        }
        
        if (permission == null) {
            permission = PermissionType.NONE;
        }
        
        return new SheetMetaDataDTO(
                this.owner.getUserName(),
                this.sheet.getSheetName(),
                this.sheet.getLayout().getRow(),
                this.sheet.getLayout().getColumn(),
                permission.getPermission()
        );
    }
    
    @Override
    public LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> getGraphFromRange(
            String rangeToBuildGraphFrom, String username) {
        
        Sheet graphSheet = this.archive.retrieveVersion(this.getUsersActiveVersion(username)).copySheet();
        
        GraphSeriesBuilder graphSeries =
                new GraphSeriesBuilder(
                        new RangeImpl("range of graph", rangeToBuildGraphFrom, graphSheet));

        return graphSeries.build();
    }
    
    @Override
    public void updatePermissionForUser(String sender, boolean answer, int requestID) {
        this.allPermissionLock.writeLock().lock();
        try {
            PermissionRequest requestToAnswerTo = this.allPermissionRequests.get(requestID);
            
            if (requestToAnswerTo == null) {
                throw new IllegalArgumentException("No Permission Request found with ID " + requestID);
            }
            
            if (answer) {
                this.usersPermissionLock.writeLock().lock();
                try {
                    this.usersPermissions.put(sender, requestToAnswerTo.getRequestedPermission());
                } finally {
                    this.usersPermissionLock.writeLock().unlock();
                }
            }
            
            requestToAnswerTo.updateRequestStatus(answer);
        } finally {
            this.allPermissionLock.writeLock().unlock();
        }
    }
    
    @Override
    public void updateUserActiveVersion(String username) {
        this.usersActiveVersionLock.writeLock().lock();
        this.isUserUpToDateLock.writeLock().lock();
        try {
            this.usersActiveVersion.put(username, this.sheet.getVersion());
            this.isUserUpToDate.put(username, true);
        } finally {
            this.usersActiveVersionLock.writeLock().unlock();
            this.isUserUpToDateLock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean isPermitted(String username) {
        this.usersPermissionLock.readLock().lock();
        try {
            if (this.usersPermissions.containsKey(username)) {
                return !this.usersPermissions.get(username).equals(PermissionType.READER);
            }
            
            throw new IllegalArgumentException("You don't have Permissions for this sheet");
        } finally {
            this.usersPermissionLock.readLock().unlock();
        }
    }
    
    @Override
    public boolean isInLatestVersion(String username) {
        return this.getUsersActiveVersion(username) == this.sheet.getVersion();
    }
    
    @Override
    public int getUsersActiveVersion(String username) {
        this.usersActiveVersionLock.readLock().lock();
        try {
            if (this.usersActiveVersion.containsKey(username)) {
                return this.usersActiveVersion.get(username);
            }
            
            throw new IllegalArgumentException("No active version was found");
        } finally {
            this.usersActiveVersionLock.readLock().unlock();
        }
    }
    
    @Override
    public Object getSheetEditLock() {
        return this.sheetEditLock;
    }
    
    @Override
    public boolean shouldNotifyUser(String username) {
        this.isUserUpToDateLock.readLock().lock();
        try {
            return !this.isUserUpToDate.get(username);
        } finally {
            this.isUserUpToDateLock.readLock().unlock();
        }
    }
    
    @Override
    public SheetAndCellDTO dynamicCellUpdate(String cellID, String newOriginalValue, String username) {
        Sheet dynamicAnalysisSheet = this.getDynamicAnalysisSheet(username);
        Cell dynamicCell = dynamicAnalysisSheet.getCell(cellID);
        
        if (dynamicCell == null || !dynamicCell.getEffectiveValue().getCellType().equals(CellType.NUMERIC)) {
            throw new IllegalArgumentException("Dynamic Analysis can only be done on a numeric cell");
        }
        
        try {
            Double.parseDouble(dynamicCell.getOriginalValue());
            Double.parseDouble(newOriginalValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Original Value Must be a numeric value");
        }
        
        dynamicCell.setDynamicValue(newOriginalValue);
        dynamicAnalysisSheet.updateDynamicSheet();
        this.addDynamicAnalysisSheet(dynamicAnalysisSheet, username);
        
        return new SheetAndCellDTO(new ColoredSheetDTO(dynamicAnalysisSheet), new CellDTO(dynamicCell, cellID));
    }
    
    private Sheet getDynamicAnalysisSheet(String username) {
        synchronized (this.dynamicAnalysisLock) {
            Sheet dynamicAnalysisSheet = this.dynamicAnalysisMap.get(username);
            if (dynamicAnalysisSheet == null) {
                dynamicAnalysisSheet = this.archive.retrieveVersion(this.getUsersActiveVersion(username)).copySheet();
            }
            
            return dynamicAnalysisSheet;
        }
    }
    
    private void addDynamicAnalysisSheet(Sheet dynamicAnalysisSheet, String username) {
        synchronized (this.dynamicAnalysisLock) {
            this.dynamicAnalysisMap.put(username, dynamicAnalysisSheet);
        }
    }
    
    @Override
    public void finishDynamicAnalysis(String username) {
        synchronized (this.dynamicAnalysisLock) {
            this.dynamicAnalysisMap.remove(username);
        }
    }
    
    @Override
    public List<PermissionDTO> getAllPermissions() {
        List<PermissionDTO> permissions;
        
        this.allPermissionLock.readLock().lock();
        try {
            permissions = new ArrayList<>(this.allPermissionRequests.size());
            
            this.allPermissionRequests.forEach((permission) ->
                    permissions.add(new PermissionDTO(
                            permission.getSenderName(),
                            permission.getRequestedPermission().getPermission(),
                            permission.getRequestStatus().getPermissionStatus()))
            );
        } finally {
            this.allPermissionLock.readLock().unlock();
        }
        
        return permissions;
    }
    
    @Override
    public void createNewPermissionRequest(SentPermissionRequestDTO requestToSend, String sender)  throws IllegalArgumentException {
        if (this.owner.getUserName().equals(sender)) {
            throw new IllegalArgumentException("Cannot create a new permission request for your own Sheets");
        }
        
        PermissionRequest permissionRequest, copyRequest;
        
        this.allPermissionLock.writeLock().lock();
        try {
            permissionRequest = new PermissionRequest(
                    sender,
                    PermissionType.valueOf(requestToSend.getRequestedPermission().toUpperCase()),
                    PermissionStatus.PENDING,
                    this.allPermissionRequests.size());
            
            this.allPermissionRequests.add(permissionRequest);
            copyRequest = permissionRequest.deepCopy();
        } finally {
            this.allPermissionLock.writeLock().unlock();
        }
        
        this.owner.createPermissionRequest(copyRequest, this.name, sender);
    }
    
    private List<EffectiveValueDTO> getUniqueItemsInColumn(String column, Range range) {
        List<Cell> itemsList = range.getRangeCells()
                .stream()
                .filter(cell -> cell.getCellId().contains(column))
                .toList();
        
        Set<EffectiveValueDTO> itemsSet = new LinkedHashSet<>();
        itemsList.forEach(cell -> itemsSet.add(new EffectiveValueDTO(cell.getEffectiveValue())));
        
        return new ArrayList<>(itemsSet);
    }
    
    @Override
    public ColoredSheetDTO filterRangeOfCells(FilterParametersDTO filterParameters, String username) {
        Sheet filteredSheet = this.archive.retrieveVersion(this.getUsersActiveVersion(username)).copySheet();
        Range rangeToFilter =
                new RangeImpl("range to filter", filterParameters.getRangeToFilterBy(), filteredSheet);
        
        rangeToFilter.getRangeCells().forEach(cell -> filteredSheet.getCells().remove(cell.getCellId()));
        Filter filter = new Filter(rangeToFilter);
        
        List<EffectiveValueDTO> uniqueItemsList =
                this.getUniqueItemsInColumn(filterParameters.getColumnToFilterBy(), rangeToFilter);
        
        List<EffectiveValueDTO> filteredItemsList = new ArrayList<>();
        for (int itemToFilterIndex : filterParameters.getItemsToFilterBy()) {
            filteredItemsList.add(uniqueItemsList.get(itemToFilterIndex));
        }
        filter.filter(filterParameters.getColumnToFilterBy(), filteredItemsList)
                .getRangeCells().forEach(cell -> filteredSheet.getCells().put(cell.getCellId(), cell));
        
        return new ColoredSheetDTO(filteredSheet);
    }
}
