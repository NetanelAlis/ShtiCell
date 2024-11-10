package client.task;

import client.gui.exception.ExceptionWindowController;
import client.gui.util.Constants;
import client.gui.util.http.HttpClientUtil;
import dto.permission.ReceivedPermissionRequestDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class PermissionRequestsTableRefresher  extends TimerTask {
    
    private final Consumer<List<ReceivedPermissionRequestDTO>> requestsListConsumer;
    
    public PermissionRequestsTableRefresher(Consumer<List<ReceivedPermissionRequestDTO>> requestsListConsumer) {
        this.requestsListConsumer = requestsListConsumer;
    }
    
    @Override
    public void run() {
        Request request =  new Request.Builder()
                .url(Constants.REFRESH_PERMISSION_REQUESTS_TABLE)
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
                    String jsonArrayOfPermissionRequests = responseBody.string();
                    ReceivedPermissionRequestDTO[] receivedPermissionRequests = Constants.GSON_INSTANCE.fromJson(
                            jsonArrayOfPermissionRequests, ReceivedPermissionRequestDTO[].class);
                    requestsListConsumer.accept(Arrays.asList(receivedPermissionRequests));
                }
            }
        });
    }
}
