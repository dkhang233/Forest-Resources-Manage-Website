package com.project.forestresourcesmanageapplication.utils;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.project.forestresourcesmanageapplication.models.AccessLog;
import com.project.forestresourcesmanageapplication.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// @Component
@WebFilter(urlPatterns = "/*")
@Order(2)
@Slf4j
@RequiredArgsConstructor
public class AccessLogFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(req, resp);

        byte[] requestBody = req.getContentAsByteArray();
        byte[] responseBody = resp.getContentAsByteArray();
        String httpMethod = request.getMethod();
        if (httpMethod.equals("POST") || httpMethod.equals("PUT")) {
            String requestUrl = request.getRequestURI();
            int statusCode = response.getStatus();
            String requestBodyJSON = new String(requestBody).isEmpty() ? "{}" : new String(requestBody);
            String responseBodyJSON = new String(responseBody).isEmpty() ? "{}" : new String(requestBody);
            AccessLog accessLog = AccessLog.builder()
                    .username("dkhang")
                    .httpMethod(httpMethod)
                    .requestUrl(requestUrl)
                    .statusCode(statusCode)
                    .requestBody(requestBodyJSON)
                    .responseBody(responseBodyJSON)
                    .build();
            userService.createAccessLog(accessLog);
        }
        resp.copyBodyToResponse();
    }

}
