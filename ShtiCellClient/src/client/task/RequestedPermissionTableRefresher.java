package client.task;

import client.gui.exception.ExceptionWindowController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.permission.RequestedRequestForTableDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;
import java.util.function.Consumer;

public class RequestedPermissionTableRefresher extends TimerTask {
    private final Consumer<List<RequestedRequestForTableDTO>> addAllRequestsToTableConsumer;
    private String sheetName;

    public RequestedPermissionTableRefresher(Consumer<List<RequestedRequestForTableDTO>> addAllRequestsToTableConsumer) {
        this.addAllRequestsToTableConsumer = addAllRequestsToTableConsumer;
        this.sheetName = Constants.SHEET_EMPTY;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public void run() {
        if(Objects.equals(sheetName, Constants.SHEET_EMPTY)) {
            return;
        }
            HttpUrl url = Objects.requireNonNull(HttpUrl.parse(Constants.REFRESH_REQUESTED_PERMISSION_TABLE))
                    .newBuilder()
                    .addQueryParameter("sheetname", this.sheetName)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
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
                        RequestedRequestForTableDTO[] requestedRequestForTableDTOS = Constants.GSON_INSTANCE.fromJson(responseBodyString, RequestedRequestForTableDTO[].class);
                        addAllRequestsToTableConsumer.accept(Arrays.asList(requestedRequestForTableDTOS));
                    }
                }
            });
        }
    }
