package ru.clevertec.servlets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.servlets.model.entity.Product;
import ru.clevertec.servlets.repository.ProductRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "ProductServlet", value = "/products/*")
public class ProductServlet extends HttpServlet {
    private ProductRepository productRepository;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) {
        this.productRepository = ProductRepository.getInstance();
        this.mapper = new ObjectMapper();
    }

    /**
     * This method parses the URI and returns single product when URI contains id of given
     * product (/products/10), if product is not found it responses with null value and 404 status code.
     * If URI is /products it returns all products with pagination (limit = 20, offset = 0 are default values),
     * but you can write your own limit and offset values in query parameters (/products?limit=10&offset=10)
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        PrintWriter writer = response.getWriter();
        String[] uriElements = request.getRequestURI().split("/");
        if (uriElements.length > 2) {
            Product product = productRepository.findById(Long.valueOf(uriElements[2]));
            if (product == null) {
                response.setStatus(404);
            }
            writer.print(mapper.writeValueAsString(product));
            writer.flush();
        } else {
            String limitValue = request.getParameter("limit");
            String offsetValue = request.getParameter("offset");
            int limit = limitValue == null ? 20 : Integer.parseInt(limitValue);
            int offset = offsetValue == null ? 0 : Integer.parseInt(offsetValue);
            List<Product> products = productRepository.findAll(limit, offset);
            writer.print(mapper.writeValueAsString(products));
            writer.flush();
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        PrintWriter writer = response.getWriter();
        Product product = productRepository.add(mapToProduct(request));
        writer.print(mapper.writeValueAsString(product));
        writer.flush();
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.print(productRepository.update(mapToProduct(request)));
        writer.flush();
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        PrintWriter writer = response.getWriter();
        String[] uriElements = request.getRequestURI().split("/");
        boolean deleteResult = productRepository.delete(Long.valueOf(uriElements[2]));
        writer.print(deleteResult);
        writer.flush();
    }

    private Product mapToProduct(ServletRequest request) throws IOException {
        Scanner s = new Scanner(request.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A");
        String requestBody = s.hasNext() ? s.next() : "";
        return mapper.readValue(requestBody, Product.class);
    }
}
