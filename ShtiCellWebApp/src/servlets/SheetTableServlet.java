package servlets;

import com.google.gson.Gson;
import dto.SheetMetaDataDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.EngineManager;
import logic.engine.Engine;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;

@WebServlet(name = "Get Sheet Table Servlet", urlPatterns = "/refreshSheetTable")
public class SheetTableServlet  extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            Set<Engine> engineSet = engineManager.getEngines();
            Set<SheetMetaDataDTO> sheetMetaDataDTO = new LinkedHashSet<>();
            engineSet.forEach(engine -> sheetMetaDataDTO.add(engine.getSheetMetaDataDTO(SessionUtils.getUsername(request))));
            String json = gson.toJson(sheetMetaDataDTO);
            out.println(json);
            out.flush();
        }
    }
}
