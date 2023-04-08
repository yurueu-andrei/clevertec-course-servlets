package ru.clevertec.servlets.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "ContentTypeFilter", value = "/")
public class ContentTypeFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        List<String> uriElements = Arrays.stream(req.getRequestURI().split("/")).toList();
        response.setCharacterEncoding("UTF-8");
        if (uriElements.contains("pdf")) {
            response.setContentType("application/pdf");
        } else {
            response.setContentType("application/json");
        }

        chain.doFilter(request, response);
    }
}
