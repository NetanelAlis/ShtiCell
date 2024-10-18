package client.util;


import com.google.gson.Gson;


public class Constants {
    ////////////global constants////////////////////
    public final static int REFRESH_RATE = 500;
    public final static String SHEET_EMPTY = "empty sheet";
    public final static String NO_PERMISSION = "None";
    ///////////////////////////////////////////////




    //////////////Server resources locations////////////////////
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/ShtiCellWebApp_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String UPLOAD_FILE = FULL_SERVER_PATH + "/loadSheetFromFile";
    public final static String REQUEST_PERMISSION = FULL_SERVER_PATH + "/requestPermission";
    public final static String RESPONSE_TO_PERMISSION_REQUEST = FULL_SERVER_PATH + "/responseToPermissionRequest";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String UPDATE_ENGINE_NAME_IN_SESSION = FULL_SERVER_PATH + "/updateEngineNameInSession";
    public final static String REFRESH_SHEET_TABLE = FULL_SERVER_PATH + "/refreshSheetTable";
    public static final String GET_SHEET = FULL_SERVER_PATH + "/getSheet";
    public static final String GET_SHEET_VERSION =FULL_SERVER_PATH + "/getSheetVersion"; ;
    public static final String GET_CELL = FULL_SERVER_PATH + "/getCell";
    public static final String UPDATE_CELL_DATA = FULL_SERVER_PATH + "/updateCellData";
    public static final String GET_SHEET_AND_RANGES = FULL_SERVER_PATH + "/getSheetAndRanges";
    public static final String ADD_RANGE = FULL_SERVER_PATH + "/addRange";
    public static final String DELETE_RANG = FULL_SERVER_PATH + "/deleteRange";
    public static final String SORT_RANGE = FULL_SERVER_PATH + "/sortRange";
    public static final String GET_COLUMNS_TO_FILTER_BY = FULL_SERVER_PATH + "/getColumnsToFilterBy";
    public static final String GET_ITEMS_TO_FILTER_BY = FULL_SERVER_PATH + "/getItemsToFilterBy"; ;
    public static final String FILTER_RANGE = FULL_SERVER_PATH + "/filterRange";
    public static final String UPDATE_CELL_STYLE = FULL_SERVER_PATH + "/updateCellStyle";
    public static final String GET_SHEET_LATEST_VERSION_NUMBER = FULL_SERVER_PATH + "/getSheetLatestVersionNumber";
    public final static String REFRESH_RECEIVED_PERMISSION_REQUEST_TABLE = FULL_SERVER_PATH + "/refreshReceivedPermissionRequestTable";
    public final static String REFRESH_REQUESTED_PERMISSION_TABLE = FULL_SERVER_PATH + "/refreshPermissionTable";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////xml locations/////////////////////////////////////////////////////////////////////

    //main pages locations
    public final static String HOME_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/home/main/view/HomeView.fxml";
    public final static String EDITOR_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/editor/main/view/MainAppView.fxml";
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/app/HomePage.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/login/LogIn.fxml";

    // resources
    public final static String SHTICELL_ICON_LOCATION = "/client/gui/resources/shticellLogo.png";

    /////////////////////////////////////////////////////////////////////////////////

    // cell types
    public final static String NUMERIC_CELL_TYPE = "Numeric";
    public final static String STRING_CELL_TYPE = "String";
    public final static String BOOLEAN_CELL_TYPE = "Boolean";
    public final static String UNKNOWN_CELL_TYPE = "Unknown";
    public final static String NO_VALUE_CELL_TYPE = "No Value";


    public static Gson GSON_INSTANCE = new Gson();

}
