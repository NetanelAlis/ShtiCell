package client.tasks;

import client.gui.exception.ExceptionWindowController;
import client.gui.home.file.upload.FileUploadController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FileLoadingTask extends Task<Boolean> {
    private String filePath;
    private FileUploadController fileUploadController;
    private Runnable onFinished;

    public FileLoadingTask(String filePath, FileUploadController fileUploadController, Runnable onFinished) {
        this.filePath = filePath;
        this.fileUploadController = fileUploadController;
        this.onFinished = onFinished;
    }

    @Override
    protected Boolean call() {
        sleepForAWhile(1000);
        updateProgress(0, 100);
        this.sendLoadFileRequest();


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

    private void sendLoadFileRequest() {
        File file = new File(filePath);

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("application/xml")))
                        .build();

        Request request = new Request.Builder()
                .url(Constants.UPLOAD_FILE)
                .post(body)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                fileUploadController.onTaskFinished(Optional.empty()));
                ExceptionWindowController.openExceptionPopup("Something went wrong: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                    fileUploadController.onTaskFinished(Optional.empty()));
                    ExceptionWindowController.openExceptionPopup(responseBody);
                } else {
                    Platform.runLater(()-> {
                        fileUploadController.onTaskFinished(Optional.ofNullable(onFinished));
                    }) ;
                }
            }
        });
    }







}
