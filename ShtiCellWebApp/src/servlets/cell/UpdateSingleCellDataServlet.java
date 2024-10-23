package servlets.cell;

import dto.cell.CellDTO;
import dto.sheet.ColoredSheetDTO;
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
                Engine engine = engineManager.getEngine(engineName);
                CellDTO cellDTO;
                ColoredSheetDTO coloredSheetDTO;
                SheetAndCellDTO sheetAndCellDTO;

                synchronized (engine.getSheetEditLock()) {
                    try {
                        if (!engine.isPermittedToWrite(username)) {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().println("You are not allowed to update this cells");
                            response.getWriter().flush();
                            return;
                        } else if (!engine.isInLastVersion(username)) {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().println("Unable to edit sheet while not in latest version");
                            response.getWriter().flush();
                            return;
                        } else {
                            engine.updateSingleCellData(cellNameFromParameter, newCellValue, username);
                             cellDTO = engine.getSingleCellData(cellNameFromParameter, username);
                             coloredSheetDTO = engine.getColoredSheetDTO(username);
                             sheetAndCellDTO = new SheetAndCellDTO(coloredSheetDTO, cellDTO);
                        }
                    } catch (RuntimeException e) {
                        ServletUtils.WriteBadRequestResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                }
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().print(Constants.GSON_INSTANCE.toJson(sheetAndCellDTO));
                response.getWriter().flush();
            }
        }
    }
}
