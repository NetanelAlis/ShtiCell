package gui;

import gui.grid.GridBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GridBuilder gridBuilder = new GridBuilder(10, 10, 35, 110);
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/gui/main/MainAppView.fxml");
        fxmlLoader.setLocation(url);
        BorderPane root = fxmlLoader.load(url.openStream());

        root.setCenter(gridBuilder.createGrid());
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Hello World");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}