package gui.action.line;

import gui.grid.MainSheetController;
import gui.main.MainAppViewController;
import gui.top.TopSubComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ActionLineController {
    private TopSubComponentController topSubComponentController;
    private MainAppViewController mainAppViewController;

    @FXML
    private Label cellIDLabel;

    @FXML
    private Label lastUpdatedCellValueVersionLabel;

    @FXML
    private TextField originalValueTextField;

    @FXML
    private Button updateValueButton;

    @FXML
    private void onUpdateValuePressed(ActionEvent event) {
        lastUpdatedCellValueVersionLabel.setText("next version");
        this.topSubComponentController.changeSomething();
    }

    public void setTopSubComponentController(TopSubComponentController topSubComponentController) {
        this.topSubComponentController = topSubComponentController;
    }

    public void setMainAppController(MainAppViewController mainAppViewController){
        this.mainAppViewController = mainAppViewController;
    }

}