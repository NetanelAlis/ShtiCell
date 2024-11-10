package client.gui.util;

import com.google.gson.Gson;

public class Constants {
    
    // global constants
    public final static String JOHN_DOE = "";
    public final static int REFRESH_RATE = 500;
    
    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/app/MainAppView.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/client/gui/login/log-in.fxml";
    public final static String HOME_VIEW_FXML_RESOURCE_LOCATION = "/client/gui/home/main/view/HomeView.fxml";
    public final static String EDITOR_VIEW_FXML_RESOURCE_LOCATION = "/client/gui/editor/main/view/EditorView.fxml";
    public final static String FILE_UPLOAD_FXML_RESOURCE_LOCATION = "/client/gui/home/file/upload/FileUploadComponent.fxml";
    public final static String CELL_COMPONENT_FXML_RESOURCE_LOCATION = "/client/gui/editor/cell/CellSubComponent.fxml";
    public final static String DYNAMIC_ANALYSIS_FXML_RESOURCE_LOCATION = "/client/gui/editor/command/DynamicAnalysis.fxml";
    
    // css locations
    public final static String GRID_COMPONENT_CSS_RESOURCE_LOCATION = "/client/gui/editor/grid/style/MainGridComponent.css";
    
    // resource locations
    public final static String SHTICELL_LOGO_RESOURCE_LOCATION = "/client/gui/util/resources/shticellLogo.png";
    
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/ShticellWebApp_Web";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public static final String EXIT_DYNAMIC_ANALYSIS = FULL_SERVER_PATH +"/exitDynamicAnalysis";
    public static final String DYNAMIC_ANALYSIS = FULL_SERVER_PATH + "/dynamicAnalysis";
    public static final String BUILD_GRAPH = FULL_SERVER_PATH + "/buildGraph";
    public static final String FILTER_RANGE = FULL_SERVER_PATH + "/filterRange";
    public static final String GET_FILTERABLE_ELEMENTS = FULL_SERVER_PATH + "/getFilterableElements";
    public static final String GET_COLUMNS_OF_RANGE = FULL_SERVER_PATH + "/getColumnsOfRange";
    public static final String SORT_RANGE = FULL_SERVER_PATH + "/sortRange";
    public static final String UPDATE_CELL_STYLE = FULL_SERVER_PATH + "/updateCellStyle";
    public static final String DELETE_RANGE = FULL_SERVER_PATH + "/deleteRange";
    public static final String ADD_NEW_RANGE = FULL_SERVER_PATH + "/addNewRange";
    public static final String LOAD_SHEET_VERSION = FULL_SERVER_PATH + "/loadSheetVersion";
    public static final String GET_LATEST_VERSION_NUMBER = FULL_SERVER_PATH + "/getLatestVersionNumber";
    public static final String UPDATE_CELL_DATA = FULL_SERVER_PATH + "/updateCellData";
    public static final String GET_SINGLE_CELL_DATA = FULL_SERVER_PATH + "/getSingleCellData";
    public static final String GET_SHEET_AND_RANGES = FULL_SERVER_PATH + "/getSheetAndRanges";
    public static final String SET_ENGINE_TO_SESSION = FULL_SERVER_PATH + "/setEngineToSession";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String LOAD_SHEET = FULL_SERVER_PATH + "/loadSheetFromFile";
    public static final String SEND_PERMISSION_REQUEST = FULL_SERVER_PATH + "/sendPermissionRequest";
    public static final String ANSWER_PERMISSION_REQUEST = FULL_SERVER_PATH + "/answerPermissionRequest";
    
    public static final String REFRESH_EDITOR = FULL_SERVER_PATH + "/refreshEditor";
    public final static String REFRESH_SHEET_TABLE = FULL_SERVER_PATH + "/refreshSheetTable";
    public static final String REFRESH_PERMISSION_TABLE = FULL_SERVER_PATH + "/refreshPermissionTable";
    public static final String REFRESH_PERMISSION_REQUESTS_TABLE = FULL_SERVER_PATH + "/refreshPermissionRequestsTable";
    
    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
    
    // Cell Types
    public final static String NUMERIC_CELL_TYPE = "Numeric";
    public final static String STRING_CELL_TYPE = "String";
    public final static String BOOLEAN_CELL_TYPE = "Boolean";
    public final static String UNKNOWN_CELL_TYPE = "Unknown";
    public final static String NO_VALUE_CELL_TYPE = "No Value";
    
    // PermissionTypes
    public final static String NONE_PERMISSION_TYPE = "None";
    
}
