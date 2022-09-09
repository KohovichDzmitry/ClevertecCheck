package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.GenericDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dao.connection.ConnectionPool;
import ru.clevertec.check.model.AEntity;

import java.sql.*;
import java.util.Comparator;

public abstract class AbstractDao<T extends AEntity> implements GenericDao<T>, IGetQuery, IPrepareStatement<T> {

    @Override
    public T save(T entity) throws DaoException {
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
            throw new DaoException("Ошибка при попытке добавить " + getEntityName() + " в БД");
        }
    }

    @Override
    public T getById(Long id) throws DaoException {
        T entity;
        String sql = getFindQuery();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entity = prepareStatementForFind(resultSet);
            } else {
                throw new DaoException(String.format("Не удалось найти " + getEntityName() + " по введённому id: %d", id));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке найти " + getEntityName() + " по введённому id: %d", id));
        }
    }

    @Override
    public CustomList<T> getAll() throws DaoException {
        String sql = getAllQuery();
        CustomList<T> entities = new CustomArrayList<>();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T entity = prepareStatementForFind(resultSet);
                entities.add(entity);
            }
            return entities;
        } catch (SQLException e) {
            throw new DaoException("Не удалось найти информацию в БД");
        }
    }

    @Override
    public CustomList<T> findAll(Integer pageSize, Integer page) throws DaoException {
        String sql = getFindAllQuery();
        CustomList<T> entities = new CustomArrayList<>();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, page);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T entity = prepareStatementForFind(resultSet);
                entities.add(entity);
            }
            return entities;
        } catch (SQLException e) {
            throw new DaoException("Не удалось найти информацию в БД");
        }
    }

    @Override
    public T update(T entity, Long id) throws DaoException {
        String sql = getUpdateQuery();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, entity, id);
            entity.setId(id);
            if (statement.executeUpdate() != 1) {
                throw new DaoException(String.format("Не удалось обновить " + getEntityName() + " по введённому id: %d", id));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке обновить " + getEntityName() + " по введённому id: %d", id));
        }
    }

    @Override
    public void delete(Long id) throws DaoException {
        String sql = getDeleteQuery();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            if (statement.executeUpdate() != 1) {
                throw new DaoException(String.format("Не удалось найти " + getEntityName() + " по введённому id: %d", id));
            }
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке удалить " + getEntityName() + " по введённому id: %d", id));
        }
    }

    @Override
    public CustomList<T> getAllSorted(Comparator<T> comparator) throws DaoException {
        return CustomList.toCustomList(getAll().stream().sorted(comparator).toArray());
    }
}
