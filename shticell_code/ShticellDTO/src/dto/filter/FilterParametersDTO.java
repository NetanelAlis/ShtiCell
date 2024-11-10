package dto.filter;

import java.util.List;

public class FilterParametersDTO {
    private String rangeToFilterBy;
    private String columnToFilterBy;
    List<Integer> itemsToFilterBy;
    
    public FilterParametersDTO(String rangeToFilterBy, String columnToFilterBy, List<Integer> itemsToFilterBy) {
        this.rangeToFilterBy = rangeToFilterBy;
        this.columnToFilterBy = columnToFilterBy;
        this.itemsToFilterBy = itemsToFilterBy;
    }
    
    public String getRangeToFilterBy() {return this.rangeToFilterBy;}
    
    public String getColumnToFilterBy() {return this.columnToFilterBy;}
    
    public List<Integer> getItemsToFilterBy() {return this.itemsToFilterBy;}
}
