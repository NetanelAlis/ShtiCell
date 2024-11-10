package server.servlet.editor.style;

import com.google.gson.Gson;
import dto.cell.CellStyleDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import manager.EngineManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "Update Cell Style Servlet", urlPatterns = "/updateCellStyle")
public class UpdateCellStyleServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        String sheetName = SessionUtils.getEngineName(request);
        if (!SessionUtils.isSessionExists(response, username)
                || !SessionUtils.isActiveEngineExists(response, sheetName)) {
            return;
        }
        
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        
        CellStyleDTO newCellStyle = gson.fromJson(reader, CellStyleDTO.class);
        Engine engine = engineManager.getEngine(sheetName);
        
        synchronized (engine.getSheetEditLock()) {
            try {
                if (!engine.isPermitted(username)) {
                    ServletUtils.writeErrorResponse(response,
                            "You are not allowed to change cell styles",
                            HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                } else if (!engine.isInLatestVersion(username)) {
                    ServletUtils.writeErrorResponse(response,
                            "Unable to edit sheet while not in latest version",
                            HttpServletResponse.SC_FORBIDDEN);
                    return;
                } else {
                    engine.updateCellStyle(newCellStyle);
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
