package logic.engine;

import dto.cell.CellDTO;
import dto.cell.CellStyleDTO;
import dto.permission.RequestedRequestForTableDTO;
import dto.range.RangeDTO;
import dto.range.RangesDTO;
import dto.returnable.EffectiveValueDTO;
import dto.sheet.ColoredSheetDTO;
import dto.sheet.SheetDTO;
import dto.sheet.SheetMetaDataDTO;
import dto.version.VersionChangesDTO;
import javafx.scene.paint.Color;
import logic.function.returnable.api.Returnable;
import user.permission.PermissionType;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public interface Engine {
    void loadData(String path);

    void loadDataFromInputStream(InputStream inputStream);

    void updateSingleCellData(String cellID, String value);

    boolean isSheetLoaded();

    void loadFromFile(String path);

    void saveToFile(String path);

    RangeDTO addRange(String rangeName, String range);

    void removeRange(String rangeName);

    RangesDTO getAllRanges();

    void updateCellStyle(CellStyleDTO cellStyleDTO);

    List<String> getColumnsListOfRange(String rangeToFilter);

    List<EffectiveValueDTO> getUniqueItemsToFilterBy(String columnToFilterBy, String rangeToFilter);

    LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> getGraphFromRange(String rangeToBuildGraphFrom);

    String getSheetName();

    void createPermissionRequest(PermissionType requestedPermission, String username);

    //// DTOs///////
    SheetDTO getSheetAsDTO();

    CellDTO getSingleCellData(String cellID);

    VersionChangesDTO showVersions();

    ColoredSheetDTO getSheetVersionAsDTO(int version);

    ColoredSheetDTO sortRangeOfCells(String range, List<String> columnsToSortBy);

    ColoredSheetDTO filterRangeOfCells(String rangeToFilterBy, String columnToFilterBy, List<Integer> itemsToFilterBy);

    SheetMetaDataDTO getSheetMetaDataDTO(String userName);

    List<RequestedRequestForTableDTO> getAllRequestsAsRequestedRequestForTableDTO();

    void updatePermissionStatus(String requesterUserName, PermissionType requestedPermission, boolean requestApproved, int requestNumber);
}
