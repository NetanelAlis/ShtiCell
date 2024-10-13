package client.task;

import client.gui.exception.ExceptionWindowController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.ReceivedRequestDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class PermissionTableRefresher extends TimerTask {
    private final Consumer<List<ReceivedRequestDTO>> addAllRequestsToTableConsumer;

    public PermissionTableRefresher(Consumer<List<ReceivedRequestDTO>> usersListConsumer) {
        this.addAllRequestsToTableConsumer = usersListConsumer;
    }

    @Override
    public void run() {
        Request request = new Request.Builder()
                .url(Constants.REFRESH_PERMISSION_TABLE)
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
                    ReceivedRequestDTO[] receivedRequestDTOS = Constants.GSON_INSTANCE.fromJson(responseBodyString, ReceivedRequestDTO[].class);
                    addAllRequestsToTableConsumer.accept(Arrays.asList(receivedRequestDTOS));
                }
            }
        });
    }
}