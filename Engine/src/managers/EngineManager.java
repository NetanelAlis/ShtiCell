package managers;

import logic.engine.Engine;

import java.util.*;

public class EngineManager {

    private final Map<String, Engine> engineMap;

    public EngineManager() {
        engineMap = new HashMap<>();
    }

    public synchronized void addEngine(String sheetName, Engine engine) {
        engineMap.put(sheetName, engine);
    }

    public synchronized void removeEngine(String sheetName, Engine engine) {engineMap.remove(sheetName, engine);}

    public boolean isEngineExists(String sheetName) {
        return engineMap.containsKey(sheetName);
    }

    public synchronized LinkedHashSet<Engine> getEngines() {
        return new LinkedHashSet<>(engineMap.values());
    }
}
