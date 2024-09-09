package gui.main;

import gui.action.line.ActionLineController;
import gui.cell.CellSubComponentController;
import gui.grid.MainSheetController;
import gui.top.TopSubComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

public class MainAppViewController {
    @FXML
    TopSubComponentController topSubComponentController;
    private MainSheetController mainSheetController;
    private ActionLineController actionLineController;
    private Map<String,CellSubComponentController> cellSubComponentControllers;

    @FXML
    public void initialize() {
        if (topSubComponentController != null) {
            this.topSubComponentController.setMainAppController();
        }

    }

    public void mainSheetController(MainSheetController mainSheetController) {
        this.mainSheetController = mainSheetController;
        this.mainSheetController.setMainAppController();
    }

    public void actionLineController(ActionLineController actionLineController) {
        this.actionLineController = actionLineController;
        this.actionLineController.setMainAppController();
    }

}
