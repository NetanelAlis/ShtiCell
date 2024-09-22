package component.cell.impl;

import component.cell.api.Cell;
import component.range.api.Range;
import component.sheet.api.ReadOnlySheet;
import component.sheet.api.Sheet;
import javafx.scene.paint.Color;
import logic.parser.FunctionParser;
import logic.function.returnable.Returnable;
import logic.parser.OriginalValueParser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CellImpl implements Cell {

    private String cellID;;
    private String originalValue;
    private Returnable effectiveValue;
    private int version;
    List<Cell> dependingOnCells;
    List<Cell> influencingCells;
    ReadOnlySheet sheet;
    private SerializableColor backgroundColor;
    private SerializableColor textColor;

    public CellImpl(String cellID, String originalValue, int version, ReadOnlySheet sheet){
        this.cellID =  Character.toUpperCase(cellID.charAt(0)) + cellID.substring(1);
        this.originalValue = originalValue;
        this.sheet = sheet;
        this.version = version;
        this.dependingOnCells = new ArrayList<>();
        this.influencingCells = new ArrayList<>();
        this.backgroundColor = new SerializableColor(Color.WHITE);
        this.textColor = new SerializableColor(Color.BLACK);

        getInfluencingCellsFromDummy();
        this.setDependencies();

    }

    private void setDependentAndInfluencingCells(String dependentCellID){
        Cell dependentCell = sheet.getCell(dependentCellID);

        if(dependentCell == null){
            dependentCell = new CellImpl(dependentCellID,"", version, sheet);
            this.sheet.getSheetCells().put(dependentCell.getCellId(), dependentCell);

        }

        this.dependingOnCells.add(dependentCell);
        dependentCell.getInfluecningOn().add(this);

    }

    private void setDependencies(){
        Set<String> dependencies = this.getDependenciesRanges();

        dependencies.addAll(this.getDependenciesFromRef());
        dependencies.forEach(this::setDependentAndInfluencingCells);

    }

    private Set<String> getDependenciesFromRef(){
       Set<String> dependencies =
        OriginalValueParser.REF.extract(originalValue)
                .stream()
               .filter(Sheet::isValidCellID)
               .collect(Collectors.toSet());

            return dependencies;
    }

    private Set<String> getDependenciesRanges() {
        Set<String> dependencies = new HashSet<>();

        this.getUsedRanges()
                .stream()
                    .filter(this.sheet::isExistingRange)
                .forEach(rangeName -> {
                    Range currentRange = this.sheet.getRanges().get(rangeName);
                    currentRange.increaseUsage();
                    currentRange.getRangeCells().forEach((cell -> dependencies.add(cell.getCellId())));
                });

        return dependencies;
    }

    private void getInfluencingCellsFromDummy(){
        Cell dummyCell = this.sheet.getCell(this.cellID);
        if(dummyCell != null){
            this.influencingCells.addAll(dummyCell.getInfluecningOn());
        }
    }

    @Override
    public String getCellId() {
        return this.cellID;
    }

    @Override
    public String getOriginalValue() {
        return this.originalValue;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public List<Cell> getDependsOn() {
        return this.dependingOnCells;
    }

    @Override
    public List<Cell> getInfluecningOn() {
        return this.influencingCells;
    }

    @Override
    public void setOriginalValue(String value, int newSheetVersion) {
        this.originalValue = value;

        this.dependingOnCells.forEach(cell -> cell.getInfluecningOn().remove(this));

        this.dependingOnCells.clear();
        this.setDependencies();
        this.version = newSheetVersion;
    }

@Override
    public Returnable getEffectiveValue() {
        return this.effectiveValue;
    }

    @Override
    public boolean calculateEffectiveValue(){
        Returnable newEffectiveValue = FunctionParser.parseFunction(this.originalValue).invoke(this.sheet);

        if(newEffectiveValue.equals(this.effectiveValue)){
            return false;
        }

        else{
            this.effectiveValue = newEffectiveValue;
            return true;
        }
    }

    @Override
    public void updateVersion(int sheetUpdatedVersion) {
        this.version = sheetUpdatedVersion;
    }

    @Override
    public Set<String> getUsedRanges() {
        Set<String> rangeNames = OriginalValueParser.SUM.extract(this.originalValue);
        rangeNames.addAll(OriginalValueParser.AVERAGE.extract(this.originalValue));
        return rangeNames;

    }

    @Override
    public SerializableColor getBackgroundColor() {
        return this.backgroundColor;
    }

    @Override
    public SerializableColor getTextColor() {
        return this.textColor;
    }

    @Override
    public void setBackgroundColor(Color color) {
        this.backgroundColor = new SerializableColor(color);
    }

    @Override
    public void setTextColor(Color color) {
        this.textColor = new SerializableColor(color);
    }

    @Override
    public void updateCellID(String cellID) {
        this.cellID = cellID;
    }

}
