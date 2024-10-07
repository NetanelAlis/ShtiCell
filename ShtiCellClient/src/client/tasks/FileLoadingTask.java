package client.tasks;

import javafx.concurrent.Task;
import logic.Engine;

public class FileLoadingTask extends Task<Boolean> {
    private String filePath;
    private Engine engine;

    public FileLoadingTask(String filePath, Engine engine) {
        this.filePath = filePath;
        this.engine = engine;
    }

    @Override
    protected Boolean call() {
        sleepForAWhile(1000);
        updateProgress(0, 100);
        this.engine.loadData(this.filePath);

        // Total time for the loading process (in milliseconds)
        int totalTime = 1000;
        int steps = 100; // 100 steps for smooth progress
        int timePerStep = totalTime / steps;

        for (int i = 0; i <= steps; i++) {
            updateProgress(i, steps);
            sleepForAWhile(timePerStep);
        }

        return Boolean.TRUE;
    }

    private void sleepForAWhile(long sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
            }
        }
    }
}