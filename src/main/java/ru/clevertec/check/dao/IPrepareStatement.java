package ru.clevertec.check.dao;

import ru.clevertec.check.api.exceptions.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface IPrepareStatement <T> {

    void prepareStatementForSave(PreparedStatement statement, T entity) throws DaoException;

    T prepareStatementForFind(ResultSet resultSet) throws DaoException;

    void prepareStatementForUpdate(PreparedStatement statement, T entity, Long id) throws DaoException;
}
