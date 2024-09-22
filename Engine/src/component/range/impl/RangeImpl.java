package component.range.impl;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.range.api.Range;
import component.sheet.api.ReadOnlySheet;
import component.sheet.api.Sheet;

import java.io.*;
import java.util.*;

public class RangeImpl implements Range, Serializable {
    private String name;
    private List<Cell> cellsInRange;
    private int usage;
    private String from;
    private String to;
    private List<String> columnsInRange = new ArrayList<>();

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

        this.usage = 0;
        this.from = edges[0];
        this.to = edges[1];
        populateRange(sheet);

        if(cellsInRange.isEmpty()) {
            throw new IllegalArgumentException("Top Left Boundary  " + this.from + " Must be smaller or equal to Bottom Right Boundary "+this.to );
        }


    }

    @Override
    public void populateRange(ReadOnlySheet sheet) {
        this.cellsInRange = new ArrayList<>();
        char beginCol = from.charAt(0);
        beginCol = Character.toUpperCase(beginCol);
        int beginRow = Integer.parseInt(from.substring(1));
        char endCol = to.charAt(0);
        endCol = Character.toUpperCase(endCol);
        int endRow = Integer.parseInt(to.substring(1));

        for (char col = beginCol; col <= endCol; col++) {
            this.columnsInRange.add("" + col);
            for (int row = beginRow; row <= endRow; row++) {
                String currentCellID = "" + col + row;
                Cell currentCell = sheet.getCell(currentCellID);

                if (currentCell == null) {
                    currentCell = new CellImpl(currentCellID, "", sheet.getVersion(), sheet);
                    sheet.getSheetCells().put(currentCell.getCellId(), currentCell);
                }

                cellsInRange.add(currentCell);
            }
        }
    }

    @Override
    public List<String> getColumnsInRange() {
        return this.columnsInRange;
    }

    @Override
    public Range copyRange() {
        try {
            // Serialize this object to a byte array
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream outStream = new ObjectOutputStream(byteOutStream);
            outStream.writeObject(this);
            outStream.flush();
            outStream.close();

            // Deserialize the byte array to a new object
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
            ObjectInputStream inStream = new ObjectInputStream(byteInStream);
            return (RangeImpl) inStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;  // Return null if deep copy fails
        }
    }

    @Override
    public List<Cell> getRangeCells() {
        return this.cellsInRange;
    }

    @Override
    public Cell getFrom() {
        return this.cellsInRange.getFirst();
    }

    @Override
    public Cell getTo() {
        return this.cellsInRange.getLast();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void reduceUsage() {
        if(this.usage > 0) {
            this.usage--;
        }
    }

    @Override
    public void increaseUsage() {
        this.usage++;
    }


    @Override
    public boolean inUse() {
        return this.usage > 0;
    }



}
