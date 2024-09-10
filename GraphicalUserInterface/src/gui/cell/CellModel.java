package gui.cell;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;
import java.util.List;

public class CellModel implements ActionLineCellModel, DependenciesCellModel {
    private StringProperty cellIDProperty;
    private StringProperty originalValueProperty;
    private StringProperty lastUpdatedVersionProperty;
    private List<String> dependsOnPropertyList;
    private List<String> influencingOnPropertyList;

    public CellModel() {
        this.cellIDProperty = new SimpleStringProperty("");
        this.originalValueProperty = new SimpleStringProperty();
        this.lastUpdatedVersionProperty = new SimpleStringProperty("");
        this.dependsOnPropertyList = new ArrayList<>();
        this.influencingOnPropertyList = new ArrayList<>();
    }


    @Override
    public void bind(StringProperty cellIDProperty, StringProperty originalValueProperty, StringProperty lastVersionProperty) {
        cellIDProperty.bind(Bindings.concat("Cell ID ", this.cellIDProperty));
        cellIDProperty.bind(Bindings.concat("Last Updated Version ", this.lastUpdatedVersionProperty));
        originalValueProperty.bind(this.originalValueProperty);
        lastVersionProperty.bind(this.lastUpdatedVersionProperty);
    }

    @Override
    public StringProperty getCellIDProperty() {
        return this.cellIDProperty;
    }

    @Override
    public StringProperty getOriginalValueProperty() {
        return this.originalValueProperty;
    }

    @Override
    public StringProperty getLastVersionProperty() {
        return this.lastUpdatedVersionProperty;
    }

    @Override
    public List<String> getDependsOnPropertyList(List<String> dependencies) {
        return this.dependsOnPropertyList;
    }

    @Override
    public List<String> getInfluencingOnPropertyList(List<String> influencing) {
        return this.influencingOnPropertyList;
    }

}
