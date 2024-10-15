package client.util;


import com.google.gson.Gson;

import java.net.URL;

public class Constants {
    // global constants
    public final static int REFRESH_RATE = 500;
    public final static String SHEET_EMPTY = "empty sheet";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/ShtiCellWebApp_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/loadSheetFromFile";
    public final static String REQUEST_PERMISSION = FULL_SERVER_PATH + "/requestPermission";
    public final static String RESPONSE_TO_PERMISSION_REQUEST = FULL_SERVER_PATH + "/responseToPermissionRequest";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String REFRESH_SHEET_TABLE = FULL_SERVER_PATH + "/refreshSheetTable";
    public final static String REFRESH_RECEIVED_PERMISSION_REQUEST_TABLE = FULL_SERVER_PATH + "/refreshReceivedPermissionRequestTable";
    public final static String REFRESH_REQUESTED_PERMISSION_TABLE = FULL_SERVER_PATH + "/refreshPermissionTable";

    //xml main pages locations
    public final static String HOME_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/home/main/view/HomeView.fxml";
    public final static String EDITOR_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/editor/main/view/MainAppView.fxml";
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/app/HomePage.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/login/LogIn.fxml";






    public static Gson GSON_INSTANCE = new Gson();
}
