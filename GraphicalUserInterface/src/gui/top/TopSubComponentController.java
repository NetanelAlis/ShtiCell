package gui.top;

import gui.action.line.ActionLineController;
import gui.main.MainAppViewController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.File;

public class TopSubComponentController {

    @FXML private ActionLineController actionLineController;
    @FXML private MenuButton currentVersionMenuButton;
    @FXML private TextField filePathTextField;
    @FXML private TitledPane sheetNameTitledPane;
    private MainAppViewController mainAppViewController;
    private StringProperty fileName;
    private StringProperty sheetName;
    private StringProperty sheetVersion;

    public TopSubComponentController() {
        this.fileName = new SimpleStringProperty("path/to/your/xml/filename.xml");
        this.sheetName = new SimpleStringProperty("Sheet Name");
        this.sheetVersion = new SimpleStringProperty("");
    }

    @FXML
    public void initialize() {
        if (actionLineController != null) {
            actionLineController.setTopSubComponentController(this);
        }
        this.filePathTextField.textProperty().bind(fileName);
        this.sheetNameTitledPane.textProperty().bind(this.sheetName);
        this.currentVersionMenuButton.textProperty().bind(Bindings.concat("Version ", this.sheetVersion));
    }

    @FXML
    private void onLoadXMLPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Sheet file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = this.mainAppViewController.getFilePath(fileChooser);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        this.fileName.set(absolutePath); // add bind
        this.mainAppViewController.loadNewSheetFromXmlFile(absolutePath);
    }

    @FXML
    private void onThemeChanged(ActionEvent event) {

    }

    @FXML
    public void onVersionMenuClicked(MouseEvent mouseEvent) {
      int numberOfSheetVersions =  this.mainAppViewController.getSheetVersions();
      if(numberOfSheetVersions > this.currentVersionMenuButton.getItems().size()) {
          for(int i = this.currentVersionMenuButton.getItems().size(); i < numberOfSheetVersions; i++) {
              MenuItem newLastMenuItem = new MenuItem("Version " + (i + 1));
              newLastMenuItem.setOnAction(this::onVersionChanged);
              this.currentVersionMenuButton.getItems().add(newLastMenuItem);
          }
      }
  }

    public void onVersionChanged(ActionEvent event){
        MenuItem clickedMenuItem = (MenuItem) event.getSource();
        this.mainAppViewController.loadSheetVersion(Integer.parseInt(clickedMenuItem.getText().substring("Version ".length())));
    }
    public void setMainAppController(MainAppViewController mainSheetController) {
        this.mainAppViewController = mainSheetController;
    }

    public ActionLineController getActionLineController(){
        return this.actionLineController;
    }

    public void setSheetNameAndVersion(String sheetName, int sheetVersion) {
        this.sheetName.set(sheetName);
        this.sheetVersion.set(String.valueOf(sheetVersion));
        this.currentVersionMenuButton.getItems().clear();
    }

    public void setVersion(int version) {
        this.sheetVersion.set(String.valueOf(version));
    }

}