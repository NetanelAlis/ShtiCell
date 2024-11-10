package client.gui.editor.graph;

import dto.returnable.EffectiveValueDTO;
import javafx.scene.chart.*;
import java.util.LinkedHashMap;
import java.util.Map;

public enum GraphType {
    LINE_CHART("Line Chart") {
        
        @Override
        public Chart createChart(
                LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData) {
            
            try {
                LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());
                lineChart.setTitle(getChartType());
                populateChartWithData(graphData, lineChart);

                return lineChart;
            } catch (NullPointerException e) {
                throw new NullPointerException("Series name cannot be empty");
            }
        }
    },
    BAR_CHART("Bar Chart") {
        
        @Override
        public Chart createChart(
                LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData) {
            
            try {
                BarChart<String, Number> barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
                barChart.setTitle(getChartType());

                populateChartWithData(graphData, barChart);
                return barChart;
            } catch (NullPointerException e) {
                throw new NullPointerException("Series name cannot be empty");
            }
        }
    },
    PIE_CHART("Pie Chart") {
        
        @Override
        public Chart createChart(
                LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData) {
            
            try{
                PieChart pieChart = new PieChart();
                pieChart.setTitle(getChartType());

                graphData.forEach((seriesName, value) -> {
                    value.forEach((xAxis, yAxis) -> {
                        try {
                            String label = seriesName.getEffectiveValue() + ": " + xAxis.getEffectiveValue();
                            double yAxisValue = Double.parseDouble(yAxis.getEffectiveValue());
                            pieChart.getData().add(new PieChart.Data(label, yAxisValue));
                        } catch (NumberFormatException e) {
                            throw new ClassCastException(
                                    "Invalid Y-Axis for series: "+ seriesName.getEffectiveValue()
                                            + " expected numeric but got:  " + yAxis.getEffectiveValue());
                        } catch (RuntimeException e) {
                            throw new NullPointerException(
                                    "Empty Y-axis value for series: " + seriesName.getEffectiveValue());
                        }
                    });
                });

                return pieChart;
            } catch (NullPointerException e) {
                throw new NullPointerException("Series name cannot be empty");            }
        }
    };

    private final String chartType;

    GraphType(String chartType) {
        this.chartType = chartType;
    }

    public String getChartType() {
        return chartType;
    }

    public abstract Chart createChart(
            LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData);

    // Common method to populate LineChart or BarChart with data
    protected void populateChartWithData(
            LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData,
            XYChart<String, Number> chart) {
        
        graphData.forEach((seriesName, value) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(seriesName.getEffectiveValue());

            value.forEach((xAxis, yAxis) -> {
                try {
                    double yAxisValue = Double.parseDouble(yAxis.getEffectiveValue());
                    series.getData().add(new XYChart.Data<>(xAxis.getEffectiveValue(), yAxisValue));
                } catch (NumberFormatException e) {
                    throw new ClassCastException(
                            "Invalid Y-Axis for series: "+ seriesName.getEffectiveValue()
                                    + " expected numeric but got:  " + yAxis.getEffectiveValue());
                } catch (NullPointerException e) {
                    throw new NullPointerException(
                            "Empty Y-axis value for series: " + seriesName.getEffectiveValue());
                }
            });

            chart.getData().add(series);
        });
    }
    
    public static LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> recreateOriginalMap(
            LinkedHashMap<String, LinkedHashMap<String, EffectiveValueDTO>> stringMap) {
        
        // Create a new LinkedHashMap to store the result
        LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> originalMap = new LinkedHashMap<>();
        
        // Iterate over the outer map (String -> LinkedHashMap<String, EffectiveValueDTO>)
        for (Map.Entry<String, LinkedHashMap<String, EffectiveValueDTO>> outerEntry : stringMap.entrySet()) {
            
            // Convert the outer String key to EffectiveValueDTO
            EffectiveValueDTO outerKey = convertStringToEffectiveValueDTO(outerEntry.getKey());
            
            // Get the inner map (String -> EffectiveValueDTO)
            LinkedHashMap<String, EffectiveValueDTO> innerMap = outerEntry.getValue();
            
            // Create a new inner map for the EffectiveValueDTO keys and values
            LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO> convertedInnerMap = new LinkedHashMap<>();
            
            // Iterate over the inner map and convert the String keys to EffectiveValueDTO
            for (Map.Entry<String, EffectiveValueDTO> innerEntry : innerMap.entrySet()) {
                EffectiveValueDTO innerKey = convertStringToEffectiveValueDTO(innerEntry.getKey());
                EffectiveValueDTO innerValue = innerEntry.getValue();
                
                // Put the converted keys and values into the new inner map
                convertedInnerMap.put(innerKey, innerValue);
            }
            
            // Put the converted outer key and inner map into the original map
            originalMap.put(outerKey, convertedInnerMap);
        }
        
        return originalMap;
    }
    
    // Helper method to convert a String in the format "effectiveValue-cellType" to EffectiveValueDTO
    private static EffectiveValueDTO convertStringToEffectiveValueDTO(String str) {
        String[] parts = str.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid format for EffectiveValueDTO key: " + str);
        }
        return new EffectiveValueDTO(parts[0], parts[1]); // Create a new EffectiveValueDTO with effectiveValue and cellType
    }
}