package utils;

import jakarta.servlet.ServletContext;
import managers.EngineManager;
import managers.UserManager;

public class ServletUtils {

	private static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";
	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

	private static final Object engineManagerLock = new Object();
	private static final Object userManagerLock = new Object();

	public static EngineManager getEngineManager(ServletContext servletContext) {

		synchronized (engineManagerLock) {
			if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, new EngineManager());
			}
		}
		return (EngineManager) servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME);
	}

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}
}