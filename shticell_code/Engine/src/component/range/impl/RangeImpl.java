package component.range.impl;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.range.api.Range;
import component.sheet.api.ReadonlySheet;
import component.sheet.api.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class RangeImpl implements Range {
    private String name;
    private String from;
    private String to;
    private List<Cell> cells;
    private int numOfUsages;
    
    
    public RangeImpl(String name, String range, ReadonlySheet sheet) {
        this.name = name;
        this.numOfUsages = 0;
        String[] edges = range.split("\\.\\.");
        this.from = edges[0];
        this.to = edges[1];
        
        if(edges.length != 2) {
            throw new IllegalArgumentException("Range must have exactly two edges");
        } else if (!Sheet.isValidCellID(edges[0]) || !Sheet.isValidCellID(edges[1])) {
            throw new IllegalArgumentException("Expected valid cell ID but got " + edges[0] + " and " + edges[1]);
        } else if (!sheet.cellInLayout(edges[0]) || !sheet.cellInLayout(edges[1])) {
            throw new IllegalArgumentException("Range exceeds sheet boundaries");
        }
        
        this.populateRange(sheet);
        if (cells.isEmpty()) {
            throw new IllegalArgumentException(
                    "Top Left boundary " + this.from +
                    " must be smaller or equal to Bottom Right Boundary " + this.to + " in column and row");
        }
        
    }
    
    @Override
    public void populateRange(ReadonlySheet sheet) {
        this.cells = new ArrayList<>();
        int fromRow = Integer.parseInt(this.from.substring(1));
        int toRow = Integer.parseInt(this.to.substring(1));
        char fromCol = Character.toUpperCase(this.from.charAt(0));
        char toCol = Character.toUpperCase(this.to.charAt(0));
        
        for (int row = fromRow; row <= toRow; row++) {
            for (char col = fromCol; col <= toCol; col++) {
                String currentCellId ="" + col + row;
                Cell currentCell = sheet.getCell(currentCellId);
                
                if (currentCell == null) {
                    currentCell = new CellImpl(currentCellId, "", sheet.getVersion(), sheet);
                    sheet.getCells().put(currentCell.getCellId(), currentCell);
                }
                
                cells.add(currentCell);
            }
        }
    }
    
    @Override
    public List<Cell> getRangeCells() {
        return this.cells;
    }
    
    @Override
    public Cell getFrom() {
        return this.cells.getFirst();
    }
    
    @Override
    public Cell getTo() {
        return this.cells.getLast();
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void addUsage() {
        this.numOfUsages++;
    }
    
    @Override
    public void reduceUsage() {
        if(this.numOfUsages > 0) {
            this.numOfUsages--;
        }
    }
    
    @Override
    public boolean isInUse() {
        return this.numOfUsages > 0;
    }
    
    @Override
    public List<String> getColumnsListOfRange() {
        char leftColumn = this.getFrom().getCellId().substring(0,1).charAt(0);
        char rightColumn = this.getTo().getCellId().substring(0,1).charAt(0);
        
        return IntStream.rangeClosed(leftColumn, rightColumn) // from 'b' to 'f'
                .mapToObj(c -> String.valueOf((char) c)) // convert int to string
                .toList();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeImpl range = (RangeImpl) o;
        return numOfUsages == range.numOfUsages && Objects.equals(name, range.name) && Objects.equals(from, range.from) && Objects.equals(to, range.to) && Objects.equals(cells, range.cells);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, from, to, cells, numOfUsages);
    }
}
