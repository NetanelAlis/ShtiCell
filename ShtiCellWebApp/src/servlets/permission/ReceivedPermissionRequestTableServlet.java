package servlets.permission;

import dto.permission.ReceivedRequestForTableDTO;
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
import java.util.List;


@WebServlet(name = "Get Received Permission Requests Table Servlet", urlPatterns = "/refreshReceivedPermissionRequestTable")
public class ReceivedPermissionRequestTableServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            User user = userManager.getUser(SessionUtils.getUsername(request));
            List<ReceivedRequestForTableDTO> receivedRequestForTableDTO = user.getAllRequestsAsReceivedRequestDTO();
            String json = Constants.GSON_INSTANCE.toJson(receivedRequestForTableDTO);
            out.println(json);
            out.flush();
        }
    }
}
