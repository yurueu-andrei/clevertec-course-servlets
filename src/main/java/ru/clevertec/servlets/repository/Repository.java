package ru.clevertec.servlets.repository;

import ru.clevertec.servlets.exception.RepositoryException;
import ru.clevertec.servlets.model.entity.BaseEntity;
import ru.clevertec.servlets.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class Repository<E extends BaseEntity> {

    private final DBUtil dbUtil;

    public Repository() {
        this.dbUtil = DBUtil.getInstance();
    }

    public DBUtil getDataSource() {
        return dbUtil;
    }

    protected abstract String getSelectByIdQuery();
    protected abstract String getSelectAllQuery();
    protected abstract String getInsertQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getDeleteQuery();
    protected abstract E construct(ResultSet resultSet) throws SQLException;
    protected abstract void settingPreparedStatement(PreparedStatement preparedStatement, E element) throws SQLException;

    public E findById(Long id) throws RepositoryException {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getSelectByIdQuery())
        ) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? construct(resultSet) : null;
            }
        } catch (Exception ex) {
            throw new RepositoryException("The entity was not found[" + ex.getMessage() + "]");
        }
    }

    public List<E> findAll(int limit, int offset) throws RepositoryException {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getSelectAllQuery())
        ) {
            preparedStatement.setLong(1, limit);
            preparedStatement.setLong(2, offset);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<E> found = new ArrayList<>();
                while (resultSet.next()) {
                    found.add(construct(resultSet));
                }
                return found;
            }
        } catch (Exception ex) {
            throw new RepositoryException("The entities were not found[" + ex.getMessage() + "]");
        }
    }

    public E add(E element) throws RepositoryException {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getInsertQuery(), Statement.RETURN_GENERATED_KEYS)
        ) {
            settingPreparedStatement(preparedStatement, element);
            int value = preparedStatement.executeUpdate();

            if (value == 1) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        element.setId(resultSet.getLong(1));
                    }
                }
            }
            return element;
        } catch (Exception ex) {
            throw new RepositoryException(element.getClass().getSimpleName() + " was not added [" + ex.getMessage() + "]");
        }
    }

    public boolean update(E element) throws RepositoryException {
        int idQueryIndex = findIdPosition(getUpdateQuery());
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getUpdateQuery())
        ) {
            settingPreparedStatement(preparedStatement, element);
            preparedStatement.setLong(idQueryIndex, element.getId());

            return preparedStatement.executeUpdate() == 1;
        } catch (Exception ex) {
            throw new RepositoryException(element.getClass().getSimpleName() + " was not updated [" + ex.getMessage() + "]");
        }
    }

    private int findIdPosition(String query) {
        return (int) query.chars()
                .filter(charId -> charId == '?')
                .count();
    }

    public boolean delete(Long id) throws RepositoryException {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDeleteQuery())
        ) {
            preparedStatement.setLong(1, id);
            try {
                connection.setAutoCommit(false);
                preparedStatement.executeUpdate();
                connection.commit();
                return true;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception ex) {
            throw new RepositoryException("The entity was not deleted [" + ex.getMessage() + "]");
        }
    }
}
