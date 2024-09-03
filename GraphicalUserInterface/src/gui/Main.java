package gui;


import gui.grid.GridBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GridBuilder gridBuilder = new GridBuilder(10, 10, 35, 110);
        BorderPane root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("resources/MainAppView.fxml")));
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