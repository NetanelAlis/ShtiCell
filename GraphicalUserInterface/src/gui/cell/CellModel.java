package gui.cell;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;
import java.util.List;

public class CellModel implements ActionLineCellModel, DependenciesCellModel {
    private StringProperty cellIDProperty;
    private StringProperty lastUpdatedVersionProperty;
    private List<String> dependsOnPropertyList;
    private List<String> influencingOnPropertyList;
    private String selectedCellD;
    private StringProperty originalValueProperty;


    public CellModel() {
        this.cellIDProperty = new SimpleStringProperty("");
        this.lastUpdatedVersionProperty = new SimpleStringProperty("");
        this.dependsOnPropertyList = new ArrayList<>();
        this.influencingOnPropertyList = new ArrayList<>();
    }


    @Override
    public void bind(StringProperty cellIDProperty, StringProperty lastVersionProperty) {
        cellIDProperty.bind(Bindings.concat("Cell ID ", this.cellIDProperty));
        lastVersionProperty.bind(Bindings.concat("Last Updated Version ", this.lastUpdatedVersionProperty));
    }

    @Override
    public StringProperty getCellIDProperty() {
        return this.cellIDProperty;
    }

    @Override
    public StringProperty getLastVersionProperty() {
        return this.lastUpdatedVersionProperty;
    }

    @Override
    public List<String> getDependsOnPropertyList() {
        return this.dependsOnPropertyList;
    }

    @Override
    public List<String> getInfluencingOnPropertyList() {
        return this.influencingOnPropertyList;
    }

    @Override
    public String getSelectedCellID() {
        return this.selectedCellD;
    }

    @Override
    public void setDependingOn(List<String> dependingOn) {
        this.dependsOnPropertyList.clear();
        this.dependsOnPropertyList.addAll(dependingOn);
    }

    @Override
    public void setInfluencingOn(List<String> influencingOn) {
        this.influencingOnPropertyList.clear();
        this.influencingOnPropertyList.addAll(influencingOn);
    }

    @Override
    public void setSelectedCell(String cellId) {
        this.selectedCellD = cellId;
    }

}
