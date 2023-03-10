package calendar.filter;


import calendar.services.AuthService;
import calendar.services.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    public static final Logger logger = LogManager.getLogger(FilterConfig.class);
    private final AuthService authService;
    private final EventService eventService;


    @Autowired
    public FilterConfig(AuthService authService, EventService eventService) {
        this.authService = authService;
        this.eventService = eventService;
    }

    /**
    * this method is used to register the cors filter
    *
    * @return FilterRegistrationBean<TokenFilter>
    *
    */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterBean() {
        logger.info("CorsFilterBean has been created");
        FilterRegistrationBean <CorsFilter> registrationBean = new FilterRegistrationBean<>();
        CorsFilter corsFilter= new CorsFilter();
        registrationBean.setFilter(corsFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); //set precedence
        return registrationBean;
    }

    /**
     * his method is used to register the token filter
     * the token filter initialized with the auth service
     * the token filter is running first in the filter chain
     * @return FilterRegistrationBean<TokenFilter>
     */
    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilterBean() {
        logger.info("FilterRegistrationBean has been created");
        FilterRegistrationBean <TokenFilter> registrationBean = new FilterRegistrationBean<>();
        TokenFilter customURLFilter = new TokenFilter(authService);
        registrationBean.setFilter(customURLFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2); //set precedence
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<RolesFilter> rolesFilterBean() {
        logger.info("FilterRegistrationBean has been created");
        FilterRegistrationBean <RolesFilter> registrationBean = new FilterRegistrationBean<>();
        RolesFilter customURLFilter = new RolesFilter(eventService,authService);
        registrationBean.setFilter(customURLFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(3); //set precedence
        return registrationBean;
    }
}
