package client.gui.editor.ranges;

import dto.RangeDTO;
import dto.RangesDTO;
import client.gui.editor.main.view.MainEditorController;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class RangesController {
    
    @FXML private TextField bottomLeftBoundaryTextField;
    @FXML private TextField topRightBoundaryTextField;
    @FXML private TextField rangeNameTextField;
    @FXML private Button saveRangeButton;
    @FXML private Button deleteRangeButton;
    @FXML private Label deleteRangeErrorLabel;
    @FXML private Label newRangeErrorLabel;
    @FXML private ListView<RangeDTO> rangesListView;

    private MainEditorController mainEditorController;
    private RangesModel rangesModel;
    
    private BooleanProperty isFileLoadedProperty;
    private BooleanProperty isSelectedRangeProperty;
    private StringProperty saveRangeErrorProperty;
    private StringProperty deleteRangeErrorProperty;
    
    public RangesController() {
        this.isFileLoadedProperty = new SimpleBooleanProperty(true);
        this.isSelectedRangeProperty = new SimpleBooleanProperty(false);
        this.saveRangeErrorProperty = new SimpleStringProperty("");
        this.deleteRangeErrorProperty = new SimpleStringProperty("");
    }
    
    @FXML
    private void initialize() {
        this.newRangeErrorLabel.textProperty().bind(this.saveRangeErrorProperty);
        this.deleteRangeErrorLabel.textProperty().bind(this.deleteRangeErrorProperty);
        this.deleteRangeButton.disableProperty().bind(this.rangesListView.getSelectionModel().selectedItemProperty().isNull());
        
        this.saveRangeButton.disableProperty().bind(
                Bindings.or(
                        this.isFileLoadedProperty,
                        Bindings.or(
                                this.rangeNameTextField.textProperty().isEmpty(),
                                Bindings.or(
                                        this.topRightBoundaryTextField.textProperty().isEmpty(),
                                        this.bottomLeftBoundaryTextField.textProperty().isEmpty()
                )
        )));
        
        this.rangesListView.setOnMouseClicked(event -> {
            RangeDTO selectedRange = this.rangesListView.getFocusModel().focusedItemProperty().get();
            this.mainEditorController.toggleSelectedRange(selectedRange, this.rangesModel.getSelectedRange());
            this.rangesModel.setSelectedRange(selectedRange);
            this.isSelectedRangeProperty.set(true);
            this.deleteRangeErrorProperty.set("");
            
        });
        this.rangesListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                PauseTransition pause = new PauseTransition(Duration.millis(10));
                // Delay the action that disables the button
                pause.setOnFinished(event -> {
                    // Check again if the ListView is still not focused
                    if (!this.rangesListView.isFocused()) {
                        this.mainEditorController.toggleSelectedRange(null, this.rangesModel.getSelectedRange());
                        this.isSelectedRangeProperty.set(false);
                        this.rangesModel.setSelectedRange(null);
                    }
                });
                pause.play();
            }
        });
    
    }
    
    public void bindFileNotLoaded(BooleanProperty isFileLoaded) {
        this.isFileLoadedProperty.bind(isFileLoaded);
    }
    
    @FXML
    void onDeleteRangeClicked(ActionEvent event) {
        RangeDTO rangeToDelete = this.rangesListView.getFocusModel().getFocusedItem();
        if (rangeToDelete != null && this.mainEditorController.deleteRange(rangeToDelete)) {
            this.rangesModel.deleteRange(rangeToDelete);
            this.deleteRangeErrorProperty.set("");
        }
    }
    
    @FXML
    void onSaveRangeClicked(ActionEvent event) {
        RangeDTO newRange = this.mainEditorController.addNewRange(
                this.rangeNameTextField.getText(),
                this.topRightBoundaryTextField.getText(),
                this.bottomLeftBoundaryTextField.getText());
        
        if (newRange != null) {
            this.rangesModel.rangesProperty().add(newRange);
            this.clearNewRangeFields();
        }
    }
    
    public void setMainController(MainEditorController mainEditorController) {
        this.mainEditorController = mainEditorController;
    }
    
    public void initializeRangesModel(RangesDTO rangesDTO) {
        this.rangesModel = new RangesModelImpl(this.rangesListView, rangesDTO);
        
    }
    
    public void updateSaveErrorLabel(String message) {
        this.saveRangeErrorProperty.set(message);
    }
    
    public void updateDeleteErrorLabel(String message) {
        this.deleteRangeErrorProperty.set(message);
    }
    
    public void resetController() {
        this.clearNewRangeFields();
        this.deleteRangeErrorProperty.set("");
    }
    
    public void clearNewRangeFields() {
        this.rangeNameTextField.textProperty().set("");
        this.topRightBoundaryTextField.textProperty().set("");
        this.bottomLeftBoundaryTextField.textProperty().set("");
        this.saveRangeErrorProperty.set("");
    }
}
