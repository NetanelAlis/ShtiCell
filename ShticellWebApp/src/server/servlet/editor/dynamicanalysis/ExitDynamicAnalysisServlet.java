package server.servlet.editor.dynamicanalysis;

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

@WebServlet(name = "Exit Dynamic Analysis Servlet", urlPatterns = "/exitDynamicAnalysis")
public class ExitDynamicAnalysisServlet extends HttpServlet {
    
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
            
            try {
                engine.finishDynamicAnalysis(username);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                ServletUtils.writeErrorResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
