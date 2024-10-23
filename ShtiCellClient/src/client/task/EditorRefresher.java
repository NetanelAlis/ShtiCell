package client.task;

import client.gui.exception.ExceptionWindowController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.version.EditorRefresherAnswerDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class EditorRefresher extends TimerTask {

    private final Consumer<Boolean> updateEditableActions;
    private final Runnable updateIsUerInLastVersionClass;

    public EditorRefresher(Consumer<Boolean> updateEditableActions, Runnable updateIsUerInLastVersionClass) {
        this.updateEditableActions = updateEditableActions;
        this.updateIsUerInLastVersionClass = updateIsUerInLastVersionClass;
    }

    @Override
    public void run() {
        Request request = new Request.Builder()
                .url(Constants.REFRESH_EDITOR)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    ExceptionWindowController.openExceptionPopup("Something went wrong: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    String responseBodyString = responseBody.string();
                    if(response.code() != 200){
                        Platform.runLater(() -> {
                            ExceptionWindowController.openExceptionPopup(responseBodyString);
                        });
                    }
                    EditorRefresherAnswerDTO editorRefresherAnswerDTO = Constants.GSON_INSTANCE.fromJson(responseBodyString, EditorRefresherAnswerDTO.class);
                    Platform.runLater(()->{
                    updateEditableActions.accept(editorRefresherAnswerDTO.isUserCantEditTheSheet());
                    if(editorRefresherAnswerDTO.isUserNotOnLastSheetVersion()){
                        updateIsUerInLastVersionClass.run();
                        }
                    });
                }
            }
        });
    }
}
