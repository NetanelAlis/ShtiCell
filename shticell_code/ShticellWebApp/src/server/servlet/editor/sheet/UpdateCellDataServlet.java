package server.servlet.editor.sheet;

import com.google.gson.Gson;
import server.constants.Constants;
import dto.sheet.SheetAndCellDTO;
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

@WebServlet(name = "Update Cell Data Servlet", urlPatterns = "/updateCellData")
public class UpdateCellDataServlet extends HttpServlet {
    
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
            String cellID = request.getParameter(Constants.CELL_ID);
            String newOriginalValue = request.getParameter(Constants.NEW_ORIGINAL_VALUE);
            
            if (cellID == null || cellID.isEmpty()) {
                ServletUtils.writeErrorResponse(
                        response, "Got No Cell ID", HttpServletResponse.SC_BAD_REQUEST);
            }
            
            if (newOriginalValue == null) {
                ServletUtils.writeErrorResponse(
                        response, "Got No original value", HttpServletResponse.SC_BAD_REQUEST);
            }
            SheetAndCellDTO sheetAndCellData;
            
            synchronized (engine.getSheetEditLock()) {
                try {
                    if (!engine.isPermitted(username)) {
                        ServletUtils.writeErrorResponse(response,
                                "You are not allowed to update cells",
                                HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    } else if (!engine.isInLatestVersion(username)) {
                        ServletUtils.writeErrorResponse(response,
                                "Unable to edit sheet while not in latest version",
                                HttpServletResponse.SC_FORBIDDEN);
                        return;
                    } else {
                        engine.updateSingleCellData(cellID, newOriginalValue, username);
                        
                        sheetAndCellData = new SheetAndCellDTO(engine.getColoredSheetAsDTO(username),
                                        engine.getSingleCellData(cellID, username));
                    }
                } catch (RuntimeException e) {
                    ServletUtils.writeErrorResponse(
                            response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
            
            Gson gson = new Gson();
            String json = gson.toJson(sheetAndCellData);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
