package gui.customization;

import dto.CellDTO;
import gui.main.MainAppViewController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class CustomizationController {

    @FXML
    private ColorPicker backgroundColorPicker;

    @FXML
    private Label cellIDLabel;

    @FXML
    private Button centerAlignmentButton;

    @FXML
    private Label collLetterLabel;

    @FXML
    private Spinner<Integer> columnWidthSpinner;

    @FXML
    private Button leftAlignmentButton;

    @FXML
    private Button rightAlignmentButton;

    @FXML
    private Spinner<Integer> rowHeightSpinner;

    @FXML
    private Label rowNumberLabel;

    @FXML
    private ColorPicker textColorColorPicker;

    @FXML
    private Label colLetterAlignmentLabel;

    @FXML
    private Button resetStyleButton;

    private MainAppViewController mainAppViewController;
    private BooleanProperty fileNotLoaded;
    SpinnerValueFactory<Integer> colValueFactory;
    SpinnerValueFactory<Integer> rowValueFactory;
    private StringProperty cellIDProperty;
    private StringProperty selectedCollProperty;
    private StringProperty selectedRowProperty;

   public CustomizationController() {
        this.fileNotLoaded = new SimpleBooleanProperty(true);
        this.colValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE,50,1);
        this.rowValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE,50,1);
        this.cellIDProperty = new SimpleStringProperty("");
        this.selectedCollProperty = new SimpleStringProperty("");
        this.selectedRowProperty = new SimpleStringProperty("");
    }

    @FXML
    public void initialize() {
       this.cellIDLabel.textProperty().bind(this.cellIDProperty);
       this.selectedCollProperty = new SimpleStringProperty("");
       this.selectedRowProperty = new SimpleStringProperty("");

       this.collLetterLabel.textProperty().bind(Bindings.format("Column %s", this.selectedCollProperty));
       this.colLetterAlignmentLabel.textProperty().bind(Bindings.format("Column %s", this.selectedCollProperty));

        this.rowNumberLabel.textProperty().bind(Bindings.format("Row %s", this.selectedRowProperty));
        this.columnWidthSpinner.disableProperty().bind(Bindings.or(fileNotLoaded, this.collLetterLabel.textProperty().isEqualTo("Column ")));
        this.rowHeightSpinner.disableProperty().bind(Bindings.or(fileNotLoaded, this.rowNumberLabel.textProperty().isEqualTo("Row ")));
        this.leftAlignmentButton.disableProperty().bind(Bindings.or(fileNotLoaded, this.selectedCollProperty.isEmpty()));
        this.centerAlignmentButton.disableProperty().bind(Bindings.or(fileNotLoaded, this.selectedCollProperty.isEmpty()));
        this.rightAlignmentButton.disableProperty().bind(Bindings.or(fileNotLoaded, this.selectedCollProperty.isEmpty()));
        this.backgroundColorPicker.disableProperty().bind(Bindings.or(fileNotLoaded, this.cellIDProperty.isEmpty()));
        this.textColorColorPicker.disableProperty().bind(Bindings.or(fileNotLoaded, this.cellIDProperty.isEmpty()));
        this.resetStyleButton.disableProperty().bind(Bindings.or(fileNotLoaded, this.cellIDProperty.isEmpty()));


        columnWidthSpinner.setValueFactory(colValueFactory);
        columnWidthSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!this.selectedCollProperty.get().isEmpty()){
            this.mainAppViewController.updateColWidth(newValue,this.collLetterLabel.textProperty().getValue().substring("Column ".length()).charAt(0) - 'A' + 1);
            }
        });

        rowHeightSpinner.setValueFactory(rowValueFactory);
        rowHeightSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!this.selectedRowProperty.get().isEmpty()){
            this.mainAppViewController.updateRowHeight(newValue, Integer.parseInt(this.rowNumberLabel.getText().substring("Row ".length())));
            }
        });
    }

    @FXML
    void onCellStyleChanged(ActionEvent event) {
        this.mainAppViewController.setCellDesign(this.backgroundColorPicker.getValue(), this.cellIDLabel.getText(), this.textColorColorPicker.getValue());
    }


    @FXML
    void onLeftAlignmentClicked(ActionEvent event) {
       this.mainAppViewController.setColTextAlignment(this.selectedCollProperty.get(),"left");
    }

    @FXML
    void onRightAlignmentClicked(ActionEvent event) {
        this.mainAppViewController.setColTextAlignment(this.selectedCollProperty.get(),"right");
    }

    @FXML
    void onCenterAlignmentClicked(ActionEvent event) {
        this.mainAppViewController.setColTextAlignment(this.selectedCollProperty.get(),"center");
    }

    @FXML
    void onResetClicked(ActionEvent event) {
        this.mainAppViewController.setCellDesign(Color.WHITE ,this.cellIDLabel.getText(), Color.BLACK);
        this.backgroundColorPicker.setValue(Color.WHITE);
        this.textColorColorPicker.setValue(Color.BLACK);
    }

    public void setMainAppController(MainAppViewController mainAppController){
        this.mainAppViewController = mainAppController;
    }

    public void bindIsFileLoaded(BooleanProperty fileNotLoaded) {
        this.fileNotLoaded.bind(fileNotLoaded);
    }

    public void showRow(String rowName, int rowHeight) {
        this.selectedRowProperty.set(rowName);
        this.rowValueFactory.setValue(rowHeight);
    }

    public void showColumn(String columnIndex, int columnWidth){
        this.selectedCollProperty.set(columnIndex);
        this.colValueFactory.setValue(columnWidth);
    }

    public void setCellIDProperty(CellDTO cellDTO) {
       this.cellIDProperty.set(cellDTO.getCellId());
    }

    public void resetController(){
        this.selectedCollProperty.set("");
        this.selectedRowProperty.set("");
        this.selectedCollProperty.set("");
        this.backgroundColorPicker.setValue(Color.WHITE);
        this.textColorColorPicker.setValue(Color.BLACK);
        this.colValueFactory.setValue(0);
        this.rowValueFactory.setValue(0);
    }

}
