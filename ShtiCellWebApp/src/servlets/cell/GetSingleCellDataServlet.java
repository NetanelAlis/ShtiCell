package servlets.cell;

import dto.cell.CellDTO;
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

@WebServlet(name = "get Cell data ", urlPatterns = "/getCell")
public class GetSingleCellDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        String engineName = SessionUtils.getEngineName(request);

        if (SessionUtils.userExistInSession(response, username) || SessionUtils.engineExistInSession(response, engineName)) {
            return;
        }

        String cellNameFromParameter = request.getParameter(Constants.CELL_ID);
        if (cellNameFromParameter == null || cellNameFromParameter.isEmpty()) {
            //no cell in query or empty cell not standard situation. it's a conflict

            // stands for conflict in server state
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            if (!engineManager.isEngineExists(engineName)) {
                String errorMessage = "sheet name: " + engineName + " dosnt exists.";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(errorMessage);
                response.getWriter().flush();
            }else if(!userManager.isUserExists(username)){
                String errorMessage = "user name: " + username + " dosnt exists.";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(errorMessage);
            } else{
                Engine engine = engineManager.getEngine(engineName);
                CellDTO cellDTO = engine.getSingleCellData(cellNameFromParameter, username);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().print(Constants.GSON_INSTANCE.toJson(cellDTO));
                response.getWriter().flush();
            }
        }
    }
}