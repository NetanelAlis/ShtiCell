package dto;

import component.range.api.Range;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RangesDTO {
    private List<RangeDTO> ranges;

    public RangesDTO(Map<String, Range> ranges) {
        this.ranges = new ArrayList<>(ranges.size());
        ranges.forEach((key, value) -> this.ranges.add(new RangeDTO(value)));
    }

    public List<RangeDTO> getRanges() {
        return this.ranges;
    }
}