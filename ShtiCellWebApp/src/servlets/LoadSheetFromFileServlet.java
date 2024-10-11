package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.EngineManager;
import logic.engine.Engine;
import logic.engine.EngineImpl;
import utils.Constants;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "Load Sheet Servlet", urlPatterns = "/loadSheetFromFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadSheetFromFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        Part filePart = request.getPart("file");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        String userNameParam = request.getParameter(Constants.USERNAME);
        request.getSession(true).setAttribute(Constants.USERNAME, userNameParam);
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("User is not logged in");
            response.getWriter().flush();
            return;
        }

        try{
            boolean engineExists = false;
            Engine engine = new EngineImpl(username);
            engine.loadDataFromInputStream(filePart.getInputStream());
            String sheetName = engine.getSheetMetaDataDTO(username).getSheetName();
            synchronized(this) {
                engineExists = engineManager.isEngineExists(sheetName);
                if (engineExists) {
                    throw new IllegalArgumentException(("Sheet with the name [" + engine.getSheetName() + "] already exists"));
                }
                else {
                    engineManager.addEngine(sheetName, engine);
                }
            }
                Gson gson = new Gson();
                String json = gson.toJson(engine.getSheetMetaDataDTO(username));
                response.getWriter().println(json);
                response.getWriter().close();
                response.flushBuffer();
                response.setStatus(HttpServletResponse.SC_OK);

         } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }
}