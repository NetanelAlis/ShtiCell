package client.gui.home.Command;

import client.gui.home.main.view.HomeViewController;
import client.task.PermissionRequestsTableRefresher;
import dto.permission.ReceivedPermissionRequestDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static client.gui.util.Constants.REFRESH_RATE;

public class CommandsController implements Closeable {
    
    @FXML private TableView<PermissionRequestTableEntry> recievedRequestsTableView;
    @FXML private TableColumn<PermissionRequestTableEntry, String> senderColumn;
    @FXML private TableColumn<PermissionRequestTableEntry, String> sheetNameColumn;
    @FXML private TableColumn<PermissionRequestTableEntry, String> permissionColumn;
    @FXML private ChoiceBox<String> newPermissionChoiceBox;
    @FXML private TextField sheetNameTextField;
    @FXML private Button sendPermissionRequestButton;
    @FXML private Button acceptRequestButton;
    @FXML private Button declineRequestButton;
    @FXML private Label newRequestErrorLabel;
    @FXML private Label viewSheetErrorLabel;
    
    private ObservableList<PermissionRequestTableEntry> receivedRequests;
    private StringProperty newRequestErrorProperty;
    private StringProperty viewSheetErrorProperty;
    private HomeViewController homeViewController;
    private TimerTask tableRefresher;
    private Timer timer;
    
    public CommandsController() {
        this.receivedRequests = FXCollections.observableArrayList();
        this.newRequestErrorProperty = new SimpleStringProperty("");
        this.viewSheetErrorProperty = new SimpleStringProperty("");
    }
    
    @FXML
    private void initialize() {
        this.initializeChoiceBox();
        this.initializeTableView();
        
        this.newRequestErrorLabel.textProperty().bind(this.newRequestErrorProperty);
        this.viewSheetErrorLabel.textProperty().bind(this.viewSheetErrorProperty);
        
        this.sendPermissionRequestButton.disableProperty().bind(
                Bindings.or(this.sheetNameTextField.textProperty().isEmpty(),
                        this.newPermissionChoiceBox.getSelectionModel()
                                .selectedItemProperty().isEqualTo("Permission Type"))
        );
        
        this.acceptRequestButton.disableProperty().bind(
                this.recievedRequestsTableView.getSelectionModel().selectedItemProperty().isNull());
        
        this.declineRequestButton.disableProperty().bind(
                this.recievedRequestsTableView.getSelectionModel().selectedItemProperty().isNull());
        
    }
    
    private void initializeChoiceBox() {
        this.newPermissionChoiceBox.getItems().add("Permission Type");
        this.newPermissionChoiceBox.getItems().add("Reader");
        this.newPermissionChoiceBox.getItems().add("Writer");
        this.newPermissionChoiceBox.getItems().add("None");
        this.newPermissionChoiceBox.getSelectionModel().selectFirst();
    }
    
    private void initializeTableView() {
        this.senderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        this.sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        this.permissionColumn.setCellValueFactory(new PropertyValueFactory<>("permission"));
        
        this.recievedRequestsTableView.setItems(this.receivedRequests);
    }
    
    public void addRequestEntry(ReceivedPermissionRequestDTO requestToAdd) {
        this.receivedRequests.add(new PermissionRequestTableEntry(
                requestToAdd.getSender(),
                requestToAdd.getSheetName(),
                requestToAdd.getRequestedPermission(),
                requestToAdd.getRequestID()));
    }
    
    private void updateReceivedRequestsTable(List<ReceivedPermissionRequestDTO> requests) {
        Platform.runLater(() -> {
            PermissionRequestTableEntry selectedRequest =
                    this.recievedRequestsTableView.getSelectionModel().getSelectedItem();
            
            receivedRequests.clear();
            requests.forEach(this::addRequestEntry);
            
            if (selectedRequest != null) {
                for (PermissionRequestTableEntry requestEntry : this.recievedRequestsTableView.getItems()) {
                    if (Objects.equals(requestEntry, selectedRequest)) {
                        this.recievedRequestsTableView.getSelectionModel().select(requestEntry);
                        break;
                    }
                }
            }
            
        });
    }
    
    public void startTableRefresher() {
        this.tableRefresher = new PermissionRequestsTableRefresher(this::updateReceivedRequestsTable);
        timer = new Timer();
        timer.schedule(this.tableRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    
    public void updateNewRequestErrorLabel(String message) {
        this.newRequestErrorProperty.set(message);
    }
    
    public void updateViewSheetErrorLabel(String message) { this.viewSheetErrorProperty.set(message); }
    
    public void clearNewPermissionRequestsFields() {
        this.sheetNameTextField.textProperty().set("");
        this.newPermissionChoiceBox.getSelectionModel().selectFirst();
        this.newRequestErrorProperty.set("");
    }
    
    @FXML
    void onAcceptRequestClicked(ActionEvent event) {
        PermissionRequestTableEntry selectedRequest =
                this.recievedRequestsTableView.getSelectionModel().getSelectedItem();
        
        if (selectedRequest != null) {
            this.homeViewController.replyToPermissionRequest(selectedRequest, true);
        }
    }
    
    @FXML
    void onDeclineRequestClicked(ActionEvent event) {
        PermissionRequestTableEntry selectedRequest =
                this.recievedRequestsTableView.getSelectionModel().getSelectedItem();
        
        if (selectedRequest != null) {
            this.homeViewController.replyToPermissionRequest(selectedRequest, false);
        }
    }
    
    @FXML
    void onSendButtonClicked(ActionEvent event) {
        this.homeViewController.sendNewPermissionRequest(
                this.sheetNameTextField.getText(),
                this.newPermissionChoiceBox.getSelectionModel().getSelectedItem());
    }
    
    @FXML
    void onViewSheetClicked(ActionEvent event) {
        if (this.homeViewController.viewSheet()) {
            this.viewSheetErrorProperty.set("");
        }
    }
    
    public void setMainController(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }
    
    @Override
    public void close() throws IOException {
        this.recievedRequestsTableView.getItems().clear();
        if (this.tableRefresher != null && timer != null) {
            this.tableRefresher.cancel();
            this.timer.cancel();
        }
    }
}
