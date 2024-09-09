package gui.top;

import gui.action.line.ActionLineController;
import gui.grid.MainSheetController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class TopSubComponentController {

    @FXML private ActionLineController actionLineController;
    @FXML private MenuButton currentVersionMenuButton;
    @FXML private TextField filePathTextField;
    @FXML private TitledPane sheetNameTitledPane;
    @FXML private MenuButton themeMenuButton;
    private MainSheetController mainSheetController;

    private Stage primaryStage;

    @FXML
    public void initialize() {
        if (actionLineController != null) {
            actionLineController.setTopSubComponentController(this);
        }
    }

    public void changeSomething() {
        this.filePathTextField.setText("the update value button was pressed");
    }

    @FXML
    private void onLoadXMLPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Sheet file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        filePathTextField.setText(absolutePath);

//        ElementObservableListDecorator<E> selectedFileProperty;
//        selectedFileProperty.set(absolutePath);
//        isFileSelected.set(true);
    }

    @FXML
    private void onThemeChanged(ActionEvent event) {

    }

    @FXML
    private void onVersionChanged(ActionEvent event) {

    }

    public void setMainSheetController(MainSheetController mainSheetController) {
        this.mainSheetController = mainSheetController;
    }

}