package pers.adlered.picuang;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static pers.adlered.picuang.tool.ToolBox.CONFIG_PATH;

@Component
public class RefererFilter extends OncePerRequestFilter {

    private static final Properties PROPERTIES = new Properties();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/images")) {
            logger.info(String.format("Referer: [%s], x-forwarded-for: [%s], x-real-ip: [%s]", request.getHeader("referer"), request.getHeader("x-forwarded-for"), request.getHeader("x-real-ip")));
            PROPERTIES.load(new BufferedInputStream(Files.newInputStream(Paths.get(CONFIG_PATH))));
            if (PROPERTIES.containsKey("referer") && !StringUtils.isEmpty(PROPERTIES.get("referer"))) {
                List<String> referer = new ArrayList<>(Arrays.asList(PROPERTIES.getProperty("referer").split(",")));
                if (null == request.getHeader("Referer") || !referer.contains(request.getHeader("Referer"))) {
                    if (PROPERTIES.containsKey("ips") && !StringUtils.isEmpty(PROPERTIES.get("ips"))) {
                        List<String> ips = new ArrayList<>(Arrays.asList(PROPERTIES.getProperty("ips").split(",")));
                        ips.add("127.0.0.1");
                        ips.add("0:0:0:0:0:0:0:1");
                        ips.add("172.17.0.1");
                        if (null == getIP(request) || !ips.contains(getIP(request))) {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write(String.format("ip: [%s] is forbidden", getIP(request)));
                            return;
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write(String.format("referer: [%s] is forbidden", null == request.getHeader("Referer") ? "empty" : request.getHeader("Referer")));
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private static String getIP(HttpServletRequest request) {
        String ip;
        if (!StringUtils.isEmpty(request.getHeader("X-Real-IP"))) {
            ip = request.getHeader("X-Real-IP");
        } else if (!StringUtils.isEmpty(request.getHeader("X-Forwarded-For"))) {
            ip = request.getHeader("X-Forwarded-For").split(",")[0];
        } else {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
