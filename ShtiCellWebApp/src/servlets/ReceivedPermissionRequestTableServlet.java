package servlets;

import dto.ReceivedRequestDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.UserManager;
import user.User;
import utils.Constants;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;


@WebServlet(name = "Get Permission Requests Table Servlet", urlPatterns = "/refreshPermissionRequestTable")
public class ReceivedPermissionRequestTableServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            User user = userManager.getUser(SessionUtils.getUsername(request));
            Set<ReceivedRequestDTO> receivedRequestDTO = user.getAllRequestsAsReceivedRequestDTO();
            String json = Constants.GSON_INSTANCE.toJson(receivedRequestDTO);
            out.println(json);
            out.flush();
        }
    }
}
