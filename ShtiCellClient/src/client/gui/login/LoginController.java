package client.gui.login;

import client.gui.app.MainAppViewController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
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
import java.util.Objects;

public class LoginController {

    @FXML
    private Label errorMessageLabel;
    @FXML
    private TextField userNameTextField;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private MainAppViewController mainAppViewController;


    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {

        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(Constants.LOGIN_PAGE))
                .newBuilder()
                .addQueryParameter("username", this.userNameTextField.getText())
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
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        mainAppViewController.updateUserName(userName);
                        mainAppViewController.switchToHomePage();
                    });
                }
            }
        });
    }

    @FXML
    private void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    @FXML
    private void quitButtonClicked(ActionEvent e) {
        Platform.exit();
    }

    public void setAppMainController(MainAppViewController mainAppViewController) {
        this.mainAppViewController = mainAppViewController;
    }
}
