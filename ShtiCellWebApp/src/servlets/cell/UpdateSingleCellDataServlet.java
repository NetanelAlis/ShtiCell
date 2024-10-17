package servlets.cell;

import dto.cell.CellDTO;
import dto.sheet.SheetAndCellDTO;
import dto.sheet.SheetDTO;
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

@WebServlet(name = "update Cell data ", urlPatterns = "/updateCellData")
public class UpdateSingleCellDataServlet extends HttpServlet {

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
        String newCellValue = request.getParameter(Constants.NEW_VALUE);
        if (cellNameFromParameter == null || cellNameFromParameter.isEmpty() ||
                newCellValue == null || newCellValue.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
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
                    engine.updateSingleCellData(cellNameFromParameter, newCellValue);
                    CellDTO cellDTO = engine.getSingleCellData(cellNameFromParameter);
                    SheetDTO sheetDTO = engine.getSheetAsDTO();
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().print(Constants.GSON_INSTANCE.toJson(new SheetAndCellDTO(sheetDTO, cellDTO)));
                    response.getWriter().flush();
                } catch (RuntimeException e) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().print(e.getMessage());
                    response.getWriter().flush();
                }
            }
        }
    }
}
