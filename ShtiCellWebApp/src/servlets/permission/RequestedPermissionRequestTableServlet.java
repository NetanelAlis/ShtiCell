package servlets.permission;

import dto.permission.RequestedRequestForTableDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import managers.EngineManager;
import utils.Constants;
import utils.ServletUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Permission", urlPatterns = "/refreshPermissionTable")
public class RequestedPermissionRequestTableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            Engine engine = engineManager.getEngine(request.getParameter(Constants.SHEETNAME));
            List<RequestedRequestForTableDTO> requestedRequestForTableDTOS = engine.getAllRequestsAsRequestedRequestForTableDTO();
            String json = Constants.GSON_INSTANCE.toJson(requestedRequestForTableDTOS);
            out.println(json);
            out.flush();
        }
    }
}
