package client.gui.editor.graph;

import dto.returnable.EffectiveValueDTO;
import javafx.scene.chart.*;
import java.util.LinkedHashMap;


public enum GraphType {
    LINE_CHART("Line Chart") {
        @Override
        public Chart createChart(LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData) {
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
        public Chart createChart(LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData) {
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
        public Chart createChart(LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData) {
            try{
                PieChart pieChart = new PieChart();
                pieChart.setTitle(getChartType());

                graphData.forEach((seriesName, value) -> {
                    value.forEach((xAxis, yAxis) -> {
                        try {
                            double yAxisValue = Double.parseDouble(yAxis.getEffectiveValue());
                            String label = seriesName.getEffectiveValue() + ": " + xAxis.getEffectiveValue();
                            pieChart.getData().add(new PieChart.Data(label, yAxisValue));
                        } catch (NumberFormatException e) {
                            throw new ClassCastException("Invalid Y-Axis for series: "+ seriesName.getEffectiveValue() + " expected numeric but got:  " + yAxis.getEffectiveValue());
                        } catch (RuntimeException e) {
                            throw new NullPointerException("Empty Y-axis value for series: " + seriesName.getEffectiveValue());
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

    public abstract Chart createChart(LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData);

    // Common method to populate LineChart or BarChart with data
    protected void populateChartWithData(LinkedHashMap<EffectiveValueDTO, LinkedHashMap<EffectiveValueDTO, EffectiveValueDTO>> graphData, XYChart<String, Number> chart) {
        graphData.forEach((seriesName, value) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(seriesName.getEffectiveValue());

            value.forEach((xAxis, yAxis) -> {
                try {
                    double yAxisValue = Double.parseDouble(yAxis.getEffectiveValue());
                    series.getData().add(new XYChart.Data<>(xAxis.getEffectiveValue(), yAxisValue));
                } catch (NumberFormatException e) {
                    throw new ClassCastException("Invalid Y-Axis for series: "+ seriesName.getEffectiveValue() + " expected numeric but got:  " + yAxis.getEffectiveValue());
                } catch (NullPointerException e) {
                    throw new NullPointerException("Empty Y-axis value for series: " + seriesName.getEffectiveValue());
                }
            });

            chart.getData().add(series);
        });
    }
}