package servlets.range;

import com.google.gson.reflect.TypeToken;
import dto.sheet.ColoredSheetDTO;
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
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "sort range", urlPatterns = "/sortRange")
public class SortRangeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                    BufferedReader reader = request.getReader();
                    List<String> columnsToSortBy = Arrays.stream(Constants.GSON_INSTANCE.fromJson(reader, String[].class))
                            .toList();
                    Engine engine = engineManager.getEngine(engineName);
                    ColoredSheetDTO coloredSheetDTO = engine.sortRangeOfCells(rangeNameFromParam, columnsToSortBy);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().print(Constants.GSON_INSTANCE.toJson(coloredSheetDTO));
                    response.getWriter().flush();
                } catch (ClassCastException e) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().print("Cannot sort by non-numeric column");
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
