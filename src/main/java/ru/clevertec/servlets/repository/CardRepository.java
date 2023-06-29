package ru.clevertec.servlets.repository;

import ru.clevertec.servlets.model.entity.DiscountCard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardRepository extends Repository<DiscountCard> {

    private static CardRepository INSTANCE;

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM discount_cards WHERE id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM discount_cards LIMIT ? OFFSET ?";
    private static final String INSERT_QUERY =
            "INSERT INTO discount_cards (discount) VALUES (?)";
    private static final String UPDATE_QUERY =
            "UPDATE discount_cards SET discount=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM discount_cards WHERE id=?";

    private CardRepository() {
    }

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_BY_ID_QUERY;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_QUERY;
    }

    @Override
    protected String getInsertQuery() {
        return INSERT_QUERY;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected String getDeleteQuery() {
        return DELETE_QUERY;
    }

    @Override
    protected DiscountCard construct(ResultSet resultSet) throws SQLException {
        DiscountCard card = new DiscountCard();
        card.setId(resultSet.getLong("id"));
        card.setDiscount(resultSet.getInt("discount"));
        return card;
    }

    @Override
    protected void settingPreparedStatement(PreparedStatement preparedStatement, DiscountCard card) throws SQLException {
        preparedStatement.setInt(1, card.getDiscount());
    }

    public static CardRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardRepository();
        }
        return INSTANCE;
    }
}
