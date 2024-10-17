package logic.graph;

import component.cell.api.Cell;
import component.range.api.Range;
import dto.returnable.EffectiveValueDTO;

import java.util.*;

public class GraphSeriesBuilder {
    private Range graphRange;
    private String leftCol;
    private String rightCol;
    private int topRow;

    public GraphSeriesBuilder(Range graphRange) {
        this.graphRange = graphRange;
        this.leftCol = graphRange.getFrom().getCellId().substring(0,1);
        this.rightCol = graphRange.getTo().getCellId().substring(0,1);
        this.topRow = Integer.parseInt(graphRange.getFrom().getCellId().substring(1));
    }

    public LinkedHashMap<EffectiveValueDTO, LinkedHashMap <EffectiveValueDTO, EffectiveValueDTO>> build() {

        LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphSeries = new LinkedHashMap<>();
        List<Cell> xAxisCells = this.getCellsInRow(topRow);
        xAxisCells = xAxisCells.subList(1,xAxisCells.size());
        List<Cell> seriesCategories = this.getCellsInColumn(graphRange.getFrom().getCellId().substring(0,1));
        seriesCategories = seriesCategories.subList(1,seriesCategories.size());
        List<List<Cell>> yAxisCells = this.getCollFromRange();

        for(int i = 0; i < seriesCategories.size(); i++){
            List<Cell> currentYaxisCells = this.getCurrentYaxisCells(i, yAxisCells);
            graphSeries.put(new EffectiveValueDTO(seriesCategories.get(i).getEffectiveValue()) , this.createChart(xAxisCells,currentYaxisCells));

        }

        return graphSeries;
    }

    private List<Cell> getCurrentYaxisCells(int seriesIndex, List<List<Cell>> yAxisCells) {
        List<Cell> currentYaxisCells = new ArrayList<>();

        for (List<Cell> yAxisCell : yAxisCells) {
            currentYaxisCells.add(yAxisCell.get(seriesIndex+1));
        }

        return currentYaxisCells;
    }

    private List<List<Cell>> getCollFromRange() {
        List<List<Cell>> cols = new ArrayList<>();

        for (char col = (char)(this.leftCol.charAt(0) + 1); col <= this.rightCol.charAt(0); col++) {
            cols.add(this.getCellsInColumn("" + col));
        }

        return cols;
    }

    private LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO> createChart(List<Cell> seriesCategories, List<Cell> yCells) {
        LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO> xyChartMap = new LinkedHashMap<>();
        for (int i = 0; i <= seriesCategories.size() - 1 ; i++) {
            xyChartMap.put(new EffectiveValueDTO(seriesCategories.get(i).getEffectiveValue()) ,new EffectiveValueDTO(yCells.get(i).getEffectiveValue()));
        }

        return xyChartMap;
    }

    private List<Cell> getCellsInColumn(String columnToFilterBy) {
        return this.graphRange.getRangeCells()
                .stream()
                .filter((cell -> cell.getCellId().contains(columnToFilterBy)))
                .toList();
    }

    private List<Cell> getCellsInRow(int rowNumber) {
        return this.graphRange.getRangeCells()
                .stream()
                .filter((cell -> cell.getCellId().contains(String.valueOf(rowNumber))))
                .toList();
    }
}