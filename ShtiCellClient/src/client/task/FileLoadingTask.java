package client.task;

import client.gui.exception.ExceptionWindowController;
import client.gui.home.file.upload.FileUploadController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.SheetMetaDataDTO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public class FileLoadingTask extends Task<Boolean> {
    private String filePath;
    private FileUploadController fileUploadController;
    private Consumer<SheetMetaDataDTO> onFinished;
    private String ownerName;

    public FileLoadingTask(String filePath, FileUploadController fileUploadController, Consumer<SheetMetaDataDTO> onFinished,String ownerName) {
        this.filePath = filePath;
        this.fileUploadController = fileUploadController;
        this.onFinished = onFinished;
        this.ownerName = ownerName;
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

        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(Constants.UPLOAD_FILE))
                .newBuilder()
                .addQueryParameter("username", this.ownerName)
                .build();

        Request request = new Request.Builder()
                .url(url)
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
                        ExceptionWindowController.openExceptionPopup(responseBodyString);
                    });
                    }
                    else {
                        Platform.runLater(() -> {
                            fileUploadController.onTaskFinished();
                            SheetMetaDataDTO sheetMetaDataDTO = Constants.GSON_INSTANCE.fromJson(responseBodyString, SheetMetaDataDTO.class);
                            onFinished.accept(sheetMetaDataDTO);
                        });
                    }
                }
            }
        });
    }
}
