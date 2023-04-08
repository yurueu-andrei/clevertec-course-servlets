package ru.clevertec.servlets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.servlets.model.Cheque;
import ru.clevertec.servlets.repository.CardRepository;
import ru.clevertec.servlets.repository.ProductRepository;
import ru.clevertec.servlets.service.ChequeBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ChequeJSONServlet", value = "/cheque")
public class ChequeJsonServlet extends HttpServlet {
    private ChequeBuilder chequeBuilder;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) {
        this.chequeBuilder = new ChequeBuilder(ProductRepository.getInstance(), CardRepository.getInstance());
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Cheque cheque = chequeBuilder.buildCheque(parametersToMap(request));
        PrintWriter writer = response.getWriter();
        String json = mapper.writeValueAsString(cheque);
        writer.print(json);
        writer.flush();
    }

    private Map<String, String> parametersToMap(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[0]));
    }
}
