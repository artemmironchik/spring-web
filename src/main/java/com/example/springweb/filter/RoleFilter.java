package com.example.springweb.filter;

import com.example.springweb.dto.UserDto;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        if ((requestURI.startsWith("/admin") || requestURI.equals("/cart") ||
                requestURI.startsWith("/profile") || requestURI.startsWith("/products"))
                && httpRequest.getSession().getAttribute("user") == null) {
            httpResponse.sendRedirect("/login");
            return;
        }

        if (requestURI.equals("/") || requestURI.equals("/login") || requestURI.equals("/register")) {
            chain.doFilter(request, response);
            return;
        }
        UserDto user = (UserDto) httpRequest.getSession().getAttribute("user");

        if (requestURI.startsWith("/admin") && requestURI.startsWith("/products") && !Objects.equals(user.getRole().getName(), "ROLE_ADMIN")) {
            httpResponse.sendRedirect("/profile");
            return;
        }
        else if (requestURI.equals("/profile") || requestURI.equals("/products") || requestURI.startsWith("/home")
                && Objects.equals(user.getRole().getName(), "ROLE_USER") || Objects.equals(user.getRole().getName(), "ROLE_ADMIN")) {
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }
}
