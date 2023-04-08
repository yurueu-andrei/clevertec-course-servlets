package ru.clevertec.servlets.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import ru.clevertec.servlets.exception.ValidationException;
import ru.clevertec.servlets.filter.wrapper.RequestWrapper;
import ru.clevertec.servlets.model.entity.Product;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@WebFilter(filterName = "BarcodeValidationFilter", value = "/products")
public class BarcodeValidationFilter implements Filter {

    /**
     * This method checks if barcode given in the request body matches the regular expression
     * for post and put methods of /products URI
     *
     * @throws ValidationException in case of invalid barcode value
     */
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getMethod().equalsIgnoreCase("POST") ||
                req.getMethod().equalsIgnoreCase("PUT")) {
            RequestWrapper servletRequest = new RequestWrapper(req);
            String requestBody = extractRequestBody(servletRequest);
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(requestBody, Product.class);
            if (!product.getBarcode().matches("^([0-9]{3})[A-Z]{3}[0-9]{2}[A-Z]{2}")) {
                throw new ValidationException("Unable to update product. Barcode is invalid");
            } else {
                chain.doFilter(servletRequest, response);
            }

        } else {
            chain.doFilter(request, response);
        }
    }

    private String extractRequestBody(ServletRequest request) throws IOException {
        try (Scanner s = new Scanner(request.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }
}
