package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
      HttpSession session = request.getSession(false);
      Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

   public static void clearSession (HttpServletRequest request) {
       request.getSession().invalidate();
   }

   public static Boolean isSessionExists (HttpServletResponse response, String username) throws IOException {
        boolean sessionExists = false;

       if (username == null) {
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           response.getWriter().println("User is not logged in");
           response.getWriter().flush();
           sessionExists = true;
       }

       return sessionExists;

   }
}