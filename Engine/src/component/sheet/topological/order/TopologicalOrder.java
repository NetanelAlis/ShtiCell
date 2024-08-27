package component.sheet.topological.order;

import component.cell.api.Cell;
import java.util.*;

public enum TopologicalOrder {
    SORT;

    public List<Cell> topologicalSort(Map<String, Cell> graph) {
        List<Cell> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();

        for (String cellID : graph.keySet()) {
            if (!visited.contains(cellID)) {
                if (dfs(graph, cellID, visited, visiting, result)) {
                    throw new IllegalArgumentException("Found Circular Reference in the sheet" +
                            " containing cell - " + cellID);
                }
            }
        }

        Collections.reverse(result);  // Reverse to get the correct topological order
        return result;
    }

    private boolean dfs(Map<String, Cell> graph, String cellID, Set<String> visited, Set<String> visiting, List<Cell> result) {
        if (visiting.contains(cellID)) {
            return true;  // Found a cycle, hence it's not a DAG
        }

        if (visited.contains(cellID)) {
            return false;  // Node already processed, no need to visit again
        }

        visiting.add(cellID);

        List<Cell> neighbors = graph.get(cellID).getInfluecningOn();
        if (neighbors != null) {
            for (Cell neighbor : neighbors) {
                if (dfs(graph, neighbor.getCellId(), visited, visiting, result)) {
                    return true;
                }
            }
        }

        visiting.remove(cellID);
        visited.add(cellID);
        result.add(graph.get(cellID));
        return false;
    }

}