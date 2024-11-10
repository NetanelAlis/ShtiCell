package server.servlet.editor.version;

import com.google.gson.Gson;
import dto.sheet.SheetAndRangesDTO;
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

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Load Sheet Version Servlet", urlPatterns = "/loadSheetVersion")
public class LoadSheetVersionServlet extends HttpServlet {
    
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
        
        String version = request.getParameter(Constants.VERSION);
        try (PrintWriter out = response.getWriter()) {
            Engine engine = engineManager.getEngine(sheetName);
            
            if (version == null || version.isEmpty()) {
                ServletUtils.writeErrorResponse(
                        response, "Invalid version", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            
            SheetAndRangesDTO coloredSheet = engine.getSheetVersionAsDTO(Integer.parseInt(version), username);
            
            Gson gson = new Gson();
            String json = gson.toJson(coloredSheet);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            ServletUtils.writeErrorResponse(response,
                    "Given version [" + version + "] is not a valid version number",
                    HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            ServletUtils.writeErrorResponse(
                    response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
