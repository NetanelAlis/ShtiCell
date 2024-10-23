package servlets.commands.filter;

import dto.returnable.EffectiveValueDTO;
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
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "filter range", urlPatterns = "/filterRange")
public class FilterRange extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        String engineName = SessionUtils.getEngineName(request);

        String rangeNameFromParam = request.getParameter(Constants.RANGE_NAME);
        String columnToFilterByFromParam = request.getParameter(Constants.COLUMN_TO_FILTER_BY);

        if (rangeNameFromParam == null || rangeNameFromParam.isEmpty() ||
                columnToFilterByFromParam == null || columnToFilterByFromParam.isEmpty()){
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
                    List<Integer> columnsToSortBy = Arrays.stream(Constants.GSON_INSTANCE.fromJson(reader, Integer[].class))
                            .toList();
                    if(columnsToSortBy.isEmpty()){
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        response.getWriter().print("Must select at least one item");
                        response.getWriter().flush();
                        return;
                    }
                    Engine engine = engineManager.getEngine(engineName);
                    ColoredSheetDTO coloredSheetDTO =  engine.filterRangeOfCells(rangeNameFromParam, columnToFilterByFromParam, columnsToSortBy, username);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().print(Constants.GSON_INSTANCE.toJson(coloredSheetDTO));
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
