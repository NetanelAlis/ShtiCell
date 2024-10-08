package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.Engine;
import utils.ServletUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

@WebServlet(name = "loadSheetFromFileServlet", urlPatterns = "/loadSheetFromFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadSheetFromFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/xml");
        Part filePart = request.getPart("file");  // "file" is the name of the form field
        String fileName = filePart.getSubmittedFileName();

        if (fileName.endsWith(".xml")) {
            fileName = fileName.substring(0, fileName.lastIndexOf(".xml"));
        }

        Engine engine =  ServletUtils.getEngine(getServletContext(), fileName);
        try{
        engine.loadDataFromInputStream(filePart.getInputStream());
        
    } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }
}