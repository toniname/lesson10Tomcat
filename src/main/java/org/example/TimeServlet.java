package org.example;

import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import org.thymeleaf.templateresolver.FileTemplateResolver;


@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(TimeServlet.class.getName());
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        templateEngine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("./templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        try {
            String timeZoneParam = req.getParameter("timezone");
            ZoneId zoneId;
            if (timeZoneParam != null && !timeZoneParam.isEmpty()) {
                zoneId = ZoneId.of(timeZoneParam);
            } else {
                zoneId = ZoneId.of("UTC");
            }
            LocalDateTime currentTime = LocalDateTime.now(zoneId);
            String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));

            String htmlResponse = "<html><head><title>Current Time</title></head><body>"
                    + "<h1>Current Time (UTC)</h1>"
                    + "<p>" + formattedTime + "</p>"
                    + "</body></html>";

            resp.getWriter().write(htmlResponse);


        } catch (IOException e) {
            logger.severe("IOException occurred while handling request: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred while processing request.");
            resp.getWriter().close();
        }
    }
}
