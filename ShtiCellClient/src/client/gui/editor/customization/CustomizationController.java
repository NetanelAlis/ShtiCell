package client.gui.editor.customization;

import dto.cell.CellDTO;
import client.gui.editor.main.view.MainEditorController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class CustomizationController {
    
    @FXML private ColorPicker backgroundColorPicker;
    @FXML private ColorPicker textColorPicker;
    @FXML private Spinner<Integer> columnWidthSpinner;
    @FXML private Spinner<Integer> rowHeightSpinner;
    @FXML private Button centerAlignmentButton;
    @FXML private Button leftAlignmentButton;
    @FXML private Button rightAlignmentButton;
    @FXML private Button resetStyleButton;
    @FXML private Label columnTextAlignmentLabel;
    @FXML private Label columnIndexLabel;
    @FXML private Label rowIndexLabel;
    @FXML private Label selectedCellLabel;

    private MainEditorController mainEditorController;
    private IntegerProperty columnWidthProperty;
    private IntegerProperty rowHeightProperty;
    private StringProperty columnNameProperty;
    private final BooleanProperty isInReaderModeProperty;


    public CustomizationController() {
        this.isInReaderModeProperty = new SimpleBooleanProperty(true);
        this.columnWidthProperty = new SimpleIntegerProperty();
        this.rowHeightProperty = new SimpleIntegerProperty();
        this.columnNameProperty = new SimpleStringProperty("");
    }
    
    @FXML
    private void initialize() {
        this.rowIndexLabel.setText("Row ");
        this.selectedCellLabel.setText("");
        this.columnIndexLabel.textProperty().bind(Bindings.format("Column %s", columnNameProperty));
        this.columnTextAlignmentLabel.textProperty().bind(Bindings.format("Column %s", columnNameProperty));
        
        
        this.columnWidthSpinner.disableProperty().bind(this.columnIndexLabel.textProperty().isEqualTo("Column "));

        
        this.rowHeightSpinner.disableProperty().bind(this.rowIndexLabel.textProperty().isEqualTo("Row "));

        this.leftAlignmentButton.disableProperty().bind(this.columnNameProperty.isEmpty());

        this.centerAlignmentButton.disableProperty().bind(this.columnNameProperty.isEmpty());

        this.rightAlignmentButton.disableProperty().bind(this.columnNameProperty.isEmpty());

        this.backgroundColorPicker.disableProperty().bind(
                Bindings.or(this.selectedCellLabel.textProperty().isEmpty(),
                            this.isInReaderModeProperty));
        
        this.textColorPicker.disableProperty().bind(
                Bindings.or(this.selectedCellLabel.textProperty().isEmpty(),
                            this.isInReaderModeProperty));
        
        this.resetStyleButton.disableProperty().bind(
                Bindings.or(this.selectedCellLabel.textProperty().isEmpty(),
                            this.isInReaderModeProperty));
        
        
        SpinnerValueFactory<Integer> columnValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0,
                        Integer.MAX_VALUE,
                        this.columnWidthProperty.getValue(),
                        1);
        
        SpinnerValueFactory<Integer> rowValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0,
                        Integer.MAX_VALUE,
                        this.rowHeightProperty.getValue(),
                        1);
        
        this.columnWidthSpinner.setValueFactory(columnValueFactory);
        this.columnWidthSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!this.columnNameProperty.get().isEmpty()) {
                int columnToUpdate = this.getColumnAsInt();
                this.mainEditorController.updateColumnWidth(newValue, columnToUpdate);
            }
        });
        
        this.rowHeightSpinner.setValueFactory(rowValueFactory);
        this.rowHeightSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!this.rowIndexLabel.textProperty().get().equals("Row ")) {
                int rowToUpdate = this.getRowAsInt();
                this.mainEditorController.updateRowHeight(newValue, rowToUpdate);
            }
        });
    }
    
    @FXML
    void onCellStyleChanged(ActionEvent event) {
        this.mainEditorController.setCellStyle(
                this.selectedCellLabel.getText(),
                this.backgroundColorPicker.getValue(),
                this.textColorPicker.getValue());
    }
    
    @FXML
    void onCenterAlignmentClicked(ActionEvent event) {
        this.mainEditorController.setColumnTextAlignment(this.columnNameProperty.get(), "center");
    }
    
    @FXML
    void onLeftAlignmentClicked(ActionEvent event) {
        this.mainEditorController.setColumnTextAlignment(this.columnNameProperty.get(), "left");
        
    }
    
    @FXML
    void onRightAlignmentClicked(ActionEvent event) {
        this.mainEditorController.setColumnTextAlignment(this.columnNameProperty.get(), "right");
    }
    
    @FXML
    void onResetClicked(ActionEvent event) {
        this.mainEditorController.setCellStyle(
                this.selectedCellLabel.getText(),
                Color.WHITE,
                Color.BLACK);
        this.backgroundColorPicker.setValue(Color.WHITE);
        this.textColorPicker.setValue(Color.BLACK);
    }
    
    public void setMainController(MainEditorController mainEditorController) {
        this.mainEditorController = mainEditorController;
    }

    public void setSelectedColumn(String columnName, int currentPrefWidth) {
        this.columnNameProperty.set(columnName);
        this.columnWidthSpinner.getValueFactory().setValue(currentPrefWidth);
    }
    
    public void setSelectedRow(int rowIndex, int currentPrefHeight) {
        this.rowIndexLabel.setText("Row " + rowIndex);
        this.rowHeightSpinner.getValueFactory().setValue(currentPrefHeight);
    }
    
    public void setSelectedCell(String cellID, Color backgroundColor, Color textColor) {
        this.selectedCellLabel.setText(cellID);
        this.backgroundColorPicker.setValue(backgroundColor);
        this.textColorPicker.setValue(textColor);
    }
    
    public void deselectCell() {
        this.selectedCellLabel.setText("");
    }
    
    private int getColumnAsInt() {
        return this.columnIndexLabel.textProperty().get().substring(7).charAt(0) - 'A' + 1;
    }
    
    private int getRowAsInt(){
        return Integer.parseInt(this.rowIndexLabel.textProperty().get().substring(4));
    }
    
    public void resetController() {
        this.columnNameProperty.set("");
        this.rowIndexLabel.textProperty().set("Row ");
        this.selectedCellLabel.textProperty().set("");
        this.backgroundColorPicker.setValue(Color.WHITE);
        this.textColorPicker.setValue(Color.BLACK);
        this.columnWidthSpinner.getValueFactory().setValue(0);
        this.rowHeightSpinner.getValueFactory().setValue(0);
    }

    public void disableEditableActions(boolean disable) {
        this.isInReaderModeProperty.set(disable);
    }
}
