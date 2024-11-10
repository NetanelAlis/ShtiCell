package client.task;

import client.gui.exception.ExceptionWindowController;
import client.gui.util.Constants;
import client.gui.util.http.HttpClientUtil;
import dto.version.EditorRefresherAnswerDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class EditorRefresher extends TimerTask {
    
    private final Consumer<Boolean> editorConsumer;
    private final Consumer<Integer> versionsNotificationConsumer;
    
    public EditorRefresher(Consumer<Boolean> editorConsumer, Consumer<Integer> versionsNotificationConsumer) {
        this.editorConsumer = editorConsumer;
        this.versionsNotificationConsumer = versionsNotificationConsumer;
    }
    
    @Override
    public void run() {
        Request request =  new Request.Builder()
                .url(Constants.REFRESH_EDITOR)
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        ExceptionWindowController.openExceptionPopup(
                                "Something went wrong: " + e.getMessage())
                );
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody responseBody = response.body()) {
                    String jsonAnswer = responseBody.string();
                    if (response.code() != 200) {
                        Platform.runLater(() -> ExceptionWindowController.openExceptionPopup(jsonAnswer));
                    } else {
                        EditorRefresherAnswerDTO refresherAnswer =
                                Constants.GSON_INSTANCE.fromJson(jsonAnswer, EditorRefresherAnswerDTO.class);
                        Platform.runLater(() -> {
                            editorConsumer.accept(refresherAnswer.isInReaderMode());
                            
                            if (refresherAnswer.shouldSendNotification()) {
                                versionsNotificationConsumer.accept(refresherAnswer.getLatestVersion());
                            }
                        });
                    }
                }
            }
        });
    }
}
