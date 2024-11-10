package server.servlet.editor.range;

import server.constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import manager.EngineManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "Delete Range Servlet", urlPatterns = "/deleteRange")
public class DeleteRangeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        String sheetName = SessionUtils.getEngineName(request);
        if (!SessionUtils.isSessionExists(response, username)
                || !SessionUtils.isActiveEngineExists(response, sheetName)) {
            return;
        }
        
        Engine engine = engineManager.getEngine(sheetName);
        String rangeName = request.getParameter(Constants.RANGE_NAME);
        
        if (rangeName == null || rangeName.isEmpty()) {
            ServletUtils.writeErrorResponse(
                    response, "Range name is empty", HttpServletResponse.SC_BAD_REQUEST);
        }
        
        synchronized (engine.getSheetEditLock()) {
            try {
                if (!engine.isPermitted(username)) {
                    ServletUtils.writeErrorResponse(response,
                            "You are not allowed to delete ranges",
                            HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                } else if (!engine.isInLatestVersion(username)) {
                    ServletUtils.writeErrorResponse(response,
                            "Unable to edit sheet while not in latest version",
                            HttpServletResponse.SC_FORBIDDEN);
                    return;
                } else {
                    engine.removeRange(rangeName);
                }
            } catch (Exception e) {
                ServletUtils.writeErrorResponse(
                        response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
