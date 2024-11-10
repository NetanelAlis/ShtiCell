package client.gui.login;

import client.gui.app.MainAppViewController;
import client.gui.util.Constants;
import client.gui.util.http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginController {
    
    @FXML public TextField userNameTextField;
    @FXML public Label errorMessageLabel;
    
    private MainAppViewController mainAppViewController;
    
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    
    @FXML
    public void initialize() {
        this.errorMessageLabel.textProperty().bind(this.errorMessageProperty);
    }
    
    @FXML
    private void onLoginClicked(ActionEvent event) {
        
        String userName = this.userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        
        HttpUrl url = HttpUrl.parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        HttpClientUtil.runAsync(request, new Callback() {
            
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }
            
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set(responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        mainAppViewController.updateUserName(userName);
                        mainAppViewController.switchToHomeView();
                    });
                }
            }
        });
    }
    
    @FXML
    private void userNameKeyTyped(KeyEvent event) {
        this.errorMessageProperty.set("");
    }
    
    public void setMainAppController(MainAppViewController mainAppViewController) {
        this.mainAppViewController = mainAppViewController;
    }
}
