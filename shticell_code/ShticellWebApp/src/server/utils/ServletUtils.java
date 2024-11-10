package server.utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.EngineManager;
import manager.UserManager;

import java.io.IOException;

public class ServletUtils {
	
	
	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";
	
	private static final Object userManagerLock = new Object();
	private static final Object engineManagerLock = new Object();
	
	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}
	
	public static EngineManager getEngineManager(ServletContext servletContext) {
		synchronized (engineManagerLock) {
			if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, new EngineManager());
			}
		}
		return (EngineManager) servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME);
	}
	
	public static void writeErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
		response.setStatus(statusCode);
		response.getWriter().println(message);
		response.getWriter().flush();
	}
	
	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return Integer.MIN_VALUE;
	}
}
