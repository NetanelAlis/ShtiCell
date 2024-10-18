package servlets.commands.filter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import managers.EngineManager;
import managers.UserManager;
import utils.Constants;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "get columns to filter by", urlPatterns = "/getColumnsToFilterBy")
public class GetColumnsToFilterByServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        String engineName = SessionUtils.getEngineName(request);

        String rangeNameFromParam = request.getParameter(Constants.RANGE_NAME);

        if (rangeNameFromParam == null || rangeNameFromParam.isEmpty()){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }else if(SessionUtils.userExistInSession(response, username) ||
                SessionUtils.engineExistInSession(response, engineName)) {
            return;
        } else {
            if (!engineManager.isEngineExists(engineName)) {
                String errorMessage = "sheet name: " + engineName + " dosnt exists.";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(errorMessage);
                response.getWriter().flush();
            } else if (!userManager.isUserExists(username)) {
                String errorMessage = "user name: " + username + " dosnt exists.";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(errorMessage);
                response.getWriter().flush();
            } else {
                try {
                    Engine engine = engineManager.getEngine(engineName);
                    List<String> columnsToFilterBy = engine.getColumnsListOfRange(rangeNameFromParam);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().print(Constants.GSON_INSTANCE.toJson(columnsToFilterBy));
                    response.getWriter().flush();
                } catch (RuntimeException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().print(e.getMessage());
                    response.getWriter().flush();
                }
            }
        }
    }
}
