package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.dao.connection.ConnectionPool;
import ru.clevertec.check.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDao extends AbstractDao<Card> implements ICardDao {

    private static final CardDao INSTANCE = new CardDao();

    private static final String NAME = "скидочную карту";

    private static final String INSERT_CARD = "INSERT INTO cards (card_number, discount) values (?, ?)";
    private static final String GET_CARD_BY_ID = "SELECT card_id, card_number, discount FROM cards WHERE card_id = ?";
    private static final String GET_CARD_BY_NUMBER = "SELECT card_id, card_number, discount FROM cards WHERE card_number = ?";
    private static final String GET_ALL_CARDS = "SELECT card_id, card_number, discount FROM cards";
    private static final String FIND_ALL_CARDS = "SELECT card_id, card_number, discount FROM cards LIMIT ? OFFSET ?";
    private static final String UPDATE_CARD_BY_ID = "UPDATE cards SET card_number = ?, discount = ? WHERE card_id = ?";
    private static final String DELETE_CARD_BY_ID = "DELETE FROM cards WHERE card_id = ?";

    private CardDao() {
    }

    public static CardDao getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getEntityName() {
        return NAME;
    }

    @Override
    protected String getInsertQuery() {
        return INSERT_CARD;
    }

    @Override
    protected String getFindQuery() {
        return GET_CARD_BY_ID;
    }

    @Override
    protected String getAllQuery() {
        return GET_ALL_CARDS;
    }

    @Override
    protected String getFindAllQuery() {
        return FIND_ALL_CARDS;
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
    protected void prepareStatementForSave(PreparedStatement statement, Card card) throws DaoException {
        try {
            statement.setInt(1, card.getNumber());
            statement.setInt(2, card.getDiscount());
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос");
        }
    }

    @Override
    protected Card prepareStatementForFind(ResultSet resultSet) throws DaoException {
        try {
            Card card = new Card();
            card.setId(resultSet.getLong("card_id"));
            card.setNumber(resultSet.getInt("card_number"));
            card.setDiscount(resultSet.getInt("discount"));
            return card;
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос");
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Card card, Long id) throws DaoException {
        try {
            statement.setInt(1, card.getNumber());
            statement.setInt(2, card.getDiscount());
            statement.setLong(3, id);
        } catch (SQLException e) {
            throw new DaoException("Не удалось выполнить запрос");
        }
    }

    @Override
    public Card getCardByNumber(Integer number) throws DaoException {
        Card card;
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CARD_BY_NUMBER)) {
            statement.setInt(1, number);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                card = prepareStatementForFind(resultSet);
            } else {
                throw new DaoException(String.format("Не удалось найти " + NAME + " по введённому номеру: %d", number));
            }
            return card;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке найти" + NAME + "по введённому номеру: %d", number));
        }
    }
}
