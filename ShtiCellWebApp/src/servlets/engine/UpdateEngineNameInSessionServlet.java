package servlets.engine;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.EngineManager;
import utils.Constants;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;

@WebServlet(name = "Update Engine Name In Session Servlet", urlPatterns = "/updateEngineNameInSession")
public class UpdateEngineNameInSessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String username = SessionUtils.getUsername(request);

        if(SessionUtils.userExistInSession(response, username)) {
            return;
        }

        String engineNameFromParameter = request.getParameter(Constants.SHEET_NAME);
        if (engineNameFromParameter == null || engineNameFromParameter.isEmpty()) {
                //no engineName in session and no engineName in parameter - not standard situation. it's a conflict

                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                    if (!engineManager.isEngineExists(engineNameFromParameter)) {
                        String errorMessage = "sheet name: " + engineNameFromParameter + " dosnt exists.";
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().print(errorMessage);
                        response.getWriter().flush();
                    }
                    else {
                        request.getSession(false).setAttribute(Constants.SHEET_NAME, engineNameFromParameter);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
            }
        }
    }