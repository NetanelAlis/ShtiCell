package servlets.cell;

import dto.cell.CellStyleDTO;
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
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "update Cell style ", urlPatterns = "/updateCellStyle")
public class UpdateCellStyleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        String engineName = SessionUtils.getEngineName(request);

        if (SessionUtils.userExistInSession(response, username) || SessionUtils.engineExistInSession(response, engineName)) {
            return;
        }
        else if (!engineManager.isEngineExists(engineName)) {
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
                    BufferedReader reader = request.getReader();
                    CellStyleDTO cellStyleDTO = Constants.GSON_INSTANCE.fromJson(reader, CellStyleDTO.class);
                    Engine engine = engineManager.getEngine(engineName);
                    engine.updateCellStyle(cellStyleDTO);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().flush();
                } catch (RuntimeException e) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().print(e.getMessage());
                    response.getWriter().flush();
                }
            }
        }
    }
