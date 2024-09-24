package gui.ranges;

import dto.RangeDTO;
import dto.RangesDTO;
import gui.main.MainAppViewController;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.List;

public class RangesController {
    @FXML
    private TextField buttomLeftBounderyTextField;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField rangeNameTextField;
    @FXML
    private Button saveButton;
    @FXML
    private TextField topRightBounderyTextField;
    @FXML
    private ListView<RangeDTO> rangeListView;
    @FXML
    private Label errorLabel;
    @FXML
    private Label errorLabelFromDelete;


    private MainAppViewController mainAppViewController;
    private BooleanProperty fileNotLoaded;
    private BooleanProperty rangeAreEmptyProperty;
    private BooleanProperty listViewItemNotSelectedProperty;
    private RangeModel rangeModel;

    public RangesController() {
        this.fileNotLoaded = new SimpleBooleanProperty(true);
        this.rangeAreEmptyProperty = new SimpleBooleanProperty(true);
        this.listViewItemNotSelectedProperty = new SimpleBooleanProperty(true);

    }

    @FXML
    public void initialize() {
        this.deleteButton.disableProperty().set(true);
        saveButton.disableProperty().bind(
                Bindings.or(
                        Bindings.or(
                                this.rangeNameTextField.textProperty().isEmpty(),
                                this.fileNotLoaded
                        ),
                        Bindings.or(
                                this.topRightBounderyTextField.textProperty().isEmpty(),
                                this.buttomLeftBounderyTextField.textProperty().isEmpty()
                        )
                )

        );


        this.rangeListView.setOnMouseClicked((event) -> {
            if (this.rangeListView.getSelectionModel().getSelectedItem() != null) {
                RangeDTO newValue = this.rangeListView.getSelectionModel().getSelectedItem();
                this.rangeModel.setListViewItemNotSelectedProperty(false);
                this.mainAppViewController.showSelectedRange(newValue, this.rangeModel.getSelectedRange());
                this.rangeModel.setSelectedRange(this.rangeListView.getSelectionModel().getSelectedItem());
            }
        });

        this.rangeListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                PauseTransition pause = new PauseTransition(Duration.millis(100));
                // Delay the action that disables the button
                pause.setOnFinished(event -> {
                    // Check again if the ListView is still not focused
                    if (!this.rangeListView.isFocused()) {
                        this.mainAppViewController.showSelectedRange(null, this.rangeModel.getSelectedRange());
                        this.rangeModel.setListViewItemNotSelectedProperty(true);
                        this.rangeModel.setSelectedRange(null);
                        // Uncomment if necessary to clear selected range
                    }
                });
                pause.play();
            }
        });

    }

    @FXML
    void onDeleteRangePressed(ActionEvent event) {
        this.mainAppViewController.deleteRange(this.rangeModel.getSelectedRange());

    }

    @FXML
    void onSaveRangePressed(ActionEvent event) {
        this.mainAppViewController.addRange(this.rangeNameTextField.getText(), topRightBounderyTextField.getText(), buttomLeftBounderyTextField.getText());

    }

    public void setMainAppController(MainAppViewController mainAppController){
        this.mainAppViewController = mainAppController;
    }

    public void bindIsFileLoaded(BooleanProperty fileNotLoaded) {
        this.fileNotLoaded.bind(fileNotLoaded);
    }

    public void showRange(RangeDTO rangeDTO) {
        this.rangeModel.addRange(rangeDTO);
        this.setErrorMessageToSaveButton("");
        this.resetController();
    }

    public void setErrorMessageToSaveButton(String errorMessage) {
        this.errorLabel.setText(errorMessage);
    }

    public void setErrorMessageToDeleteButton(String errorMessage) {
        this.errorLabelFromDelete.setText(errorMessage);
    }

    public void setRangesAreEmptyProperty() {
           this.rangeModel.setRangesAreEmptyProperty(this.rangeModel.getSelectedRangesSize() == 0);
    }

    public void unShowRange(RangeDTO rangeDTO){
        this.rangeModel.removeRange(this.rangeModel.getSelectedRange());
        this.rangeModel.setListViewItemNotSelectedProperty(true);
        this.setRangesAreEmptyProperty();
        this.setErrorMessageToDeleteButton("");
        this.resetController();
    }

    public void resetController(){
        this.rangeNameTextField.setText("");
        this.topRightBounderyTextField.setText("");
        this.buttomLeftBounderyTextField.setText("");
    }

    public void initializeRangesModel(){
        this.rangeModel = new RangeModel();
        this.rangeModel.bindAll(this.rangeListView, this.rangeAreEmptyProperty, this.listViewItemNotSelectedProperty);
        deleteButton.disableProperty().bind(Bindings.or(this.rangeModel.getListViewItemNotSelectedProperty(), this.rangeModel.getRangesAreEmptyProperty()));

    }

}
