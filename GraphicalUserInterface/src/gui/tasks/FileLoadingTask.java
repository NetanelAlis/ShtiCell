package gui.tasks;

import javafx.application.Platform;
import javafx.concurrent.Task;
import logic.Engine;
import java.util.function.Consumer;

public class FileLoadingTask extends Task<Boolean> {
    private String filePath;
    private Engine engine;
    private Consumer<String> errorHandler;  // Consumer to handle errors

    public FileLoadingTask(String filePath, Engine engine, Consumer<String> errorHandler) {
        this.filePath = filePath;
        this.engine = engine;
        this.errorHandler = errorHandler;  // Assign the passed consumer
    }

    @Override
    protected Boolean call() {
        try {
            sleepForAWhile(1000);
            updateProgress(0, 100);
            this.engine.LoadDataFromXML(this.filePath);

            // Total time for the loading process (in milliseconds)
            int totalTime = 1000;

            // Number of steps to divide the progress into
            int steps = 100; // 100 steps for smooth progress

            // Time per step (in milliseconds)
            int timePerStep = totalTime / steps; // 100ms per step

            // Simulate gradual progress
            for (int i = 0; i <= steps; i++) {
                updateProgress(i, steps);

                // Sleep for the calculated time per step
                sleepForAWhile(timePerStep);
            }
        } catch (RuntimeException e) {
            Platform.runLater(() -> errorHandler.accept(e.getMessage()));  // Pass the exception message to the consumer
            return Boolean.FALSE;
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