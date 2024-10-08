package client.util;


public class Constants {

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/main/chat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/login/login.fxml";
    public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/ShtiCellWebApp_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/loadSheetFromFile";


}
