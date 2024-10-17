package servlets.permission;

import dto.permission.SendRequestDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import managers.EngineManager;
import utils.Constants;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "Request Sheet Permission Servlet", urlPatterns = "/requestPermission")
public class SheetPermissionRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String username = SessionUtils.getUsername(request);
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());

        if(SessionUtils.userExistInSession(response, username)){
            return;
        }

        BufferedReader reader = request.getReader();
        SendRequestDTO sendRequestDTO = Constants.GSON_INSTANCE.fromJson(reader, SendRequestDTO.class);

            Engine engine = engineManager.getEngine(sendRequestDTO.getSheetName());
            if(engine == null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Sheet with the name [" + sendRequestDTO.getSheetName() + "] does not exist");
                response.getWriter().flush();
            }

            try{
                engine.createPermissionRequest(sendRequestDTO.getRequestedPermission(), username);
                response.setStatus(HttpServletResponse.SC_OK);

        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
            response.getWriter().flush();
        }
    }
}