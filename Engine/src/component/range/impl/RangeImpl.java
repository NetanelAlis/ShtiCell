package component.range.impl;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.range.api.Range;
import component.sheet.api.ReadOnlySheet;
import component.sheet.api.Sheet;

import java.util.*;

public class RangeImpl implements Range {
    private String name;
    private Set<Cell> cellsInRange;

    public RangeImpl(String name, String cellsRange, ReadOnlySheet sheet) {
        this.name = name;
        String[] edges = cellsRange.split("\\.\\.");
        if(edges.length != 2) {
        throw new IllegalArgumentException("Range must include exactly two edges");
        } else if(!Sheet.isValidCellID(edges[0]) || !Sheet.isValidCellID(edges[1])) {
            throw new IllegalArgumentException("Expected valid cell ID but got " + edges[0] + " and " + edges[1]);
        }
        else if(!sheet.cellInLayout(edges[0]) || !sheet.cellInLayout(edges[1])) {
            throw new IllegalArgumentException("Range exceeds sheet boundaries");
        }

        this.cellsInRange = this.createRange(edges[0], edges[1], sheet);


    }

    private Set<Cell> createRange(String from, String end, ReadOnlySheet sheet) {
        Set<Cell> cells = new HashSet<Cell>();
        char beginCol = from.charAt(0);
        int beginRow = Integer.parseInt(from.substring(1));
        char endCol = end.charAt(0);
        int endRow = Integer.parseInt(end.substring(1));

        for (char col = beginCol; col <= endCol; col++) {
            for (int row = beginRow; row <= endRow; row++) {
                String currentCellID = "" + col + row;
                Cell currentCell = sheet.getCell(currentCellID);

                if (currentCell == null) {
                    currentCell = new CellImpl(currentCellID, "", sheet.getVersion(), sheet);
                    sheet.getSheetCells().put(currentCell.getCellId(), currentCell);
                }

                cells.add(currentCell);
            }
        }

        return cells;
    }



    @Override
    public List<Cell> getRangeCells() {
        return List.of();
    }

    @Override
    public Cell getFrom() {
        return null;
    }

    @Override
    public Cell getTo() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
