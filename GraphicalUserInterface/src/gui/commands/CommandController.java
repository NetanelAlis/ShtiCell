package gui.commands;

import gui.main.MainAppViewController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import logic.function.returnable.Returnable;
import java.util.ArrayList;
import java.util.List;

import static gui.main.MainAppViewController.effectiveValueFormatter;

public class CommandController {

    @FXML
    private TextField bottomLeftBoundaryTextField;

    @FXML
    private Button filterButton;

    @FXML
    private ChoiceBox<String> filterColumnChoiceBox;

    @FXML
    private MenuButton filterElementMenuButton;

    @FXML
    private Label filterErrorLabel;

    @FXML
    private Button sortButton;

    @FXML
    private TextField sortColumnTextField;

    @FXML
    private Label sortErrorLabel;

    @FXML
    private TextField topRightBoundaryTextField;

    private List<ChoiceBox<String>> additionalColumnsToSortBy;

    private MainAppViewController mainAppViewController;
    private BooleanProperty isFileLoadedProperty;
    private StringProperty sortErrorProperty;

    public CommandController() {
        this.isFileLoadedProperty = new SimpleBooleanProperty(false);
        this.sortErrorProperty = new SimpleStringProperty("");
        this.additionalColumnsToSortBy = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        this.bottomLeftBoundaryTextField.disableProperty().bind(this.isFileLoadedProperty);
        this.topRightBoundaryTextField.disableProperty().bind(this.isFileLoadedProperty);
        this.sortErrorLabel.textProperty().bind(this.sortErrorProperty);

        this.filterColumnChoiceBox.getItems().add("Select Column");
        this.filterColumnChoiceBox.getSelectionModel().selectFirst();

        this.sortColumnTextField.disableProperty().bind(
                Bindings.or(this.isFileLoadedProperty.not(),
                        Bindings.or(this.bottomLeftBoundaryTextField.textProperty().isEmpty(),
                                this.topRightBoundaryTextField.textProperty().isEmpty())));

        this.filterColumnChoiceBox.disableProperty().bind(
                Bindings.or(this.isFileLoadedProperty,
                        Bindings.or(this.bottomLeftBoundaryTextField.textProperty().isEmpty(),
                                this.topRightBoundaryTextField.textProperty().isEmpty())));


        this.filterElementMenuButton.disableProperty().bind(
                Bindings.or(this.filterColumnChoiceBox.disableProperty(),
                        this.filterColumnChoiceBox.getSelectionModel()
                                .selectedItemProperty().isEqualTo("Select Item")));


        this.sortButton.disableProperty().bind(this.sortColumnTextField.textProperty().isEmpty());

        this.filterButton.disableProperty().bind(this.filterElementMenuButton.disableProperty());

        this.filterColumnChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.equals("Select Item")) {
                        List<Returnable> availableFilters =
                                this.mainAppViewController.getItemsToFilterBy(this.filterColumnChoiceBox.getValue(),
                                        getCurrentRangeAsString());
                        this.filterElementMenuButton.getItems().clear();
                        availableFilters.forEach((item) -> {
                            CheckMenuItem checkMenuItem = new CheckMenuItem(effectiveValueFormatter(item));
                            this.filterElementMenuButton.getItems().add(checkMenuItem);
                        });
                    }
                });
    }

    private String getCurrentRangeAsString() {
        return this.topRightBoundaryTextField.getText() + ".." + this.bottomLeftBoundaryTextField.getText();
    }



    @FXML
    void onRangeTextFieldClicked(MouseEvent event) {
        this.filterColumnChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    void onFilterButtonClicked(ActionEvent event) {
        if (this.filterElementMenuButton.getItems() != null
                && this.filterColumnChoiceBox.getSelectionModel().getSelectedItem() != null
                && !this.filterColumnChoiceBox.getSelectionModel().getSelectedItem().equals("Select Column")) {

            String rangeToFilter =
                    this.topRightBoundaryTextField.getText() + ".." + this.bottomLeftBoundaryTextField.getText();

            List<Integer> itemsToFilter = new ArrayList<>();
            for (int i = 0; i < this.filterElementMenuButton.getItems().size(); i++) {
                CheckMenuItem checkMenuItem = (CheckMenuItem) this.filterElementMenuButton.getItems().get(i);
                if (checkMenuItem.isSelected()) {
                    itemsToFilter.add(i);
                }
            }

            this.mainAppViewController.filterSheet(rangeToFilter, itemsToFilter, this.filterColumnChoiceBox.getValue());
        }
    }

    @FXML
    void onColumnToFilterByClicked(MouseEvent event) {
        event.consume();
        List<String> columnsToFilterBy = this.mainAppViewController.getColumnsToFilterBy(getCurrentRangeAsString());
        this.filterColumnChoiceBox.getItems().clear();
        this.filterColumnChoiceBox.getItems().add("Select Column");
        this.filterColumnChoiceBox.getItems().addAll(columnsToFilterBy);
        this.filterColumnChoiceBox.show();
    }

    @FXML
    void onSortClicked(ActionEvent event) {
        List<String> columnsToSortBy = this.parsUserColumnsToFilterBy();

        if (this.mainAppViewController.sortRange(this.topRightBoundaryTextField.getText(), this.bottomLeftBoundaryTextField.getText(),
                columnsToSortBy)) {
            this.setErrorMessageToSortButton("");
            this.resetRange();
        }

    }

    private List<String> parsUserColumnsToFilterBy() {
        {
            List<String> columnsToFilterBY = new ArrayList<>();

            String[] userColumnsToFilterBY = this.sortColumnTextField.getText().split(";");
            for (int i = 0; i < userColumnsToFilterBY.length; i++) {
                columnsToFilterBY.add(userColumnsToFilterBY[i].trim().toUpperCase());
            }
            return columnsToFilterBY;
        }

    }

    public void setMainController(MainAppViewController mainViewController) {
        this.mainAppViewController = mainViewController;
    }

    public void resetController() {
        this.topRightBoundaryTextField.textProperty().set("");
        this.bottomLeftBoundaryTextField.textProperty().set("");
        this.sortColumnTextField.textProperty().set("");
        this.sortErrorProperty.set("");
    }

    public void updateSortErrorLabel(String message) {
        this.sortErrorProperty.set(message);
    }

       public void setErrorMessageToSortButton(String message) {
     this.sortErrorLabel.setText(message);
   }

        private void resetRange() {
        this.topRightBoundaryTextField.setText("");
        this.bottomLeftBoundaryTextField.setText("");
    }

        public void bindIsFileLoaded(BooleanProperty fileNotLoaded) {
        this.isFileLoadedProperty.bind(fileNotLoaded);
    }

}
