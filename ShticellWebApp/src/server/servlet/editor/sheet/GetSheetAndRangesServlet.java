package server.servlet.editor.sheet;

import com.google.gson.Gson;
import dto.sheet.SheetAndRangesDTO;
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

@WebServlet(name = "Get Sheet And Ranges Servlet", urlPatterns = "/getSheetAndRanges")
public class GetSheetAndRangesServlet extends HttpServlet {
    
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
            
            engine.updateUserActiveVersion(username);
            SheetAndRangesDTO sheetAndRanges =
                    new SheetAndRangesDTO(
                            engine.getColoredSheetAsDTO(username),
                            engine.getAllRanges(username),
                            !engine.isPermitted(username));
            
            Gson gson = new Gson();
            String json = gson.toJson(sheetAndRanges);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
