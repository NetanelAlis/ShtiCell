package client;

import client.gui.app.MainAppViewController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    private MainAppViewController mainAppViewController;
    @Override
    public void start(Stage stage){
        URL url = getClass().getResource(Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);
            Parent root = loader.load(url.openStream());
            MainAppViewController mainAppViewController = loader.getController();
            Scene scene = new Scene(root, 1000, 600);
            stage.setTitle("ShtiCell v3.0");
            stage.getIcons().add(
                    new Image(Objects.requireNonNull(
                            Main.class.getResourceAsStream("/client/gui/resources/shticellLogo.png"))));
            stage.setScene(scene);
            //        controller.setPrimaryStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws IOException {
        HttpClientUtil.shutdown();
        this.mainAppViewController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
