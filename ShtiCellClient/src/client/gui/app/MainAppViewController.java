package client.gui.app;

import client.gui.editor.main.view.MainEditorController;
import client.gui.home.main.view.HomeViewController;
import client.gui.login.LoginController;
import client.util.Constants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainAppViewController {

    private Parent loginComponent;
    private LoginController loginController;

    private Parent homeViewComponent;
    private HomeViewController homeViewController;

    private BorderPane editorViewComponent;
    private MainEditorController editorViewController;
    private Stage stage;

    @FXML
    private AnchorPane mainPanel;
    @FXML
    private Label userGreetingLabel;

    private final StringProperty currentUserName;

    public MainAppViewController() {
        currentUserName = new SimpleStringProperty("");
    }

    @FXML
    public void initialize() {
        userGreetingLabel.textProperty().bind(Bindings.concat(currentUserName));

        this.loadLoginPage();
        this.loadHomePage();
        this.loadEditorPage();

    }

    private void loadHomePage() {
        URL loginPageUrl = getClass().getResource(Constants.HOME_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            this.homeViewComponent = fxmlLoader.load();
            this.homeViewController = fxmlLoader.getController();
            this.homeViewController.setAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            this.loginComponent = fxmlLoader.load();
            this.loginController = fxmlLoader.getController();
            this.loginController.setAppMainController(this);
            this.setMainPanelTo(this.loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadEditorPage() {
        URL loginPageUrl = getClass().getResource(Constants.EDITOR_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            this.editorViewComponent = fxmlLoader.load();
            this.editorViewController = fxmlLoader.getController();
            this.editorViewController.setAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    public void switchToHomePage() {
        this.editorViewController.setInActive();
        this.stage.close();
        this.stage.setWidth(1000);
        this.stage.show();
        setMainPanelTo(this.homeViewComponent);
        this.homeViewController.setActive();
    }

    public void switchToEditorPage(String sheenName) {
        this.homeViewController.setInActive();
        this.stage.close();
        this.stage.setWidth(1200);
        this.stage.show();
        this.editorViewController.setActive(sheenName);
        setMainPanelTo(this.editorViewComponent);
    }

//    public void switchToLogin() {
//        Platform.runLater(() -> {
//            this.currentUserName.set("");
//            chatRoomComponentController.setInActive();
//            setMainPanelTo(loginComponent);
//        });
//    }

    public void updateUserName(String userName) {
        currentUserName.set("Hello " + userName);
    }


    public void close() throws IOException {
        this.homeViewController.close();
    }

    public Parent getEditorViewRootComponent() {
        return this.editorViewComponent;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
