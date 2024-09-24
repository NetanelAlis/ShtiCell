package gui.graph;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class GraphController {

    @FXML
    private BarChart<String, Number> barChart;

    public void initialize() {
        // Create a series of data for the bar chart
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Category 1");

        series1.getData().add(new XYChart.Data<>("Jan", 100));
        series1.getData().add(new XYChart.Data<>("Feb", 200));
        series1.getData().add(new XYChart.Data<>("Mar", 150));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Category 2");

        series2.getData().add(new XYChart.Data<>("Jan", 300));
        series2.getData().add(new XYChart.Data<>("Feb", 400));
        series2.getData().add(new XYChart.Data<>("Mar", 250));

        // Add series to the bar chart
        barChart.getData().addAll(series1, series2);
    }
}