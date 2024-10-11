package client.gui.home.command;

import client.gui.home.main.view.HomeViewController;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CommandComponentController {

    @FXML
    private Button acceptPermissionButton;

    @FXML
    private Button declinePremissionButton;

    @FXML
    private ChoiceBox<String> permissionChoiceBox;

    @FXML
    private TableColumn<?, ?> permissionCol;

    @FXML
    private TableView<?> requestTableView;

    @FXML
    private Button sendPermissonRequestButton;

    @FXML
    private TableColumn<?, ?> senderCol;

    @FXML
    private TableColumn<?, ?> sheetNameCol;

    @FXML
    private TextField sheetNameTextField;

    @FXML
    private Button viewSheetButton;
    private HomeViewController homeViewController;

    @FXML
    private void initialize() {
        this.permissionChoiceBox.getItems().add("Permission Type");
        this.permissionChoiceBox.getItems().add("Reader");
        this.permissionChoiceBox.getItems().add("Writer");
        this.permissionChoiceBox.getItems().add("None");
        this.permissionChoiceBox.getSelectionModel().selectFirst();

        this.acceptPermissionButton.disableProperty().bind(Bindings.or(
                this.sheetNameTextField.textProperty().isEmpty(),
                this.permissionChoiceBox.getSelectionModel().selectedItemProperty().isEqualTo("Permission Type")));
    }

    @FXML
    void onAcceptPermissionClicked(ActionEvent event) {

    }

    @FXML
    void onDeclinePermissionClicked(ActionEvent event) {

    }

    @FXML
    void onSendPermissionClicked(ActionEvent event) {
        this.homeViewController.homeViewControllerSendNewPermissionRequest(this.sheetNameTextField.getText(),
                this.permissionChoiceBox.getSelectionModel().getSelectedItem());

    }

    @FXML
    void onViewSheetClicked(ActionEvent event) {

    }

    public void setHomeViewController(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }



}
