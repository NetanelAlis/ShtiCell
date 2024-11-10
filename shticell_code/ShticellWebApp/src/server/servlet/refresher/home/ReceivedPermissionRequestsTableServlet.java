package server.servlet.refresher.home;

import com.google.gson.Gson;
import dto.permission.ReceivedPermissionRequestDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.UserManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;
import user.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Refresh Permission Requests Table Servlet", urlPatterns = "/refreshPermissionRequestsTable")
public class ReceivedPermissionRequestsTableServlet  extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        
        String userName = SessionUtils.getUsername(request);
        if (!SessionUtils.isSessionExists(response, userName)) {
            return;
        }
        
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            User owner = userManager.getUser(userName);
            List<ReceivedPermissionRequestDTO> receivedPermissionRequestsDTOSet = owner.getPermissionRequests();
            
            String json = gson.toJson(receivedPermissionRequestsDTOSet);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
