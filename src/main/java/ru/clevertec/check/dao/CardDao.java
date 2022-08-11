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

//    @Override
//    public Card saveCard(Card card) {
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(INSERT_CARD, Statement.RETURN_GENERATED_KEYS)) {
//            statement.setInt(1, card.getNumber());
//            statement.setInt(2, card.getDiscount());
//            statement.executeUpdate();
//            ResultSet resultSet = statement.getGeneratedKeys();
//            if (resultSet.next()) {
//                final Long key = resultSet.getLong(1);
//                card.setId(key);
//            } else {
//                throw new DaoException("Не удалось автоматически создать id для заказа");
//            }
//            return card;
//        } catch (SQLException e) {
//            throw new DaoException("Ошибка при добавлении заказа в БД ", e);
//        }
//    }
//
//    @Override
//    public Card getCardById(Long id) {
//        Card card;
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(GET_CARD_BY_ID)) {
//            statement.setLong(1, id);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                card = createFromResultSet(resultSet);
//            } else {
//                throw new DaoException(String.format("Не удалось найти скидочную карту по введённому id: %d", id));
//            }
//            return card;
//        } catch (SQLException e) {
//            throw new DaoException(String.format("Ошибка при попытке найти скидочную карту по введённому id: %d", id));
//        }
//    }
//
//    @Override
//    public CustomList<Card> getAllCards() {
//        CustomList<Card> cards = new CustomArrayList<>();
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(GET_ALL_CARDS)) {
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                Card card = createFromResultSet(resultSet);
//                cards.add(card);
//            }
//            return cards;
//        } catch (SQLException e) {
//            throw new DaoException("Ошибка при попытке найти скидочные карты в БД", e);
//        }
//    }
//
//    @Override
//    public Card updateCard(Card card, Long id) {
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(UPDATE_CARD_BY_ID)) {
//            statement.setInt(1, card.getNumber());
//            statement.setInt(2, card.getDiscount());
//            statement.setLong(3, id);
//            card.setId(id);
//            if (statement.executeUpdate() != 1) {
//                throw new DaoException(String.format("Не удалось найти скидочную карту по введённому id: %d", id));
//            }
//            return card;
//        } catch (SQLException e) {
//            throw new DaoException(String.format("Ошибка при попытке обновить скидочную карту по введённому id: %d", id));
//        }
//    }
//
//    @Override
//    public void deleteCard(Long id) {
//        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
//             PreparedStatement statement = connection.prepareStatement(DELETE_CARD_BY_ID)) {
//            statement.setLong(1, id);
//            if (statement.executeUpdate() != 1) {
//                throw new DaoException(String.format("Не удалось найти скидочную карту с id: %d", id));
//            }
//        } catch (SQLException e) {
//            throw new DaoException(String.format("Ошибка при попытке удалить скидочную карту с id: %d", id));
//        }
//    }
//
//    private Card createFromResultSet(ResultSet resultSet) {
//        try {
//            Card card = new Card();
//            card.setId(resultSet.getLong("card_id"));
//            card.setNumber(resultSet.getInt("card_number"));
//            card.setDiscount(resultSet.getInt("discount"));
//            return card;
//        } catch (SQLException e) {
//            throw new DaoException(e);
//        }
//    }
}
