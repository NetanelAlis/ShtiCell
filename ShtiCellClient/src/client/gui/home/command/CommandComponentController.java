package client.gui.home.command;

import client.gui.home.main.view.HomeViewController;
import client.task.PermissionTableRefresher;
import dto.ReceivedRequestDTO;
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
    }

    @FXML
    void onAcceptPermissionClicked(ActionEvent event) {

    }

    @FXML
    void onDeclinePermissionClicked(ActionEvent event) {

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
        tableRefresher = new PermissionTableRefresher(this::updateRequestTable);
        timer = new Timer();
        timer.schedule(tableRefresher, 10000, REFRESH_RATE);
    }

    public void addNewPermissionRequest(ReceivedRequestDTO receivedRequestDTO) {
            requestTableView.getItems().add(new PermissionRequestTableEntry(
                    receivedRequestDTO.getRequesterUserName(),
                    receivedRequestDTO.getSheetName(),
                    receivedRequestDTO.getRequestedPermission().getType()));
    }

    private void updateRequestTable(List<ReceivedRequestDTO> requests) {
        Platform.runLater(() -> {
            ObservableList<PermissionRequestTableEntry> items = requestTableView.getItems();
            items.clear();
            requests.forEach(this::addNewPermissionRequest);
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
