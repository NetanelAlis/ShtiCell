package servlets.sheet;

import dto.version.EditorRefresherAnswerDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import managers.EngineManager;
import utils.Constants;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;

@WebServlet(name = "Editor Refresher", urlPatterns = "/refreshEditor")
public class EditorRefresherServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String userName = SessionUtils.getUsername(request);
        String engineName = SessionUtils.getEngineName(request);

        if (SessionUtils.userExistInSession(response, userName) || SessionUtils.engineExistInSession(response, engineName)
        ) {
            return;
        }

        try{
        Engine engine = engineManager.getEngine(engineName);
        int userActiveVersion = engine.getUserActiveVersion(userName);
        boolean userCantEditTheSheet = engine.isUserCantEditTheSheet(userActiveVersion, userName);
        boolean userNotOnLastVersion = !engine.isInLastVersion(userName);
        EditorRefresherAnswerDTO editorRefresherAnswerDTO = new EditorRefresherAnswerDTO(userCantEditTheSheet, userNotOnLastVersion);

        String json =  Constants.GSON_INSTANCE.toJson(editorRefresherAnswerDTO);
        response.getWriter().print(json);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();

    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print(e.getMessage());
        response.getWriter().flush();
        }
    }
}