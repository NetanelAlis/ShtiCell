package client.main;

import client.gui.app.MainAppViewController;
import client.gui.util.http.HttpClientUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static client.gui.util.Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION;
import static client.gui.util.Constants.SHTICELL_LOGO_RESOURCE_LOCATION;

public class Main extends Application {
    
    private MainAppViewController mainAppViewController;
    
    @Override
    public void start(Stage primaryStage) {
        
        URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            this.mainAppViewController = fxmlLoader.getController();
            
            Scene scene = new Scene(root, 400, 600);
            primaryStage.setTitle("ShtiCell v3.0");
            primaryStage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream(SHTICELL_LOGO_RESOURCE_LOCATION))));
            
            primaryStage.setScene(scene);
            this.mainAppViewController.setStage(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop() throws Exception {
        HttpClientUtil.shutdown();
        this.mainAppViewController.close();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
