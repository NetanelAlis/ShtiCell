package client.task;

import client.gui.exception.ExceptionWindowController;
import client.gui.util.Constants;
import client.gui.util.http.HttpClientUtil;
import dto.permission.PermissionDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class PermissionTableRefresher extends TimerTask {
    private final Consumer<List<PermissionDTO>> permissionsListConsumer;
    private String sheetName;
    
    public PermissionTableRefresher(Consumer<List<PermissionDTO>> usersListConsumer) {
        this.permissionsListConsumer = usersListConsumer;
    }
    
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
        this.run();
    }
    
    @Override
    public void run() {
        
        if (this.sheetName == null || this.sheetName.isEmpty()){
            return;
        }
        
        HttpUrl url = HttpUrl.parse(Constants.REFRESH_PERMISSION_TABLE)
                .newBuilder()
                .addQueryParameter("sheetName", this.sheetName)
                .build();
        
        Request request =  new Request.Builder()
                .url(url)
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
                    String jsonArrayOfSheetPermissions = responseBody.string();
                    PermissionDTO[] permissions = Constants.GSON_INSTANCE.fromJson(
                            jsonArrayOfSheetPermissions, PermissionDTO[].class);
                    permissionsListConsumer.accept(Arrays.asList(permissions));
                }
            }
        });
    }
}
