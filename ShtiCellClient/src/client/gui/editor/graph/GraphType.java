package client.gui.editor.graph;

import javafx.scene.chart.*;
import logic.function.returnable.api.Returnable;
import java.util.LinkedHashMap;


public enum GraphType {
    LINE_CHART("Line Chart") {
        @Override
        public Chart createChart(LinkedHashMap<Returnable, LinkedHashMap<Returnable, Returnable>> graphData) {
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
        public Chart createChart(LinkedHashMap<Returnable, LinkedHashMap<Returnable, Returnable>> graphData) {
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
        public Chart createChart(LinkedHashMap<Returnable, LinkedHashMap<Returnable, Returnable>> graphData) {
            try{
                PieChart pieChart = new PieChart();
                pieChart.setTitle(getChartType());

                graphData.forEach((seriesName, value) -> {
                    value.forEach((xAxis, yAxis) -> {
                        try {
                            String label = seriesName.getValue().toString() + ": " + xAxis.getValue().toString();
                            pieChart.getData().add(new PieChart.Data(label, yAxis.tryConvertTo(Double.class)));
                        } catch (ClassCastException e) {
                            throw new ClassCastException("Invalid Y-Axis for series: "+ seriesName.getValue() + " expected numeric but got:  " + yAxis.getValue());
                        } catch (RuntimeException e) {
                            throw new NullPointerException("Empty Y-axis value for series: " + seriesName.getValue());
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

    public abstract Chart createChart(LinkedHashMap<Returnable, LinkedHashMap<Returnable, Returnable>> graphData);

    // Common method to populate LineChart or BarChart with data
    protected void populateChartWithData(LinkedHashMap<Returnable, LinkedHashMap<Returnable, Returnable>> graphData, XYChart<String, Number> chart) {
        graphData.forEach((seriesName, value) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("" + seriesName.getValue());

            value.forEach((xAxis, yAxis) -> {
                try {
                    series.getData().add(new XYChart.Data<>(xAxis.getValue().toString(), yAxis.tryConvertTo(Double.class)));
                } catch (ClassCastException e) {
                    throw new ClassCastException("Invalid Y-Axis for series: "+ seriesName.getValue() + " expected numeric but got:  " + yAxis.getValue());
                } catch (NullPointerException e) {
                    throw new NullPointerException("Empty Y-axis value for series: " + seriesName.getValue());
                }
            });

            chart.getData().add(series);
        });
    }
}