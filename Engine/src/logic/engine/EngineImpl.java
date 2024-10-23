package logic.engine;

import component.archive.api.Archive;
import component.archive.impl.ArchiveImpl;
import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.range.api.Range;
import component.range.impl.RangeImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import dto.cell.CellDTO;
import dto.cell.CellStyleDTO;
import dto.permission.RequestedRequestForTableDTO;
import dto.range.RangeDTO;
import dto.range.RangesDTO;
import dto.returnable.EffectiveValueDTO;
import dto.sheet.ColoredSheetDTO;
import dto.sheet.SheetAndRangesDTO;
import dto.sheet.SheetMetaDataDTO;
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
import user.permission.request.PermissionRequest;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EngineImpl implements Engine {
    private User owner;
    private String sheetName;
    private Sheet sheet;
    private Archive archive;
    private Map<String, PermissionType> usersCurrentPermission;
    private List<PermissionRequest> allPermissionRequests;
    private Map<String, Integer> usersActiveSheetVersion;

    private final Object sheetEditLock;
    private final ReadWriteLock usersCurrentPermissionReadWriteLock;
    private final ReadWriteLock allPermissionRequestsReadWriteLock;
    private final ReadWriteLock usersActiveSheetVersionReadWriteLock;

    public EngineImpl(User owner) {
        this.usersCurrentPermission = new HashMap<>();
        this.allPermissionRequests = new LinkedList<>();
        this.usersActiveSheetVersion = new HashMap<>();
        this.owner = owner;
        usersCurrentPermission.put(owner.getUserName(), PermissionType.OWNER);
        this.archive = null;
        this.sheet = null;
        this.sheetName = null;
        this.allPermissionRequests.add(new PermissionRequest(this.owner.getUserName(), PermissionType.OWNER, PermissionStatus.OWNER, 0));
        this.usersCurrentPermissionReadWriteLock = new ReentrantReadWriteLock();
        this.allPermissionRequestsReadWriteLock = new ReentrantReadWriteLock();
        this.usersActiveSheetVersionReadWriteLock = new ReentrantReadWriteLock();
        this.sheetEditLock = new Object();
    }

    @Override
    public void loadData(String path) {
        try {
            XMLToSheetConverter converter = new XMLToSheetConverterImpl();
            this.sheet = converter.convert(path);
            this.archive = new ArchiveImpl();
            this.archive.storeInArchive(this.sheet.copySheet());
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException("Error loading data from file", e);
        }
    }

    @Override
    public void loadDataFromInputStream(InputStream inputStream) {
        try {
            XMLToSheetConverter converter = new XMLToSheetConverterImpl();
            this.sheet = converter.convertFromStream(inputStream);
            this.sheetName = this.sheet.getSheetName();
            this.archive = new ArchiveImpl();
            this.archive.storeInArchive(this.sheet.copySheet());
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException("Error loading data from file", e);
        }
    }

    @Override
    public ColoredSheetDTO getColoredSheetDTO(String userName) {
        this.usersActiveSheetVersionReadWriteLock.readLock().lock();
        try {
            return new ColoredSheetDTO(this.archive.retrieveVersion(this.usersActiveSheetVersion.get(userName)));
        } finally {
            this.usersActiveSheetVersionReadWriteLock.readLock().unlock();
        }
    }

    @Override
    public CellDTO getSingleCellData(String cellID, String userName) {
        this.usersActiveSheetVersionReadWriteLock.readLock().lock();
        try {
            return new CellDTO(this.archive.retrieveVersion(this.usersActiveSheetVersion.get(userName)).getCell(cellID), cellID);
        } finally {
            this.usersActiveSheetVersionReadWriteLock.readLock().unlock();
        }
    }

    @Override
    public void updateSingleCellData(String cellID, String value, String userName) {
        Cell cellToUpdate = this.sheet.getCell(cellID);
        boolean isUpdatingEmptyToEmpty = cellToUpdate == null && value.isEmpty();
        boolean isOriginalValueChanged = cellToUpdate != null && !cellToUpdate.getOriginalValue().equals(value);

        if (isUpdatingEmptyToEmpty) {
            return;
        }

        SheetImpl newSheetVersion = this.sheet.copySheet();
        updateCell(cellID, value, newSheetVersion);
        Sheet tempSheet = this.sheet.updateSheet(newSheetVersion, isOriginalValueChanged);
        if (!tempSheet.equals(this.sheet)) {
            this.sheet = tempSheet;
            this.updateUserActiveSheetVersion(userName);
            this.archive.storeInArchive(this.sheet.copySheet());
        }
    }

    private void updateCell(String cellID, String value, Sheet newSheetVersion) {
        Cell cellToUpdate = newSheetVersion.getCell(cellID);

        if (cellToUpdate != null) {
            cellToUpdate.getUsedRanges().forEach(range -> {
                Range currentRange = newSheetVersion.getRanges().get(range);
                if (currentRange != null) {
                    currentRange.reduceUsage();
                }
            });
            cellToUpdate.setOriginalValue(value, newSheetVersion.getVersion() + 1);
        } else {
            cellToUpdate = new CellImpl(cellID, value, newSheetVersion.getVersion(), newSheetVersion);
            newSheetVersion.getCells().put(cellToUpdate.getCellId(), cellToUpdate);
        }
    }

    @Override
    public VersionChangesDTO showVersions() {
        return new VersionChangesDTO(this.archive.getAllVersionsChangesList());
    }

    @Override
    public SheetAndRangesDTO getSheetVersionAndRangesAsDTO(int version, String userName) {
        usersActiveSheetVersionReadWriteLock.writeLock().lock();

        try {
                this.usersActiveSheetVersion.put(userName, version);
        } finally {
            usersActiveSheetVersionReadWriteLock.writeLock().unlock();
        }

        Sheet lastVersionSheet = this.archive.retrieveLatestVersion();
        boolean userInReaderMode = !this.isPermittedToWrite(userName);
        boolean lastVersionRequested = version == lastVersionSheet.getVersion();
        boolean userCantEditTheSheet = userInReaderMode ? true : !lastVersionRequested;

        Sheet sheet = this.archive.retrieveVersion(version);
        ColoredSheetDTO coloredSheetDTO = new ColoredSheetDTO(sheet);
        RangesDTO rangesDTO = new RangesDTO(sheet.getRanges());

        return new SheetAndRangesDTO(coloredSheetDTO, rangesDTO, userCantEditTheSheet);
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
        this.allPermissionRequestsReadWriteLock.writeLock().lock();

        try {
            this.sheet.createRange(rangeName, range);
            return new RangeDTO(this.sheet.getRanges().get(rangeName));
        } finally {
            this.allPermissionRequestsReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void removeRange(String rangeName) {
        this.sheet.deleteRange(rangeName);
    }

    @Override
    public RangesDTO getAllRanges(String userName) {
        this.allPermissionRequestsReadWriteLock.readLock().lock();
        try{
            return new RangesDTO(this.archive.retrieveVersion(this.usersActiveSheetVersion.get(userName)).getRanges());
        } finally {
            this.allPermissionRequestsReadWriteLock.readLock().unlock();
        }

    }

    @Override
    public void updateCellStyle(CellStyleDTO cellStyleDTO) {
        Cell cellToUpdate = this.sheet.getCell(cellStyleDTO.getCellID());
        if (cellToUpdate == null) {
            cellToUpdate = new CellImpl(cellStyleDTO.getCellID(), "", this.sheet.getVersion(), this.sheet);
            this.sheet.getCells().put(cellToUpdate.getCellId(), cellToUpdate);
        }

        cellToUpdate.setBackgroundColor(cellStyleDTO.getBackgroundColor());
        cellToUpdate.setTextColor(cellStyleDTO.getTextColor());
    }

    @Override
    public ColoredSheetDTO sortRangeOfCells(String range, List<String> columnsToSortBy) {
        Sheet sortedSheet = this.sheet.copySheet();
        Sorter sorter = new Sorter(new RangeImpl("sort", range, sortedSheet), columnsToSortBy);

        sorter.sort().getRangeCells().forEach(cell -> sortedSheet.getCells().put(cell.getCellId(), cell));

        return new ColoredSheetDTO(sortedSheet);
    }

    @Override
    public List<String> getColumnsListOfRange(String range) {
        Range rangeToFilter = new RangeImpl("range of columns", range, this.sheet.copySheet());

        return rangeToFilter.getColumnsListOfRange();
    }

    @Override
    public List<EffectiveValueDTO> getUniqueItemsToFilterBy(String column, String rangeName) {
        Range range = new RangeImpl("range of unique items", rangeName, this.sheet.copySheet());

        return this.getUniqueItemsInColumn(column, range);
    }

    @Override
    public LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> getGraphFromRange(String rangeToBuildGraphFrom) {
        GraphSeriesBuilder graphSeries = new GraphSeriesBuilder(new RangeImpl("range of graph", rangeToBuildGraphFrom, this.sheet.copySheet()));

        return graphSeries.build();
    }

    @Override
    public String getSheetName() {
        return this.sheetName;
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
    public ColoredSheetDTO filterRangeOfCells(String rangeToFilterBy, String columnToFilterBy, List<Integer> itemsToFilterBy) {
        Sheet filteredSheet = this.sheet.copySheet();
        Range rangeToFilter = new RangeImpl("range to filter", rangeToFilterBy, filteredSheet);
        rangeToFilter.getRangeCells().forEach(cell -> filteredSheet.getCells().remove(cell.getCellId()));
        Filter filter = new Filter(rangeToFilter);
        List<EffectiveValueDTO> uniqueItemsList = this.getUniqueItemsInColumn(columnToFilterBy, rangeToFilter);
        List<EffectiveValueDTO> filteredItemsList = new ArrayList<>();
        for (int itemToFilterIndex : itemsToFilterBy) {
            filteredItemsList.add(uniqueItemsList.get(itemToFilterIndex));
        }
        filter.filter(columnToFilterBy, filteredItemsList)
                .getRangeCells().forEach(cell -> filteredSheet.getCells().put(cell.getCellId(), cell));
        return new ColoredSheetDTO(filteredSheet);
    }

    @Override
    public SheetMetaDataDTO getSheetMetaDataDTO(String userName) {
        PermissionType userPermissionType = this.getUserPermission(userName);

        return new SheetMetaDataDTO(sheet.getSheetName(), sheet.getLayout().getColumn(), sheet.getLayout().getRow(), this.owner.getUserName(), userPermissionType);
    }

    @Override
    public List<RequestedRequestForTableDTO> getAllRequestsAsRequestedRequestForTableDTO() {
        List<RequestedRequestForTableDTO> requestedRequestForTableDTOs;

        this.usersCurrentPermissionReadWriteLock.readLock().lock();
        try {
            requestedRequestForTableDTOs = new ArrayList<>(this.allPermissionRequests.size());

            this.allPermissionRequests.forEach((permission) -> {
                requestedRequestForTableDTOs.add(
                        new RequestedRequestForTableDTO(
                                permission.getSenderName(),
                                permission.getRequestedPermissionType(),
                                permission.getRequestedPermissionStatus()
                        )
                );
            });
        } finally {
            this.usersCurrentPermissionReadWriteLock.readLock().unlock();
        }

        return requestedRequestForTableDTOs;
    }

    @Override
    public void updatePermissionStatus(String requesterUserName, PermissionType requestedPermission, boolean requestApproved, int requestNumber) {
        PermissionStatus permissionStatus;

        if (requestApproved) {
            permissionStatus = PermissionStatus.ACCEPTED;
            this.usersCurrentPermissionReadWriteLock.writeLock().lock();
            try {
                this.usersCurrentPermission.put(requesterUserName, requestedPermission);
            } finally {
                this.usersCurrentPermissionReadWriteLock.writeLock().unlock();
            }
        } else {
            permissionStatus = PermissionStatus.DENIED;
        }

        this.usersCurrentPermissionReadWriteLock.writeLock().lock();
        try {
            this.allPermissionRequests.get(requestNumber).setRequestedPermissionStatus(permissionStatus);
        } finally {
            this.usersCurrentPermissionReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean isPermittedToWrite(String userName) {
        this.usersCurrentPermissionReadWriteLock.readLock().lock();

        try{
            if(this.usersCurrentPermission.containsKey(userName)){
                return !this.usersCurrentPermission.get(userName).equals(PermissionType.READER);
                } else {
                throw new RuntimeException("user name: " + userName + " dosnt exists.");
            }
            } finally {
            this.usersCurrentPermissionReadWriteLock.readLock().unlock();
        }
    }

    public boolean isInLastVersion(String userName){
        this.usersActiveSheetVersionReadWriteLock.readLock().lock();

        try{
            if(this.usersActiveSheetVersion.containsKey(userName)){
                return this.usersActiveSheetVersion.get(userName).equals(this.sheet.getVersion());
            } else {
                throw new RuntimeException("user name: " + userName + " dosnt exists.");
            }
        } finally {
            this.usersActiveSheetVersionReadWriteLock.readLock().unlock();
        }
    }

    @Override
    public void updateUserActiveSheetVersion(String userName) {
        this.usersActiveSheetVersionReadWriteLock.writeLock().lock();

        try{
            this.usersActiveSheetVersion.put(userName, this.sheet.getVersion());
        } finally {
            this.usersActiveSheetVersionReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Object getSheetEditLock() {
        return this.sheetEditLock;
    }

    @Override
    public void createPermissionRequest(PermissionType requestedPermission, String username) {
        if (this.owner.getUserName().equals(username)) {
            throw (new IllegalArgumentException("Cannot crate permission request for your own sheet"));
        }

        this.allPermissionRequestsReadWriteLock.writeLock().lock();
        PermissionRequest permissionRequestForOwner;

        try {
            PermissionRequest permissionRequest = new PermissionRequest(username,
                    requestedPermission, PermissionStatus.PENDING, this.allPermissionRequests.size());
            this.allPermissionRequests.add(permissionRequest);
            permissionRequestForOwner = permissionRequest.deepCopy();
        } finally {
            this.allPermissionRequestsReadWriteLock.writeLock().unlock();
        }
        this.owner.createPermissionRequest(permissionRequestForOwner, this.sheetName);
    }

    private PermissionType getUserPermission(String userName) {
        this.usersCurrentPermissionReadWriteLock.readLock().lock();
        try{
        PermissionType userCurrentPermission = this.usersCurrentPermission.get(userName);

        if (userCurrentPermission == null) {
            userCurrentPermission = PermissionType.NONE;
        }

        return userCurrentPermission;
    } finally {
            this.usersCurrentPermissionReadWriteLock.readLock().unlock();
        }
    }

}
