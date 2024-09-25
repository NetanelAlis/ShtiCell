package logic;

import component.archive.api.Archive;
import component.archive.impl.ArchiveImpl;
import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.range.api.Range;
import component.range.impl.RangeImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import dto.*;
import jakarta.xml.bind.JAXBException;
import javafx.scene.paint.Color;
import jaxb.converter.XMLToSheetConverter;
import logic.filter.Filter;
import logic.function.returnable.Returnable;
import logic.sort.Sorter;
import java.io.FileNotFoundException;
import java.util.*;

public class EngineImpl implements Engine {

    private Sheet sheet = null;
    private Archive archive = null;
    private List<Returnable> itemsInColumnToFilter = null;

    @Override
    public void LoadDataFromXML(String path) {
        try {
            XMLToSheetConverter converter = new jaxb.converter.XMLToSheetConverterImpl();
            this.sheet = converter.convert(path);
            this.archive = new ArchiveImpl();
            this.archive.storeInArchive(this.sheet.copySheet());
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException("Error loading data from XML", e);
        }

    }

    @Override
    public SheetDTO getSheetAsDTO() {
        return new SheetDTO(this.sheet);
    }

    @Override
    public CellDTO geCellAsDTO(String cellID) {
        return new CellDTO(this.sheet.getCell(cellID), cellID);
    }

    @Override
    public void updateSingleCellData(String cellId, String value) {
        Cell cellToUpdate = this.sheet.getCell(cellId);

        boolean originalAndCellAreEmpty = (cellToUpdate == null) && value.isEmpty();
        boolean isOriginalValueChanged = cellToUpdate != null && !cellToUpdate.getOriginalValue().equals(value);

        if (originalAndCellAreEmpty) {
            return;
        }

        SheetImpl newSheetVersion = this.sheet.copySheet();
        updateCell(cellId, value, newSheetVersion);
        Sheet tempSheet = this.sheet.updateSheet(newSheetVersion, isOriginalValueChanged);
        if (!tempSheet.equals(this.sheet)) {
            this.sheet = tempSheet;
            this.archive.storeInArchive(this.sheet.copySheet());
        }
    }

    @Override
    public boolean isSheetLoaded() {
        return this.sheet != null;
    }

    private void updateCell(String cellToUpdateID, String value, Sheet newSheetVersion) {
        Cell updatedCell = newSheetVersion.getCell(cellToUpdateID);
        if (updatedCell != null) {
            updatedCell.getUsedRanges()
                    .forEach(rangeName -> {
                        Range currentRange = newSheetVersion.getRanges().get(rangeName);
                        if (currentRange != null) {
                            currentRange.reduceUsage();
                        }
                    });
            updatedCell.setOriginalValue(value, newSheetVersion.getVersion() + 1);
        } else {
            updatedCell = new CellImpl(cellToUpdateID, value, newSheetVersion.getVersion(), newSheetVersion);
            newSheetVersion.getSheetCells().put(updatedCell.getCellId(), updatedCell);
        }
    }

    @Override
    public VersionsChangesDTO getVersionsChangesAsDTO() {
        return new VersionsChangesDTO(this.archive.getAllVersionsChanges());
    }

    @Override
    public SheetDTO getSheetVersionsAsDTO(int version) {
        return new SheetDTO(this.archive.retrieveFromArchive(version));
    }

    @Override
    public void saveToFile(String path) {
        this.archive.saveToFile(path);
    }

    @Override
    public void LoadDataFromFile(String path) {
        this.archive = Archive.loadFromFile(path);
        this.sheet = this.archive.retrieveLastSheetVersionFromArchive();
    }

    @Override
    public void addRange(String rangeName, String range) {
        this.sheet.createRange(rangeName, range);
    }

    @Override
    public void deleteRange(String rangeName) {
        this.sheet.deleteRange(rangeName);
    }

    @Override
    public RangeDTO getRangesAsDTO(String rangeName) {
        return new RangeDTO(this.sheet.getRanges().get(rangeName));
    }

    @Override
    public RangesDTO getAllRangesAsDTO() {
        return new RangesDTO(this.sheet.getRanges());
    }

    @Override
    public void updateBackgroundColor(Color color, String cellID) {
        Cell cellToUpdate = this.sheet.getCell(cellID);

        if (cellToUpdate == null) {
            cellToUpdate = new CellImpl(cellID, "", this.sheet.getVersion(), this.sheet);
            this.sheet.getSheetCells().put(cellID, cellToUpdate);
        }

        cellToUpdate.setBackgroundColor(color);
    }

    @Override
    public void updateTextColor(Color color, String cellID) {
        Cell cellToUpdate = this.sheet.getCell(cellID);

        if (cellToUpdate == null) {
            cellToUpdate = new CellImpl(cellID, "", this.sheet.getVersion(), this.sheet);

            this.sheet.getSheetCells().put(cellID, cellToUpdate);
        }

        cellToUpdate.setTextColor(color);
    }

    @Override
    public SheetDTO sortRangeCells(String cellsInRange, List<String> colsToSortBy) {
        Sheet copySheet = this.sheet.copySheet();
        Sorter sorter = new Sorter(new RangeImpl("", cellsInRange, copySheet), colsToSortBy);

        Range sortedRange = sorter.sort();
        List<Cell> sortedCells = sortedRange.getRangeCells();


        sortedCells.forEach(cell -> {
            copySheet.getSheetCells().put(cell.getCellId(), cell);
        });

        return new SheetDTO(copySheet);
    }

    @Override
    public List<String> getColumnsToSortBy(String cellsInRange) {
        try {
            Range range = new RangeImpl("", cellsInRange, this.sheet.copySheet());
            return range.getColumnsInRange();
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Returnable> getItemsToFilterBy(String colToFilterBy, String cellsInRange) {
        Range range = new RangeImpl("", cellsInRange, this.sheet.copySheet());

        return this.getUniqueItemsInColumn(colToFilterBy, range);
        }

    @Override
    public SheetDTO getFilterSheet(String cellsInRange, List<Integer> itemsToFilterByIndexes, String colToSortBy) {
        Sheet copySheet = this.sheet.copySheet();
        Range range = new RangeImpl("", cellsInRange, copySheet);

        range.getRangeCells().forEach(cell -> {
            copySheet.getSheetCells().remove(cell.getCellId(), cell);
        });
        Filter filter = new Filter(range);

        List<Returnable> uniqItemsList = this.getUniqueItemsInColumn(colToSortBy, range);
        List<Returnable> filteredItemsList = new ArrayList<>();

        for(Integer i : itemsToFilterByIndexes) {
            filteredItemsList.add(uniqItemsList.get(i));
        }
        filter.filter(colToSortBy, filteredItemsList)
                .getRangeCells()
                .forEach(cell -> copySheet.getSheetCells().put(cell.getCellId(), cell));

        return new SheetDTO(copySheet);
    }

        private List<Returnable> getUniqueItemsInColumn(String col, Range range) {
            List<Cell> itemsList = range.getRangeCells()
                    .stream()
                    .filter(cell -> cell.getCellId().contains(col))
                    .toList();

            Set<Returnable> itemsSet = new LinkedHashSet<>();
            itemsList.forEach(cell -> itemsSet.add(cell.getEffectiveValue()));

            return new ArrayList<>(itemsSet);
        }

    }
