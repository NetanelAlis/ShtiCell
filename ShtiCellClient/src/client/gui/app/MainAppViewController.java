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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class MainAppViewController {

    private Parent loginComponent;
    private LoginController loginController;

    private Parent homeViewComponent;
    private HomeViewController homeViewController;

    private Parent editorViewComponent;
    private MainEditorController editorViewController;

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
//        loadEditorPage();

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
            this.setMainPanelTo(this.editorViewComponent);
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
        setMainPanelTo(this.homeViewComponent);
        this.homeViewController.setActive();
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

}
