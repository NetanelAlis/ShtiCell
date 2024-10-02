package gui;

import gui.main.view.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/gui/main/view/MainAppView.fxml");
        loader.setLocation(url);
        BorderPane root = loader.load(url.openStream());
        MainViewController controller = loader.getController();
        
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("ShtiCell v2.0");
        stage.getIcons().add(
                new Image(Objects.requireNonNull(
                        Main.class.getResourceAsStream("/gui/main/view/style/shticellLogo.png"))));
        stage.setScene(scene);
        controller.setPrimaryStage(stage);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
