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
import dto.permission.RequestedRequestForTableDTO;
import dto.range.RangeDTO;
import dto.range.RangesDTO;
import dto.returnable.EffectiveValueDTO;
import dto.sheet.ColoredSheetDTO;
import dto.sheet.SheetDTO;
import dto.sheet.SheetMetaDataDTO;
import dto.version.VersionChangesDTO;
import jakarta.xml.bind.JAXBException;
import javafx.scene.paint.Color;
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
    private final ReadWriteLock usersCurrentPermissionReadWriteLock;
    private final ReadWriteLock allPermissionRequestsReadWriteLock;

    public EngineImpl(User owner) {
        this.usersCurrentPermission = new HashMap<>();
        this.allPermissionRequests = new LinkedList<>();
        this.owner = owner;
        usersCurrentPermission.put(owner.getUserName(), PermissionType.OWNER);
        this.archive = null;
        this.sheet = null;
        this.sheetName = null;
        this.allPermissionRequests.add(new PermissionRequest(this.owner.getUserName(), PermissionType.OWNER, PermissionStatus.OWNER, 0));
        this.usersCurrentPermissionReadWriteLock = new ReentrantReadWriteLock();
        this.allPermissionRequestsReadWriteLock = new ReentrantReadWriteLock();
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
    public SheetDTO getSheetAsDTO() {
        return new SheetDTO(this.sheet);
    }

    @Override
    public CellDTO getSingleCellData(String cellID) {
        return new CellDTO(this.sheet.getCell(cellID), cellID);
    }

    @Override
    public void updateSingleCellData(String cellID, String value) {
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
    public ColoredSheetDTO getSheetVersionAsDTO(int version) {
        return new ColoredSheetDTO(this.archive.retrieveVersion(version));
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
    public RangesDTO getAllRanges() {
        return new RangesDTO(this.sheet.getRanges());
    }

    @Override
    public void updateCellStyle(String cellID, Color backgroundColor, Color textColor) {
        if (this.sheet.getCell(cellID) == null) {
            Cell cell = new CellImpl(cellID, "", this.sheet.getVersion(), this.sheet);
            this.sheet.getCells().put(cell.getCellId(), cell);
        }

        this.sheet.getCell(cellID).setBackgroundColor(backgroundColor);
        this.sheet.getCell(cellID).setTextColor(textColor);
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
