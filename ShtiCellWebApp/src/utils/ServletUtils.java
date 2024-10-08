package utils;

import jakarta.servlet.ServletContext;
import logic.Engine;

public class ServletUtils {

	private static final Object engineLock = new Object();

	public static Boolean isEngineExist(ServletContext servletContext, Engine engine ,String sheetName) {
		boolean isEngineExist = false;

		synchronized (engineLock) {
				if (servletContext.getAttribute(sheetName) == null) {
					servletContext.setAttribute(sheetName, engine);
					isEngineExist = true;
				}
			}

			return isEngineExist;
		}
	}
