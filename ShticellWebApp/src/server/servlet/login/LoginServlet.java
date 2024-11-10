package server.servlet.login;

import server.constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.UserManager;
import server.utils.ServletUtils;
import server.utils.SessionUtils;
import user.User;

import java.io.IOException;

import static server.constants.Constants.USERNAME;

@WebServlet(name = "Login Servlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        
        if (usernameFromSession == null) { //user is not logged in yet
            
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict
                
                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        // stands for unauthorized as there is already such user with this name
                        ServletUtils.writeErrorResponse(response,
                                "Username " + usernameFromParameter +
                                        " already exists. Please enter a different username.",
                                HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter, new User(usernameFromParameter));
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                        
                        //redirect the request to the chat room - in order to actually change the URL
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
