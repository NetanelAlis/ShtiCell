package client.gui.home.permission.table;

import client.gui.home.main.view.HomeViewController;
import client.gui.home.sheet.table.SheetTableEntry;
import client.task.RequestedPermissionTableRefresher;
import dto.permission.RequestedRequestForTableDTO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.Closeable;
import java.util.List;
import java.util.Timer;
import static client.util.Constants.REFRESH_RATE;

public class PermissionTableController implements Closeable {

    @FXML
    private TableColumn<PermissionTableEntry, String> RequestTypeColumn;

    @FXML
    private TableColumn<PermissionTableEntry, String> userNameColumn;

    @FXML
    private TableColumn<PermissionTableEntry, String> RequestTypeStatus;

    @FXML
    private TableView<PermissionTableEntry> permissionTableView;

    private HomeViewController homeViewController;
    private RequestedPermissionTableRefresher requestedPermissionTableRefresher;
    private Timer timer;

    @FXML
    public void initialize() {
        RequestTypeStatus.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        RequestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        this.permissionTableView.setSelectionModel(null);
    }

    public void setHomeViewController(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }

    public void startPermissionTableRefresher() {
        requestedPermissionTableRefresher = new RequestedPermissionTableRefresher(this::updatePermissionTable);
        timer = new Timer();
        timer.schedule(requestedPermissionTableRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updatePermissionTable(List<RequestedRequestForTableDTO> requests) {
        Platform.runLater(() -> {
            ObservableList<PermissionTableEntry> items = permissionTableView.getItems();
            items.clear();
            requests.forEach(this::addNewRequestForPermissionTable);
        });
    }

    private void addNewRequestForPermissionTable(RequestedRequestForTableDTO requestedRequestForTableDTO) {
        permissionTableView.getItems().add(new PermissionTableEntry(requestedRequestForTableDTO.getRequesterUserName(), requestedRequestForTableDTO.getRequestedPermission(), requestedRequestForTableDTO.getRequestPermissionStatus()));
    }

    @Override
    public void close() {
        permissionTableView.getItems().clear();
        if (requestedPermissionTableRefresher != null && timer != null) {
            requestedPermissionTableRefresher.cancel();
            timer.cancel();
        }
    }

    public void updateSheetNameInRefresher(SheetTableEntry newSheetEntry) {
        this.requestedPermissionTableRefresher.setSheetName(newSheetEntry.getSheetName());
        this.requestedPermissionTableRefresher.run();
    }
}
