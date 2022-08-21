package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.GenericDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dao.connection.ConnectionPool;
import ru.clevertec.check.model.AEntity;

import java.sql.*;
import java.util.Comparator;

public abstract class AbstractDao<T extends AEntity> implements GenericDao<T> {

    @Override
    public T save(T entity) {
        String sql = getInsertQuery();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForSave(statement, entity);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                final Long key = resultSet.getLong(1);
                entity.setId(key);
            } else {
                throw new DaoException("Не удалось автоматически создать id");
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при добавлении сущности в БД ", e);
        }
    }

    @Override
    public T getById(Long id) {
        T entity;
        String sql = getFindQuery();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entity = prepareStatementForFind(resultSet);
            } else {
                throw new DaoException(String.format("Не удалось найти сущность по введённому id: %d", id));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке найти сущность по введённому id: %d", id));
        }
    }

    @Override
    public CustomList<T> getAll() {
        String sql = getFindAllQuery();
        CustomList<T> entitys = new CustomArrayList<>();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T entity = prepareStatementForFind(resultSet);
                entitys.add(entity);
            }
            return entitys;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при попытке найти сущности в БД", e);
        }
    }

    @Override
    public T update(T entity, Long id) {
        String sql = getUpdateQuery();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, entity, id);
            entity.setId(id);
            if (statement.executeUpdate() != 1) {
                throw new DaoException(String.format("Не удалось найти сущность по введённому id: %d", id));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке обновить сущность по введённому id: %d", id));
        }
    }

    @Override
    public void delete(Long id) {
        String sql = getDeleteQuery();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            if (statement.executeUpdate() != 1) {
                throw new DaoException(String.format("Не удалось найти сущность по введённому id: %d", id));
            }
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке удалить сущность по введённому id: %d", id));
        }
    }

    @Override
    public CustomList<T> getAllSorted(Comparator<T> comparator) {
        return CustomList.toCustomList(getAll().stream().sorted(comparator).toArray());
    }

    protected abstract String getInsertQuery();

    protected abstract String getFindQuery();

    protected abstract String getFindAllQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getDeleteQuery();

    protected abstract void prepareStatementForSave(PreparedStatement statement, T entity);

    protected abstract T prepareStatementForFind(ResultSet resultSet);

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T entity, Long id);

}
