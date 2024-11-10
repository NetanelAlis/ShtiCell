package client.gui.home.permission.table;

import client.gui.home.main.view.HomeViewController;
import client.task.PermissionTableRefresher;
import dto.permission.PermissionDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Timer;

import static client.gui.util.Constants.REFRESH_RATE;

public class PermissionsTableController implements Closeable {
    
    @FXML private TableView<PermissionTableEntry> permissionsTable;
    @FXML private TableColumn<String, PermissionTableEntry> usernameColumn;
    @FXML private TableColumn<String, PermissionTableEntry> permissionTypeColumn;
    @FXML private TableColumn<String, PermissionTableEntry> requestStatusColumn;
    
    private ObservableList<PermissionTableEntry> permissionsRequests;
    private HomeViewController homeViewController;
    private PermissionTableRefresher tableRefresher;
    private Timer timer;
    
    public PermissionsTableController() {
        this.permissionsRequests = FXCollections.observableArrayList();
    }
    
    @FXML
    private void initialize() {
        this.initializeTableView();
    }
    
    private void initializeTableView() {
        this.usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        this.permissionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("permissionType"));
        this.requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        
        this.permissionsTable.setItems(this.permissionsRequests);
        this.permissionsTable.setSelectionModel(null);
    }
    
    public void setMainController(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }
    
    public void addPermissionEntry(PermissionDTO permissionToAdd) {
        this.permissionsRequests.add(new PermissionTableEntry(
                permissionToAdd.getUsername(),
                permissionToAdd.getPermissionType(),
                permissionToAdd.getRequestStatus()));
    }
    
    public void startTableRefresher() {
        this.tableRefresher = new PermissionTableRefresher(this::updatePermissionsTable);
        this.timer = new Timer();
        this.timer.schedule(this.tableRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    
    private void updatePermissionsTable(List<PermissionDTO> permissions) {
        Platform.runLater(() -> {
            this.permissionsRequests.clear();
            permissions.forEach(this::addPermissionEntry);
        });
    }
    
    @Override
    public void close() throws IOException {
        this.permissionsTable.getItems().clear();
        if (this.tableRefresher != null && timer != null) {
            this.tableRefresher.cancel();
            this.timer.cancel();
        }
    }
    
    public void setSelectedSheet(String sheetName) {
        this.tableRefresher.setSheetName(sheetName);
    }
}
