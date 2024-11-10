package server.servlet.editor.range;

import com.google.gson.Gson;
import server.constants.Constants;
import dto.range.RangeDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import manager.EngineManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Add New Range Servlet", urlPatterns = "/addNewRange")
public class AddNewRangeServlet extends HttpServlet {
    
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
        
        try (PrintWriter out = response.getWriter()) {
            Engine engine = engineManager.getEngine(sheetName);
            String rangeName = request.getParameter(Constants.RANGE_NAME);
            String rangeBoundaries = request.getParameter(Constants.RANGE_BOUNDARIES);
            
            if (rangeName == null || rangeName.isEmpty()) {
                ServletUtils.writeErrorResponse(response, "Range name is empty",
                        HttpServletResponse.SC_BAD_REQUEST);
            }
            
            if (rangeBoundaries == null || rangeBoundaries.isEmpty()) {
                ServletUtils.writeErrorResponse(response, "Range boundaries are empty",
                        HttpServletResponse.SC_BAD_REQUEST);
            }
            
            RangeDTO newRange;
            synchronized (engine.getSheetEditLock()) {
                try {
                    if (!engine.isPermitted(username)) {
                        ServletUtils.writeErrorResponse(response,
                                "You are not allowed to create new ranges",
                                HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    } else if (!engine.isInLatestVersion(username)) {
                        ServletUtils.writeErrorResponse(response,
                                "Unable to edit sheet while not in latest version",
                                HttpServletResponse.SC_FORBIDDEN);
                        return;
                    } else {
                        newRange = engine.addRange(rangeName, rangeBoundaries);
                    }
                } catch (Exception e) {
                    ServletUtils.writeErrorResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
            
            Gson gson = new Gson();
            String json = gson.toJson(newRange);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
