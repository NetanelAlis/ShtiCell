package server.utils;

import server.constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static server.utils.ServletUtils.writeErrorResponse;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
    
    public static String getEngineName (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.SHEET_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
    
    public static boolean isSessionExists (HttpServletResponse response, String username) throws IOException {
        if (username == null) {
            writeErrorResponse(
                    response, "User is not logged in", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        return true;
    }
    
    public static boolean isActiveEngineExists(HttpServletResponse response, String sheetName) throws IOException {
        if (sheetName == null) {
            writeErrorResponse(
                    response, "No Active Sheet Found", HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        
        return true;
    }
    
    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}