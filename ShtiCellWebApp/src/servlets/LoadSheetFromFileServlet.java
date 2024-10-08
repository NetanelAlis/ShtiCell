package servlets;

import com.google.gson.Gson;
import dto.SheetDTO;
import dto.SheetNameAndSizeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.Engine;
import logic.EngineImpl;
import utils.ServletUtils;
import java.io.IOException;

@WebServlet(name = "loadSheetFromFileServlet", urlPatterns = "/loadSheetFromFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadSheetFromFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/xml");
        Part filePart = request.getPart("file");  // "file" is the name of the form field
        try{
            Engine engine = new EngineImpl();
            engine.loadDataFromInputStream(filePart.getInputStream());
            String sheetName = engine.getSheetNameAndSizeAsDTO().getSheetName();
            if(ServletUtils.isEngineExist(getServletContext(), engine, sheetName)){
                Gson gson = new Gson();
                String json = gson.toJson(engine.getSheetNameAndSizeAsDTO());
                response.getWriter().println(json);
                response.getWriter().close();
                response.flushBuffer();
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Sheet with the name " + sheetName + " already exists");
            }

         } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }
}