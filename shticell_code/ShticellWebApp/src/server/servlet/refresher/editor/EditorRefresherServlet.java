package server.servlet.refresher.editor;

import com.google.gson.Gson;
import dto.version.EditorRefresherAnswerDTO;
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

@WebServlet(name = "Editor Refresher Servlet", urlPatterns = "/refreshEditor")
public class EditorRefresherServlet extends HttpServlet {
    
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
            String json = "";
            try {
                Gson gson = new Gson();
                Engine engine = engineManager.getEngine(sheetName);
                
                EditorRefresherAnswerDTO refresherAnswer = new EditorRefresherAnswerDTO(
                        engine.isUserCannotEdit(engine.getUsersActiveVersion(username), username),
                        engine.shouldNotifyUser(username),
                        engine.showVersions().getVersionChanges().size());
                
                json = gson.toJson(refresherAnswer);
                
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                ServletUtils.writeErrorResponse(response, "return boolean " + json, HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
