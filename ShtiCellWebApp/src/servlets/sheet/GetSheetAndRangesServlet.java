package servlets.sheet;

import dto.sheet.SheetAndRangesDTO;
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

@WebServlet(name = "Get Sheet And Ranges Servlet", urlPatterns = "/getSheetAndRanges")
public class GetSheetAndRangesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String userName = SessionUtils.getUsername(request);
        String engineName = SessionUtils.getEngineName(request);

        if (SessionUtils.userExistInSession(response, userName) || SessionUtils.engineExistInSession(response, engineName)
        ) {
            return;
        }

        Engine engine = engineManager.getEngine(engineName);

        if (engine == null) {
            response.getWriter().println("No such Sheet with the Name " + engineName);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().flush();

        }
        engine.updateUserActiveSheetVersion(userName);
        SheetAndRangesDTO sheetAndRangesDTO = new SheetAndRangesDTO(engine.getColoredSheetDTO(userName), engine.getAllRanges(userName),
                !engine.isPermittedToWrite(userName));
        response.getWriter().println(Constants.GSON_INSTANCE.toJson(sheetAndRangesDTO));
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
    }
}
