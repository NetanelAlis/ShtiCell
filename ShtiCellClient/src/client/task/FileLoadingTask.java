package client.task;

import client.gui.exception.ExceptionWindowController;
import client.gui.home.file.upload.FileUploadController;
import client.gui.util.Constants;
import client.gui.util.http.HttpClientUtil;
import dto.sheet.SheetMetaDataDTO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class FileLoadingTask extends Task<Void> {
    private final String filePath;
    private final FileUploadController fileUploadController;
    private final Consumer<SheetMetaDataDTO> onSuccessfulResponse;
    

    public FileLoadingTask(String filePath,
                           FileUploadController fileUploadController,
                           Consumer<SheetMetaDataDTO> onFinished) {
        this.filePath = filePath;
        this.fileUploadController = fileUploadController;
        this.onSuccessfulResponse = onFinished;
    }

    @Override
    protected Void call() {
        this.updateProgress(0, 100);

        // Total time for the loading process (in milliseconds)
        int totalTime = 1000;
        int steps = 100; // 100 steps for smooth progress
        int timePerStep = totalTime / steps;

        for (int i = 0; i <= steps; i++) {
            this.updateProgress(i, steps);
            this.sleepForAWhile(timePerStep);
        }
        if (!isCancelled()) {
            this.sendLoadFileRequest();
        }
        
        return null;
    }
    
    private void sendLoadFileRequest() {
        File f = new File(this.filePath);
        
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart(
                                "file",
                                f.getName(),
                                RequestBody.create(f, MediaType.parse("application/xml")))
                        .build();
        
        HttpUrl url = HttpUrl.parse(Constants.LOAD_SHEET)
                .newBuilder()
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
                    ExceptionWindowController.openExceptionPopup(
                            "Something went wrong: " + e.getMessage());
                });
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() -> {
                            fileUploadController.onTaskFinished();
                            ExceptionWindowController.openExceptionPopup(responseBodyString);
                        });
                    } else {
                        Platform.runLater(()-> {
                            fileUploadController.onTaskFinished();
                            onSuccessfulResponse.accept(Constants.GSON_INSTANCE.fromJson(responseBodyString,
                                    SheetMetaDataDTO.class));
                        });
                    }
                }
            }
        });
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