package client.gui.editor.command;

import client.gui.editor.main.view.MainEditorController;
import dto.returnable.EffectiveValueDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static client.gui.editor.main.view.MainEditorController.effectiveValueFormatter;
import static client.gui.util.Constants.DYNAMIC_ANALYSIS_FXML_RESOURCE_LOCATION;

public class CommandsController {
    
    @FXML private FlowPane dynamicAnalysisFlowPane;
    @FXML private TextField bottomLeftBoundaryTextField;
    @FXML private TextField topRightBoundaryTextField;
    @FXML private TextField columnsToSortByTextField;
    @FXML private ChoiceBox<String> filterColumnChoiceBox;
    @FXML private MenuButton filterElementMenuButton;
    @FXML private Button sortButton;
    @FXML private Button filterButton;
    @FXML private Label sortErrorLabel;
    @FXML private Label filterErrorLabel;
    @FXML private Label graphErrorLabel;
    @FXML private Button buildGraphButton;
    @FXML private ChoiceBox<String> graphTypeChoiceBox;
    
    private List<DynamicAnalysisController> dynamicAnalysisControllers;
    
    private MainEditorController mainEditorController;
    private StringProperty sortErrorProperty;
    private StringProperty filterErrorProperty;
    
    public CommandsController() {
        this.dynamicAnalysisControllers = new ArrayList<>();
        this.sortErrorProperty = new SimpleStringProperty("");
        this.filterErrorProperty = new SimpleStringProperty("");
    }
    
    @FXML private void initialize() {
        this.filterErrorLabel.textProperty().bind(this.filterErrorProperty);
        this.sortErrorLabel.textProperty().bind(this.sortErrorProperty);
        
        
        this.filterColumnChoiceBox.getItems().add("Select Column");
        this.filterColumnChoiceBox.getSelectionModel().selectFirst();

        this.graphTypeChoiceBox.getItems().add("Graph Type");
        this.graphTypeChoiceBox.getItems().add("Line Chart");
        this.graphTypeChoiceBox.getItems().add("Bar Chart");
        this.graphTypeChoiceBox.getItems().add("Pie Chart");
        this.graphTypeChoiceBox.getSelectionModel().selectFirst();

        this.columnsToSortByTextField.disableProperty().bind(
                Bindings.or(this.bottomLeftBoundaryTextField.textProperty().isEmpty(),
                        this.topRightBoundaryTextField.textProperty().isEmpty()));
        
        this.filterColumnChoiceBox.disableProperty().bind(
                Bindings.or(this.bottomLeftBoundaryTextField.textProperty().isEmpty(),
                        this.topRightBoundaryTextField.textProperty().isEmpty()));

        this.graphTypeChoiceBox.disableProperty().bind(
                Bindings.or(this.bottomLeftBoundaryTextField.textProperty().isEmpty(),
                        this.topRightBoundaryTextField.textProperty().isEmpty()));
        
        this.filterElementMenuButton.disableProperty().bind(
                Bindings.or(this.filterColumnChoiceBox.disableProperty(),
                        Bindings.or(this.filterColumnChoiceBox.getSelectionModel()
                                    .selectedItemProperty().isEqualTo("Select Column"),
                                    this.filterColumnChoiceBox.getSelectionModel().selectedItemProperty().isNull())));
        
        this.buildGraphButton.disableProperty().bind(
                Bindings.or(this.graphTypeChoiceBox.disableProperty(),
                        this.graphTypeChoiceBox.getSelectionModel()
                                .selectedItemProperty().isEqualTo("Graph Type")));
        
        this.sortButton.disableProperty().bind(this.columnsToSortByTextField.textProperty().isEmpty());
        
        this.filterButton.disableProperty().bind(this.filterElementMenuButton.disableProperty());
        
        this.addAnalyser();
        
        this.filterColumnChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.equals("Select Column")) {
                this.mainEditorController
                        .getUniqueItems(this.filterColumnChoiceBox.getValue(), getCurrentRangeAsString());
            }
        });
    }
    
    private void addAnalyser() {
        URL editorPageUrl = getClass().getResource(DYNAMIC_ANALYSIS_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(editorPageUrl);
            this.dynamicAnalysisFlowPane.getChildren().add(fxmlLoader.load());
            DynamicAnalysisController newController = fxmlLoader.getController();
            newController.setAnalyserID(this.dynamicAnalysisControllers.size());
            newController.setCommandsController(this);
            this.dynamicAnalysisControllers.add(newController);
            if (this.mainEditorController != null) {
                this.mainEditorController.addDynamicAnalysisController(this.dynamicAnalysisControllers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void updateFilterElementMenuButton(List<EffectiveValueDTO> availableFilters) {
        this.filterElementMenuButton.getItems().clear();
        availableFilters.forEach((item) -> {
            if (item != null) {
                CheckMenuItem checkMenuItem = new CheckMenuItem(effectiveValueFormatter(item));
                this.filterElementMenuButton.getItems().add(checkMenuItem);
            }
        });
    }
    
    private String getCurrentRangeAsString() {
        return this.topRightBoundaryTextField.getText() + ".." + this.bottomLeftBoundaryTextField.getText();
    }
    
    @FXML
    void onFinishAnalysisClicked(ActionEvent event) {
        this.dynamicAnalysisControllers.clear();
        this.dynamicAnalysisFlowPane.getChildren().clear();
        this.mainEditorController.exitDynamicAnalysisMode();
        this.addAnalyser();
    }
    
    @FXML
    void onAddAnalyserClicked(ActionEvent event) {
        this.addAnalyser();
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
            
            List<Integer> itemsToFilterIndices = this.getItemsToFilter();
            
            if (itemsToFilterIndices.isEmpty()){
                this.filterErrorProperty.set("Must select at least one item");
            } else {
                this.filterErrorProperty.set("");
                this.mainEditorController.filterRange(
                        getCurrentRangeAsString(),
                        this.filterColumnChoiceBox.getValue(),
                        getItemsToFilter());
            }
        }
    }
    
    private List<Integer> getItemsToFilter() {
        List<Integer> itemsToFilter = new ArrayList<>();
        for (int i = 0; i < this.filterElementMenuButton.getItems().size(); i++) {
            CheckMenuItem checkMenuItem = (CheckMenuItem) this.filterElementMenuButton.getItems().get(i);
            if (checkMenuItem.isSelected()) {
                itemsToFilter.add(i);
            }
        }
        
        return itemsToFilter;
    }
    
    @FXML
    void onColumnToFilterByClicked(MouseEvent event) {
        event.consume();
        this.mainEditorController.getColumnsOfRange(getCurrentRangeAsString());
    }
    
    public void updateFilterColumnChoiceBox(List<String> columnsToFilterBy) {
        this.filterColumnChoiceBox.getItems().clear();
        this.filterColumnChoiceBox.getItems().add("Select Column");
        if (columnsToFilterBy.isEmpty()) {
            this.filterColumnChoiceBox.getSelectionModel().selectFirst();
        } else {
            this.filterColumnChoiceBox.getItems().addAll(columnsToFilterBy);
            this.filterColumnChoiceBox.show();
        }
    }

    @FXML
    void onSortClicked(ActionEvent event) {
        String rangeToSort = this.getCurrentRangeAsString();
        
        List<String> columnsToSortBy = this.getColumnsToSortBy();
        this.mainEditorController.sortRange(rangeToSort, columnsToSortBy);
    }

    @FXML
    public void onBuildGraphClicked(ActionEvent actionEvent) {
        String rangeToBuildGraphFrom = this.getCurrentRangeAsString();
        
        this.mainEditorController.buildGraph(
                rangeToBuildGraphFrom, this.graphTypeChoiceBox.getSelectionModel().getSelectedItem());
    }
    
    public void updateGraphTypeChoiceBox() {
        this.updateGraphErrorLabel("");
        this.graphTypeChoiceBox.getSelectionModel().selectFirst();
    }

    private List<String> getColumnsToSortBy() {
        List<String> columnsToSortBy = new ArrayList<>();
        String[] columnsNames = this.columnsToSortByTextField.getText().split(";");
        
        for (String columnsName : columnsNames) {
            columnsToSortBy.add(columnsName.trim().toUpperCase());
        }

        return columnsToSortBy;
    }


    public void setMainController(MainEditorController mainViewController) {
        this.mainEditorController = mainViewController;
    }

    public void resetController() {
        this.topRightBoundaryTextField.textProperty().set("");
        this.bottomLeftBoundaryTextField.textProperty().set("");
        this.columnsToSortByTextField.textProperty().set("");
        this.filterColumnChoiceBox.getItems().clear();
        this.filterColumnChoiceBox.getItems().add("Select Column");
        this.filterColumnChoiceBox.getSelectionModel().selectFirst();
        this.filterElementMenuButton.getItems().clear();
        this.sortErrorProperty.set("");
        this.filterErrorProperty.set("");
        this.graphErrorLabel.setText("");
        this.graphTypeChoiceBox.getSelectionModel().selectFirst();
    }

    public void updateSortErrorLabel(String message) {
        this.sortErrorProperty.set(message);
    }

    public void updateFilterErrorLabel(String message) {
        this.filterErrorProperty.set(message);
    }

    public void updateGraphErrorLabel(String message) {
        this.graphErrorLabel.setText(message);
    }
    
    public List<DynamicAnalysisController> getDynamicAnalysisControllers() {
        return this.dynamicAnalysisControllers;
    }
}
