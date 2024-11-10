package server.servlet.editor.filter;

import com.google.gson.Gson;
import server.constants.Constants;
import dto.returnable.EffectiveValueDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import manager.EngineManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Get Filterable Elements Servlet", urlPatterns = "/getFilterableElements")
public class GetFilterableElementsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        String sheetName = SessionUtils.getEngineName(request);
        if (!SessionUtils.isSessionExists(response, username)
                || !SessionUtils.isActiveEngineExists(response, sheetName)) {
            return;
        }
        
        Engine engine = engineManager.getEngine(sheetName);
        
        String rangeToFilter = request.getParameter(Constants.RANGE_NAME);
        if (rangeToFilter == null || rangeToFilter.isEmpty()) {
            ServletUtils.writeErrorResponse(response,
                    "No range boundaries were given", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        String columnToFilterBy = request.getParameter(Constants.COLUMN_NAME);
        if (columnToFilterBy == null || columnToFilterBy.isEmpty()) {
            ServletUtils.writeErrorResponse(response,
                    "No column to filter by was given", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try (PrintWriter out = response.getWriter()) {
            List<EffectiveValueDTO> uniqueItemsInColumn;
            try {
                uniqueItemsInColumn = engine.getUniqueItemsToFilterBy(columnToFilterBy, rangeToFilter, username);
                Gson gson = new Gson();
                String json = gson.toJson(uniqueItemsInColumn);
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                ServletUtils.writeErrorResponse(
                        response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
