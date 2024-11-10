package client.gui.editor.command;

import client.gui.editor.main.view.MainEditorController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class DynamicAnalysisController {
    @FXML private TextField cellIDTextField;
    @FXML private TextField maxValueTextField;
    @FXML private TextField minValueTextField;
    @FXML private TextField stepSizeTextField;
    @FXML private Label errorLabel;
    @FXML private Slider dynamicAnalysisSlider;
    
    private MainEditorController mainEditorController;
    private CommandsController commandsController;
    private StringProperty errorLabelProperty;
    
    private double currentValue;
    private Integer minValue = null;
    private Integer maxValue = null;
    private Integer stepSize = null;
    private int analyserID;
    
    public DynamicAnalysisController() {
        this.errorLabelProperty = new SimpleStringProperty("");
    }
    
    @FXML
    private void initialize() {
        this.dynamicAnalysisSlider.disableProperty().bind(
                Bindings.or(this.cellIDTextField.textProperty().isEmpty(),
                            Bindings.or(this.minValueTextField.textProperty().isEmpty(),
                                        Bindings.or(this.maxValueTextField.textProperty().isEmpty(),
                                                    Bindings.or(this.stepSizeTextField.textProperty().isEmpty(),
                                                                this.errorLabelProperty.isEmpty().not()
                                                    )
                                        )
                            )
                )
        );
        
        this.errorLabel.textProperty().bind(this.errorLabelProperty);
        
        this.initializeCellIDTextField();
        this.initializeMinValueTextField();
        this.initializeMaxValueTextField();
        this.initializeStepSizeTextField();
        
        this.dynamicAnalysisSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!Objects.equals(newValue, this.currentValue) && this.minValue != null &&
                    this.maxValue != null && this.minValue <= this.maxValue) {
                this.currentValue = Math.round(newValue.doubleValue() / this.stepSize) * this.stepSize;
                this.dynamicAnalysisSlider.setValue(this.currentValue);
                this.mainEditorController.dynamicAnalysis(
                        this.cellIDTextField.getText(), this.currentValue, this.analyserID);
            }
        });
    }
    
    private void initializeCellIDTextField() {
        this.cellIDTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            this.errorLabelProperty.set("");
        });
    }
    
    private void initializeMaxValueTextField() {
        this.maxValueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null && !newValue.isEmpty()) {
                    this.maxValue = Integer.parseInt(newValue);
                    this.dynamicAnalysisSlider.setMax(this.maxValue);
                    if(this.minValue != null) {
                        this.dynamicAnalysisSlider.setMin(this.minValue);
                    }
                }
            } catch (NumberFormatException e) {
                this.errorLabelProperty.set("Max value must be an integer");
                this.maxValueTextField.setText("");
            }
        });
    }

    private void initializeMinValueTextField() {
        this.minValueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null && !newValue.isEmpty()) {
                    this.minValue = Integer.parseInt(newValue);
                    this.dynamicAnalysisSlider.setMin(this.minValue);
                    if(this.maxValue != null) {
                        this.dynamicAnalysisSlider.setMax(this.maxValue);
                    }
                }
            } catch (NumberFormatException e) {
                this.errorLabelProperty.set("Min value must be an integer");
                this.minValueTextField.setText("");
            }
        });
    }

    private void initializeStepSizeTextField() {
        this.stepSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null && !newValue.isEmpty()) {
                    this.stepSize = Integer.parseInt(newValue);
                    this.dynamicAnalysisSlider.setBlockIncrement(this.stepSize);
                }
            } catch (NumberFormatException e) {
                this.errorLabelProperty.set("Step Size must be an integer");
                this.stepSizeTextField.setText("");
            }
        });
    }

    public void updateErrorLabel(String message) {
        this.errorLabelProperty.set(message);
    }
    
    public void setMainEditorController(MainEditorController mainEditorController) {
        this.mainEditorController = mainEditorController;
    }
    
    public void setCommandsController(CommandsController commandsController) {
        this.commandsController = commandsController;
    }
    
    public void setSelectedCell(String cellID) {
        this.cellIDTextField.setText(cellID);
    }
    
    public void setAnalyserID(int id) {
        this.analyserID = id;
    }
    
    public void resetController() {
        this.cellIDTextField.setText("");
        this.minValueTextField.setText("");
        this.maxValueTextField.setText("");
        this.stepSizeTextField.setText("");
        this.errorLabelProperty.set("");
    }
}
