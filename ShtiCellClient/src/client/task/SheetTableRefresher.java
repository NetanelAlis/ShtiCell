package client.task;

import client.gui.exception.ExceptionWindowController;
import client.gui.util.Constants;
import client.gui.util.http.HttpClientUtil;
import dto.sheet.SheetMetaDataDTO;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class SheetTableRefresher extends TimerTask {
    
    private final Consumer<List<SheetMetaDataDTO>> usersListConsumer;
    
    public SheetTableRefresher(Consumer<List<SheetMetaDataDTO>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;
    }
    
    @Override
    public void run() {
        Request request =  new Request.Builder()
                .url(Constants.REFRESH_SHEET_TABLE)
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
                    String jsonArrayOfSheetsMetaData = responseBody.string();
                    SheetMetaDataDTO[] sheetsMetaData = Constants.GSON_INSTANCE.fromJson(
                            jsonArrayOfSheetsMetaData, SheetMetaDataDTO[].class);
                    usersListConsumer.accept(Arrays.asList(sheetsMetaData));
                }
            }
        });
    }
}
