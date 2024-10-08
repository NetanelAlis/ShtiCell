package client.gui.home.sheet.table;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SheetTableController {

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


    @FXML
    public void initialize() {
        permissionsColumn.setCellValueFactory(new PropertyValueFactory<>("permissions"));
        sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        sheetSizeColumn.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
    }

    public void updateTable(String sheetName, int numberOfRows, int numberOfCols) {
        table.getItems().add(new SheetTableEntry("UserName", sheetName, "" + numberOfRows + "x" + numberOfRows, "Owner"));
    }
}
