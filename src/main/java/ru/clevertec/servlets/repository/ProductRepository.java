package ru.clevertec.servlets.repository;

import ru.clevertec.servlets.model.entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRepository extends Repository<Product> {

    private static ProductRepository INSTANCE;

    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM products WHERE id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM products LIMIT ? OFFSET ?";
    private static final String INSERT_QUERY =
            "INSERT INTO products (name, price, on_sale, barcode) VALUES (?,?,?,?)";
    private static final String UPDATE_QUERY =
            "UPDATE products SET name=?, price=?, on_sale=?, barcode=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM products WHERE id=?";

    private ProductRepository() {
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
    protected Product construct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setOnSale(resultSet.getBoolean("on_sale"));
        product.setBarcode(resultSet.getString("barcode"));
        return product;
    }

    @Override
    protected void settingPreparedStatement(PreparedStatement preparedStatement, Product product) throws SQLException {
        preparedStatement.setString(1, product.getName());
        preparedStatement.setBigDecimal(2, product.getPrice());
        preparedStatement.setBoolean(3, product.isOnSale());
        preparedStatement.setString(4, product.getBarcode());
    }

    public static ProductRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductRepository();
        }
        return INSTANCE;
    }
}
