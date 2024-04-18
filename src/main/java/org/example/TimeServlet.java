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

import org.thymeleaf.context.WebContext;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String timeZoneParam = req.getParameter("timezone");
        ZoneId zoneId;
        if (timeZoneParam != null && !timeZoneParam.isEmpty()) {
            zoneId = ZoneId.of(timeZoneParam);
        } else {
            zoneId = ZoneId.of("UTC");
        }
        LocalDateTime currentTime = LocalDateTime.now(zoneId);
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));


        WebContext context = new WebContext(req, resp, getServletContext(), req.getLocale());
        context.setVariable("timeZone", zoneId.getId());
        context.setVariable("formattedTime", formattedTime);
        templateEngine.process("time", context, resp.getWriter());


    }
}
