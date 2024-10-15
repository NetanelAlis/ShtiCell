package client.task;

import client.gui.exception.ExceptionWindowController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.permission.ReceivedRequestForTableDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ReceivedPermissionTableRefresher extends TimerTask {
    private final Consumer<List<ReceivedRequestForTableDTO>> addAllRequestsToTableConsumer;

    public ReceivedPermissionTableRefresher(Consumer<List<ReceivedRequestForTableDTO>> usersListConsumer) {
        this.addAllRequestsToTableConsumer = usersListConsumer;
    }

    @Override
    public void run() {
        Request request = new Request.Builder()
                .url(Constants.REFRESH_RECEIVED_PERMISSION_REQUEST_TABLE)
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
                    ReceivedRequestForTableDTO[] receivedRequestForTableDTOS = Constants.GSON_INSTANCE.fromJson(responseBodyString, ReceivedRequestForTableDTO[].class);
                    addAllRequestsToTableConsumer.accept(Arrays.asList(receivedRequestForTableDTOS));
                }
            }
        });
    }
}