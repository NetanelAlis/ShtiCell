package client.gui.home.command;

import client.gui.home.main.view.HomeViewController;
import client.task.ReceivedPermissionTableRefresher;
import dto.permission.ReceivedRequestForTableDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.ResponseBody;
import java.io.Closeable;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static client.util.Constants.REFRESH_RATE;

public class CommandComponentController implements Closeable {

    @FXML
    private Button acceptPermissionButton;

    @FXML
    private Button declinePremissionButton;

    @FXML
    private ChoiceBox<String> permissionChoiceBox;

    @FXML
    private TableView<PermissionRequestTableEntry> requestTableView;

    @FXML
    private TableColumn<PermissionRequestTableEntry, String> permissionsColumn;

    @FXML
    private TableColumn<PermissionRequestTableEntry, String> senderColumn;

    @FXML
    private TableColumn<PermissionRequestTableEntry, String> sheetNameColumn;

    @FXML
    private Button sendPermissonRequestButton;

    @FXML
    private TextField sheetNameTextField;

    @FXML
    private Button viewSheetButton;

    private HomeViewController homeViewController;
    private TimerTask tableRefresher;
    private Timer timer;

    @FXML
    private void initialize() {
        this.permissionChoiceBox.getItems().add("Permission Type");
        this.permissionChoiceBox.getItems().add("Reader");
        this.permissionChoiceBox.getItems().add("Writer");
        this.permissionChoiceBox.getItems().add("None");
        this.permissionChoiceBox.getSelectionModel().selectFirst();

        permissionsColumn.setCellValueFactory(new PropertyValueFactory<>("permissions"));
        sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));

        this.sendPermissonRequestButton.disableProperty().bind(Bindings.or(
                this.sheetNameTextField.textProperty().isEmpty(),
                this.permissionChoiceBox.getSelectionModel().selectedItemProperty().isEqualTo("Permission Type")));

        this.acceptPermissionButton.disableProperty().bind(this.requestTableView.getSelectionModel().selectedItemProperty().isNull());
        this.declinePremissionButton.disableProperty().bind(this.requestTableView.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    void onAcceptPermissionClicked(ActionEvent event) {
        PermissionRequestTableEntry selectedRequest = this.requestTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            this.homeViewController.replyToPermissionRequest(
                    this.requestTableView.getSelectionModel().getSelectedItem(),true);
        }
    }

    @FXML
    void onDeclinePermissionClicked(ActionEvent event) {
        PermissionRequestTableEntry selectedRequest = this.requestTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            this.homeViewController.replyToPermissionRequest(
                    this.requestTableView.getSelectionModel().getSelectedItem(),false);
        }
    }

    @FXML
    void onSendPermissionClicked(ActionEvent event) {
        this.homeViewController.sendNewPermissionRequest(this.sheetNameTextField.getText(),
                this.permissionChoiceBox.getSelectionModel().getSelectedItem());

    }

    @FXML
    void onViewSheetClicked(ActionEvent event) {

    }

    public void setHomeViewController(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }

    public void startPermissionTableRefresher() {
        tableRefresher = new ReceivedPermissionTableRefresher(this::updateRequestTable);
        timer = new Timer();
        timer.schedule(tableRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void addNewPermissionRequest(ReceivedRequestForTableDTO receivedRequestForTableDTO) {
            requestTableView.getItems().add(new PermissionRequestTableEntry(
                    receivedRequestForTableDTO.getRequesterUserName(),
                    receivedRequestForTableDTO.getSheetName(),
                    receivedRequestForTableDTO.getRequestedPermission().getType(),
                    receivedRequestForTableDTO.getRequestNumber()));
    }

    private void updateRequestTable(List<ReceivedRequestForTableDTO> requests) {
        Platform.runLater(() -> {
            PermissionRequestTableEntry selectedRequest = this.requestTableView.getSelectionModel().getSelectedItem();


            ObservableList<PermissionRequestTableEntry> items = requestTableView.getItems();
            items.clear();
            requests.forEach(this::addNewPermissionRequest);

            if(selectedRequest != null) {
                for (PermissionRequestTableEntry requestEntry : this.requestTableView.getItems()) {
                    if(Objects.equals(requestEntry, selectedRequest)){
                        this.requestTableView.getSelectionModel().select(requestEntry);
                        break;
                    }
                }
            }

        });
    }



    @Override
    public void close() {
        requestTableView.getItems().clear();
        if (tableRefresher != null && timer != null) {
            tableRefresher.cancel();
            timer.cancel();
        }
    }

    public void clearNewPermissionRequest() {
        this.sheetNameTextField.textProperty().set("");
        this.permissionChoiceBox.getSelectionModel().selectFirst();
        // this.errorLabelClear
    }

    public void updateErrorLabel(ResponseBody responseBody) {
    }
}
