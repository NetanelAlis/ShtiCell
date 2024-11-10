package server.servlet.permission;

import com.google.gson.Gson;
import dto.permission.SentPermissionRequestDTO;
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

@WebServlet(name = "Send Permission Request Servlet", urlPatterns = "/sendPermissionRequest")
public class SendPermissionRequestServlet  extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
        
        String username = SessionUtils.getUsername(request);
        if (!SessionUtils.isSessionExists(response, username)) {
            return;
        }
        
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        
        SentPermissionRequestDTO requestToSend = gson.fromJson(reader, SentPermissionRequestDTO.class);
        Engine engine = engineManager.getEngines().get(requestToSend.getRequestedEngineName());
        if (engine == null) {
            ServletUtils.writeErrorResponse(response,
                    "No Sheet found for name " + requestToSend.getRequestedEngineName(),
                    HttpServletResponse.SC_BAD_REQUEST);
        } else {
            try {
                engine.createNewPermissionRequest(requestToSend, username);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (RuntimeException e) {
                ServletUtils.writeErrorResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
