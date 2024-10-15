package client.gui.home.sheet.table;

import client.gui.home.main.view.HomeViewController;
import client.task.SheetTableRefresher;
import dto.sheet.SheetMetaDataDTO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.Closeable;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static client.util.Constants.REFRESH_RATE;

public class SheetTableController implements Closeable {

    @FXML
    private TableColumn<SheetTableEntry, String> permissionsColumn;

    @FXML
    private TableColumn<SheetTableEntry, String> sheetNameColumn;

    @FXML
    private TableColumn<SheetTableEntry, String> sheetSizeColumn;

    @FXML
    private TableColumn<SheetTableEntry, String>userNameColumn;

    @FXML
    private TableView<SheetTableEntry> table;

    private HomeViewController homeViewController;
    private TimerTask tableRefresher;
    private Timer timer;


    @FXML
    public void initialize() {
        permissionsColumn.setCellValueFactory(new PropertyValueFactory<>("permissions"));
        sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        sheetSizeColumn.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                this.homeViewController.updateCurrentSelectedSheet(newValue);
            }
        });
    }

    public void setHomeViewController(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }

    public void addNewSheet(SheetMetaDataDTO sheetMetaDataDTO) {
        table.getItems().add(new SheetTableEntry(sheetMetaDataDTO.getUserName(), sheetMetaDataDTO.getSheetName(),sheetMetaDataDTO.numberOfRows() + "x" + sheetMetaDataDTO.getNumberOfCols(), sheetMetaDataDTO.getPermission().getType()));
    }

    public void startSheetTableRefresher() {
        tableRefresher = new SheetTableRefresher(this::updateSheetTable);
        timer = new Timer();
        timer.schedule(tableRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateSheetTable(List<SheetMetaDataDTO> sheets) {
        Platform.runLater(() -> {
            SheetTableEntry selectedSheet = this.table.getSelectionModel().getSelectedItem();
            String selectedSheetName = (selectedSheet != null) ? selectedSheet.getSheetName() : null;

            ObservableList<SheetTableEntry> items = table.getItems();
            items.clear();
            sheets.forEach(this::addNewSheet);

            if(selectedSheetName != null) {
                for (SheetTableEntry sheetEntry : this.table.getItems()) {
                    if(Objects.equals(sheetEntry.getSheetName(), selectedSheetName)){
                        this.table.getSelectionModel().select(sheetEntry);
                        break;
                    }
                }
            }

        });
    }

    @Override
    public void close() {
        table.getItems().clear();
        if (tableRefresher != null && timer != null) {
            tableRefresher.cancel();
            timer.cancel();
        }
    }
}
