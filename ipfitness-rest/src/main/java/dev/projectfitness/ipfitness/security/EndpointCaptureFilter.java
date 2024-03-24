package dev.projectfitness.ipfitness.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static dev.projectfitness.ipfitness.util.Constants.ENDPOINT_SERVLET_ATTRIBUTE;
import static dev.projectfitness.ipfitness.util.Constants.IPADDRESS_SERVLET_ATTRIBUTE;

@Component
public class EndpointCaptureFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.setAttribute(ENDPOINT_SERVLET_ATTRIBUTE, request.getRequestURI());
        request.setAttribute(IPADDRESS_SERVLET_ATTRIBUTE, request.getRemoteAddr());
        filterChain.doFilter(request, response);
    }
}
