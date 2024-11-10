package client.gui.app;

import client.gui.editor.main.view.MainEditorController;
import client.gui.home.main.view.HomeViewController;
import client.gui.login.LoginController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static client.gui.util.Constants.*;

public class MainAppViewController implements Closeable {
    
    private Parent loginComponent;
    private LoginController logicController;
    
    private Parent homeViewComponent;
    private HomeViewController homeViewController;
    
    private Parent editorViewComponent;
    private MainEditorController editorViewController;
    
    @FXML private Label userGreetingLabel;
    @FXML private AnchorPane mainPanel;
    @FXML private Button backButton;
    
    private final StringProperty currentUserName;
    private Stage primaryStage;
    
    public MainAppViewController() {
        this.currentUserName = new SimpleStringProperty(JOHN_DOE);
    }
    
    @FXML
    public void initialize() {
        this.userGreetingLabel.textProperty().bind(Bindings.concat("Hello ", this.currentUserName));
        
        this.backButton.setVisible(false);
        
        // prepare components
        this.loadLoginPage();
        this.loadHomePage();
        this.loadEditorPage();
    }
    
    @FXML
    void onBackClicked(ActionEvent event) {
        this.switchToHomeView();
    }
    
    public void updateUserName(String userName) {
        this.currentUserName.set(userName);
    }
    
    private void setMainPanelTo(Parent pane) {
        this.mainPanel.getChildren().clear();
        this.mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
    }
    
    @Override
    public void close() throws IOException {
        this.homeViewController.close();
        this.editorViewController.close();
    }
    
    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            this.loginComponent = fxmlLoader.load();
            this.logicController = fxmlLoader.getController();
            this.logicController.setMainAppController(this);
            this.setMainPanelTo(this.loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadHomePage() {
        URL homePageUrl = getClass().getResource(HOME_VIEW_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(homePageUrl);
            this.homeViewComponent = fxmlLoader.load();
            this.homeViewController = fxmlLoader.getController();
            this.homeViewController.setAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadEditorPage() {
        URL editorPageUrl = getClass().getResource(EDITOR_VIEW_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(editorPageUrl);
            this.editorViewComponent = fxmlLoader.load();
            this.editorViewController = fxmlLoader.getController();
            this.editorViewController.setAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void switchToHomeView() {
        this.editorViewController.setInActive();
        this.primaryStage.close();
        this.primaryStage.setWidth(1000);
        this.primaryStage.show();
        this.backButton.setVisible(false);
        this.setMainPanelTo(this.homeViewComponent);
        this.homeViewController.setActive();
    }
    
    public void switchToEditor(String sheetName) {
        this.homeViewController.setInActive();
        this.primaryStage.close();
        this.primaryStage.setWidth(1200);
        this.primaryStage.show();
        this.backButton.setVisible(true);
        this.editorViewController.setActive(sheetName);
        this.setMainPanelTo(this.editorViewComponent);
    }
    
    public Parent getEditorRootElement() {
        return this.editorViewComponent;
    }
    
    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
