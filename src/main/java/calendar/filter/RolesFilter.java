package calendar.filter;

import calendar.entities.Event;
import calendar.entities.MutableHttpServletRequest;
import calendar.entities.User;
import calendar.enums.UserRole;
import calendar.services.AuthService;
import calendar.services.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RolesFilter implements Filter {
    public static final Logger logger = LogManager.getLogger(RolesFilter.class);
    private final EventService eventService;
    private final AuthService authService;
    private Map<String, List<UserRole>> permissionsMap;
    public RolesFilter(EventService eventService, AuthService authService) {
        permissionsMap = new HashMap<>();
        permissionsMap.put("inviteGuest", Arrays.asList(UserRole.ORGANIZER,UserRole.ADMIN));
        permissionsMap.put("makeAdmin", Arrays.asList(UserRole.ORGANIZER));
        permissionsMap.put("update",Arrays.asList(UserRole.ORGANIZER,UserRole.ADMIN));
        permissionsMap.put("delete",Arrays.asList(UserRole.ORGANIZER));
        permissionsMap.put("removeGuest",Arrays.asList(UserRole.ORGANIZER,UserRole.ADMIN));

        this.eventService = eventService;
        this.authService = authService;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
    public UserRole getUserRole(MutableHttpServletRequest request){
        Long eventId = Long.valueOf((String) request.getAttribute("eventId"));
        User user = (User) request.getAttribute("user");
        logger.info(eventId);
        logger.info(user);
        if(eventId==null || user==null) return null;
        return eventService.getUserRole(user,  eventId);
    }
    public void updateFilter(MutableHttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws IOException, ServletException {
        Long eventId = Long.valueOf((String) request.getAttribute("eventId"));
        String title = request.getHeader("title");
        Integer duration = Integer.valueOf(request.getHeader("duration"));
        String date = request.getHeader("time");
        Event event = eventService.fetchEventById(eventId);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        if(!title.equals(event.getTitle()) || duration != event.getDuration() || !date.equals(event.getDateTime().format(format))){
            returnBadResponse(response,"Admin can't edit event's title, duration or event's date");
        } else {
            filterChain.doFilter(request, response);
        }
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("In roleFilter doFilter");
        MutableHttpServletRequest req = new MutableHttpServletRequest((HttpServletRequest) servletRequest);
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String path = ((HttpServletRequest) servletRequest).getServletPath();
        if (path.startsWith("/event/")) {
            String operation = path.split("/")[2];
            List<UserRole> roles = permissionsMap.get(operation);
            if (roles != null) {
                if(operation.equals("update") && getUserRole(req).equals(UserRole.ADMIN)) {updateFilter(req,res,filterChain);}
                else if (!roles.contains(getUserRole(req))) {
                    returnBadResponse(res, "The user have no permissions to do this operation");
                } else filterChain.doFilter(req, res);
            } else filterChain.doFilter(req, res);
        } else filterChain.doFilter(req, res);
    }
/*
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
                //String body = (String) req.getAttribute("body");
                //JSONObject jsonObject = new JSONObject(body);
                Event event = eventService.fetchEventById(eventId);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formatDateTime = event.getDateTime().format(format);
//                if(!jsonObject.get("title").equals(event.getTitle()) || jsonObject.getInt("duration") != event.getDuration() || !formatDateTime.equals(event.getDateTime().format(format))){
//                    logger.info("No update permission for the admin");
//                    returnBadResponse(res,"Admin can't edit title,duration,date and time");
//                } else {
//                    filterChain.doFilter(req, res);
//                }
            } else {
                returnBadResponse(res,"There is no update permissions for who is not ORGANIZER or ADMIN!");

            }
        }
        //check the permission for the invite guest feature
        if (((HttpServletRequest) servletRequest).getServletPath().startsWith("/event/inviteGuest")){
            String token = req.getHeader("token");
            Long eventId = Long.valueOf(req.getHeader("eventId"));
            logger.info("User with token: "+token+" want to update event with id: "+eventId);
            User user = authService.getCachedUser(token);
            UserRole userRole = eventService.getUserRole(user,eventId);
            if(userRole==UserRole.ORGANIZER || userRole==UserRole.ADMIN){
                filterChain.doFilter(req, res);
            }
            else {
                returnBadResponse(res,"Only Organizer and Admins can invite guests");
            }
        }

        //check the permission for the make admin feature
        if (((HttpServletRequest) servletRequest).getServletPath().startsWith("/event/makeAdmin")){
            String token = req.getHeader("token");
            Long eventId = Long.valueOf(req.getHeader("eventId"));
            User user = authService.getCachedUser(token);
            UserRole userRole = eventService.getUserRole(user,eventId);
            if(userRole==UserRole.ORGANIZER){
                filterChain.doFilter(req, res);
            }
            else {
                returnBadResponse(res,"Only Organizer can make admin");
            }
        }
        //check the permission for the delete event feature
        if (((HttpServletRequest) servletRequest).getServletPath().startsWith("/event/delete")){
            String token = req.getHeader("token");
            Long eventId = Long.valueOf(req.getHeader("eventId"));
            User user = authService.getCachedUser(token);
            UserRole userRole = eventService.getUserRole(user,eventId);
            if(userRole==UserRole.ORGANIZER){
                filterChain.doFilter(req, res);
            }
            else {
                returnBadResponse(res,"Only Organizer can delete the event");
            }
        }
        filterChain.doFilter(req, res);
*/

    private void returnBadResponse(HttpServletResponse res,String errorMessage) throws IOException {
        res.sendError(401, errorMessage);
    }
}
