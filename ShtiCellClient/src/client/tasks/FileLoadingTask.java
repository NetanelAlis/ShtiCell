package client.tasks;

import client.gui.exception.ExceptionWindowController;
import client.gui.home.file.upload.FileUploadController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.SheetNameAndSizeDTO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class FileLoadingTask extends Task<Boolean> {
    private String filePath;
    private FileUploadController fileUploadController;
    private Consumer<SheetNameAndSizeDTO> onFinished;

    public FileLoadingTask(String filePath, FileUploadController fileUploadController, Consumer<SheetNameAndSizeDTO> sheetNameAndSizeDTOConsumer) {
        this.filePath = filePath;
        this.fileUploadController = fileUploadController;
        this.onFinished = sheetNameAndSizeDTOConsumer;
    }

    @Override
    protected Boolean call() {
        updateProgress(0, 100);
        // Total time for the loading process (in milliseconds)
        int totalTime = 1000;
        int steps = 100; // 100 steps for smooth progress
        int timePerStep = totalTime / steps;

        for (int i = 0; i <= steps; i++) {
            updateProgress(i, steps);
            sleepForAWhile(timePerStep);
        }
        if(!isCancelled()) {
            this.sendLoadFileRequest();
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
                Platform.runLater(() -> {
                    fileUploadController.onTaskFinished();
                    ExceptionWindowController.openExceptionPopup("Something went wrong: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->{
                                fileUploadController.onTaskFinished();
                        System.out.println(responseBody);
                        ExceptionWindowController.openExceptionPopup(responseBodyString);
                    });
                    }
                    else {
                        Platform.runLater(() -> {
                            fileUploadController.onTaskFinished();
                            SheetNameAndSizeDTO sheetNameAndSizeDTO = Constants.gson.fromJson(responseBodyString, SheetNameAndSizeDTO.class);
                            onFinished.accept(sheetNameAndSizeDTO);
                        });
                    }
                }
            }
        });
    }
}
