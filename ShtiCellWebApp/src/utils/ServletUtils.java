package utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import logic.Engine;
import logic.EngineImpl;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object engineLock = new Object();
	public static Engine getEngine(ServletContext servletContext, String engineName) {

		synchronized (engineLock) {
			if (servletContext.getAttribute(engineName) == null) {
				servletContext.setAttribute(engineName, new EngineImpl());
			}
		}
		return (Engine) servletContext.getAttribute(engineName);
	}
//
//	public static ChatManager getChatManager(ServletContext servletContext) {
//		synchronized (chatManagerLock) {
//			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
//			}
//		}
//		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
//	}
//
//	public static int getIntParameter(HttpServletRequest request, String name) {
//		String value = request.getParameter(name);
//		if (value != null) {
//			try {
//				return Integer.parseInt(value);
//			} catch (NumberFormatException numberFormatException) {
//			}
//		}
//		return INT_PARAMETER_ERROR;
//	}
}
