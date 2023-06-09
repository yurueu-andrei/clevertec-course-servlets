package ru.clevertec.servlets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.RequestDispatcher.ERROR_MESSAGE;

@WebServlet(name = "ExceptionHandlerServlet", value = "/detailedError")
public class ExceptionHandlerServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        handleException(response, request);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        handleException(response, request);
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        handleException(response, request);
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        handleException(response, request);
    }

    private void handleException(HttpServletResponse response, HttpServletRequest request) throws IOException {
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> details = new HashMap<>();
        details.put("info", request.getAttribute(ERROR_MESSAGE));
        String responseValue = mapper
                .writeValueAsString(new ApiCallDetailedError("Something gone wrong", details));
        writer.print(responseValue);
        writer.flush();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ApiCallDetailedError {
        private String message;
        private Map<String, Object> details;
    }
}
