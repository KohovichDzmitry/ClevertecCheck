package ru.clevertec.check.dao;

public interface IGetQuery {

    String getEntityName();

    String getInsertQuery();

    String getFindQuery();

    String getAllQuery();

    String getFindAllQuery();

    String getUpdateQuery();

    String getDeleteQuery();
}
