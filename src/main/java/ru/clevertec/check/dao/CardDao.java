package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.model.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDao extends AbstractDao<Card> implements ICardDao {

    private static final String INSERT_CARD = "INSERT INTO cards (card_number, discount) values (?, ?)";
    private static final String GET_CARD_BY_ID = "SELECT card_id, card_number, discount FROM cards WHERE card_id = ?";
    private static final String GET_ALL_CARDS = "SELECT card_id, card_number, discount FROM cards";
    private static final String UPDATE_CARD_BY_ID = "UPDATE cards SET card_number = ?, discount = ? WHERE card_id = ?";
    private static final String DELETE_CARD_BY_ID = "DELETE FROM cards WHERE card_id = ?";

    @Override
    protected String getInsertQuery() {
        return INSERT_CARD;
    }

    @Override
    protected String getFindQuery() {
        return GET_CARD_BY_ID;
    }

    @Override
    protected String getFindAllQuery() {
        return GET_ALL_CARDS;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_CARD_BY_ID;
    }

    @Override
    protected String getDeleteQuery() {
        return DELETE_CARD_BY_ID;
    }

    @Override
    protected void prepareStatementForSave(PreparedStatement statement, Card card) {
        try {
            statement.setInt(1, card.getNumber());
            statement.setInt(2, card.getDiscount());
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос ", e);
        }
    }

    @Override
    protected Card prepareStatementForFind(ResultSet resultSet) {
        try {
            Card card = new Card();
            card.setId(resultSet.getLong("card_id"));
            card.setNumber(resultSet.getInt("card_number"));
            card.setDiscount(resultSet.getInt("discount"));
            return card;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Card card, Long id) {
        try {
            statement.setInt(1, card.getNumber());
            statement.setInt(2, card.getDiscount());
            statement.setLong(3, id);
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос ", e);
        }
    }
}
