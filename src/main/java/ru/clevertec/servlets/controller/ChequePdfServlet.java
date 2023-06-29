package ru.clevertec.servlets.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.servlets.model.Cheque;
import ru.clevertec.servlets.repository.CardRepository;
import ru.clevertec.servlets.repository.ProductRepository;
import ru.clevertec.servlets.service.ChequeBuilder;
import ru.clevertec.servlets.util.PdfConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ChequePDFServlet", value = "/cheque/pdf")
public class ChequePdfServlet extends HttpServlet {
    private ChequeBuilder chequeBuilder;
    private PdfConverter pdfConverter;

    @Override
    public void init(ServletConfig config) {
        this.chequeBuilder = new ChequeBuilder(ProductRepository.getInstance(), CardRepository.getInstance());
        this.pdfConverter = new PdfConverter();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Cheque cheque = chequeBuilder.buildCheque(parametersToMap(request));
        pdfConverter.chequeToPdf(cheque);
        response.addHeader("Content-Disposition", "inline; filename=" + "Cheque.pdf");
        ServletOutputStream out = response.getOutputStream();
        File chequeFile = new File("Cheque.pdf");
        Files.copy(chequeFile.toPath(), out);
    }

    private Map<String, String> parametersToMap(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[0]));
    }
}