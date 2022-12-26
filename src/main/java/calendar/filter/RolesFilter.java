package calendar.filter;

import calendar.DTO.UpdateEventDTO;
import calendar.entities.Event;
import calendar.entities.MutableHttpServletRequest;
import calendar.entities.User;
import calendar.enums.UserRole;
import calendar.services.AuthService;
import calendar.services.EventService;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class RolesFilter implements Filter {
    public static final Logger logger = LogManager.getLogger(RolesFilter.class);
    private final EventService eventService;
    private final AuthService authService;

    public RolesFilter(EventService eventService, AuthService authService) {
        this.eventService = eventService;
        this.authService = authService;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("In roleFilter doFilter");
        MutableHttpServletRequest req = new MutableHttpServletRequest((HttpServletRequest) servletRequest);
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        //check the permission for the update feature
        if (((HttpServletRequest) servletRequest).getServletPath().startsWith("/event/update")){
            String token = req.getHeader("token");
            Long eventId = Long.valueOf(req.getHeader("eventId"));
            logger.info("User with token: "+token+" want to update event with id: "+eventId);
            User user = authService.getCachedUser(token);
            UserRole userRole = eventService.getUserRole(user,eventId);
            if(userRole==UserRole.ORGANIZER){
                filterChain.doFilter(req, res);
            } else if (userRole==UserRole.ADMIN) {
                String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                JSONObject jsonObject = new JSONObject(body);
                Event event = eventService.fetchEventById(eventId);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formatDateTime = event.getDateTime().format(format);
                if(!jsonObject.get("title").equals(event.getTitle()) || jsonObject.getInt("duration") != event.getDuration() || !formatDateTime.equals(event.getDateTime().format(format))){
                    logger.info("No update permission for the admin");
                    returnBadResponse(res);
                } else {
                    filterChain.doFilter(req, res);
                }
            } else {
                returnBadResponse(res);

            }
        }
        filterChain.doFilter(req, res);

    }
    private void returnBadResponse(HttpServletResponse res) throws IOException {
        res.sendError(401, "There is no update permissions");
    }
}
