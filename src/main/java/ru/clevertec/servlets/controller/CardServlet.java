package ru.clevertec.servlets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.servlets.model.entity.DiscountCard;
import ru.clevertec.servlets.repository.CardRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

@WebServlet(name = "CardServlet", value = "/cards/*")
public class CardServlet extends HttpServlet {
    private CardRepository cardRepository;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) {
        this.cardRepository = CardRepository.getInstance();
        this.mapper = new ObjectMapper();
    }

    /**
     * This method parses the URI and returns single product when URI contains id of given
     * card (/cards/10), if card is not found it responses with null value and 404 status code.
     * If URI is /cards it returns all cards with pagination (limit = 20, offset = 0 are default values),
     * but you can write your own limit and offset values in query parameters (/cards?limit=10&offset=10)
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        PrintWriter writer = response.getWriter();
        String[] uriElements = request.getRequestURI().split("/");
        if (uriElements.length > 2) {
            DiscountCard card = cardRepository.findById(Long.valueOf(uriElements[2]));
            if (card == null) {
                response.setStatus(404);
            }
            writer.print(mapper.writeValueAsString(card));
            writer.flush();
        } else {
            String limitValue = request.getParameter("limit");
            String offsetValue = request.getParameter("offset");
            int limit = limitValue == null ? 20 : Integer.parseInt(limitValue);
            int offset = offsetValue == null ? 0 : Integer.parseInt(offsetValue);
            List<DiscountCard> cards = cardRepository.findAll(limit, offset);
            writer.print(mapper.writeValueAsString(cards));
            writer.flush();
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        PrintWriter writer = response.getWriter();
        DiscountCard card = cardRepository.add(mapToCard(request));
        writer.print(mapper.writeValueAsString(card));
        writer.flush();
    }

    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        PrintWriter writer = response.getWriter();
        boolean updateResult = cardRepository.update(mapToCard(request));
        writer.print(updateResult);
        writer.flush();
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        PrintWriter writer = response.getWriter();
        String[] uriElements = request.getRequestURI().split("/");
        boolean deleteResult = cardRepository.delete(Long.valueOf(uriElements[2]));
        writer.print(deleteResult);
        writer.flush();
    }

    private DiscountCard mapToCard(HttpServletRequest request) throws IOException {
        Scanner s = new Scanner(request.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A");
        String requestBody = s.hasNext() ? s.next() : "";
        return mapper.readValue(requestBody, DiscountCard.class);
    }
}
