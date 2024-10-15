package servlets.permission;

import dto.permission.ReceivedRequestForTableDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.Engine;
import managers.EngineManager;
import managers.UserManager;
import user.User;
import utils.Constants;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "Post Response For Permission Requests Servlet", urlPatterns = "/responseToPermissionRequest")
    public class ResponseToPermissionRequestServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            BufferedReader reader = request.getReader();
            EngineManager engineManager = ServletUtils.getEngineManager(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String sheetOwnerName = SessionUtils.getUsername(request);

            if(SessionUtils.isSessionExists(response, sheetOwnerName)){
                return;
            }

            ReceivedRequestForTableDTO ownerResponseForTheRequest = Constants.GSON_INSTANCE.fromJson(reader, ReceivedRequestForTableDTO.class);
            Engine engine = engineManager.getEngine(ownerResponseForTheRequest.getSheetName());
            User requester = userManager.getUser(ownerResponseForTheRequest.getRequesterUserName());
            User sheetOwner = userManager.getUser(sheetOwnerName);
            String requestAnswer = request.getParameter(Constants.REQUEST_APPROVED).trim().toLowerCase();

            if(!requestAnswer.equals("true") && !requestAnswer.equals("false")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid request answer");
                response.getWriter().flush();
            }

            if(engine == null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Sheet with the name [" + ownerResponseForTheRequest.getSheetName() + "] does not exist");
                response.getWriter().flush();
            }

            if(requester == null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("user with the name [" + ownerResponseForTheRequest.getRequesterUserName() + "] does not exist");
                response.getWriter().flush();
            }

            try{
                sheetOwner.removeRequestForSheetPermission(ownerResponseForTheRequest.getSheetName(), ownerResponseForTheRequest.getRequestNumber());
                engine.updatePermissionStatus(ownerResponseForTheRequest.getRequesterUserName(),
                        ownerResponseForTheRequest.getRequestedPermission(), requestAnswer.equals("true"), ownerResponseForTheRequest.getRequestNumber());
            } catch (RuntimeException e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(e.getMessage());
                response.getWriter().flush();

            }
        }

}
