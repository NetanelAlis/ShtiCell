package server.servlet.editor.sort;

import com.google.gson.Gson;
import server.constants.Constants;
import dto.sheet.ColoredSheetDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import manager.EngineManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "Sort Range Servlet", urlPatterns = "/sortRange")
public class SortRangeServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        String sheetName = SessionUtils.getEngineName(request);
        if (!SessionUtils.isSessionExists(response, username)
                || !SessionUtils.isActiveEngineExists(response, sheetName)) {
            return;
        }
        
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        
        Engine engine = engineManager.getEngine(sheetName);
        List<String> columnsToSortBy = Arrays.stream(gson.fromJson(reader, String[].class)).toList();
        String rangeToSort = request.getParameter(Constants.RANGE_NAME);
        
        if (rangeToSort == null || rangeToSort.isEmpty()) {
            ServletUtils.writeErrorResponse(response,
                    "No range boundaries to sort were given",
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try (PrintWriter out = response.getWriter()) {
            ColoredSheetDTO sortedSheet;
            try {
                sortedSheet = engine.sortRangeOfCells(rangeToSort, columnsToSortBy, username);
                String json = gson.toJson(sortedSheet);
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (ClassCastException e) {
                ServletUtils.writeErrorResponse(response,
                        "Cannot sort by non-numeric column",
                        HttpServletResponse.SC_BAD_REQUEST);
            } catch (Exception e) {
                ServletUtils.writeErrorResponse(
                        response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
                
            }
        }
    }
}
