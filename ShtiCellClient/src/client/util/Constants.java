package client.util;


import com.google.gson.Gson;

public class Constants {
    // global constants
    public final static int REFRESH_RATE = 2000;

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/ShtiCellWebApp_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/loadSheetFromFile";
    public final static String REQUEST_PERMISSION = FULL_SERVER_PATH + "/requestPermission";
    public final static String REFRESH_SHEET_TABLE = FULL_SERVER_PATH + "/refreshSheetTable";
    public final static String REFRESH_PERMISSION_TABLE = FULL_SERVER_PATH + "/refreshPermissionRequestTable";

    public static Gson GSON_INSTANCE = new Gson();
}
