package ru.clevertec.check.dao;

import ru.clevertec.check.api.dao.ICardDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dao.connection.ConnectionPool;
import ru.clevertec.check.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDao implements ICardDao {

    private static final String INSERT_CARD = "INSERT INTO cards (number, discount) values (?, ?)";
    private static final String GET_CARD_BY_NUMBER = "SELECT number, discount FROM cards WHERE number = ?";
    private static final String GET_ALL_CARDS = "SELECT number, discount FROM cards";
    private static final String UPDATE_CARD = "UPDATE cards SET discount = ? WHERE number = ?";
    private static final String DELETE_CARD_BY_NUMBER = "DELETE FROM cards WHERE number = ?";

    @Override
    public Card saveCard(Card card) {
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_CARD)) {
            statement.setInt(1, card.getNumber());
            statement.setInt(2, card.getDiscount());
            statement.executeUpdate();
            return card;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при добавлении скидочной карты в БД ", e);
        }
    }

    @Override
    public Card getCardByNumber(Integer number) {
        Card card;
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CARD_BY_NUMBER)) {
            statement.setLong(1, number);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                card = createFromResultSet(resultSet);
            } else {
                throw new DaoException(String.format("Не удалось найти скидочную карту по введённому номеру: %d", number));
            }
            return card;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке найти скидочную карту по введённому номеру: %d", number));
        }
    }

    @Override
    public CustomList<Card> getAllCards() {
        CustomList<Card> cards = new CustomArrayList<>();
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_CARDS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Card card = createFromResultSet(resultSet);
                cards.add(card);
            }
            return cards;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при попытке найти скидочные карты в БД", e);
        }
    }

    @Override
    public Card updateDiscountInCard(Integer number, Integer discount) {
        Card card;
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CARD)) {
            statement.setInt(1, discount);
            statement.setInt(2, number);
            if (statement.executeUpdate() == 1) {
                card = new Card(number, discount);
            } else {
                throw new DaoException(String.format("Не удалось найти скидочную карту с номером: %d", number));
            }
            return card;
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке обновить скидочную карту с номером: %d", number));
        }
    }

    @Override
    public void deleteCard(Integer number) {
        try (Connection connection = ConnectionPool.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CARD_BY_NUMBER)) {
            statement.setInt(1, number);
            if (statement.executeUpdate() != 1) {
                throw new DaoException(String.format("Не удалось найти скидочную карту с номером: %d", number));
            }
        } catch (SQLException e) {
            throw new DaoException(String.format("Ошибка при попытке удалить скидочную карту с номером: %d", number));
        }
    }

    private Card createFromResultSet(ResultSet resultSet) {
        try {
            Card card = new Card();
            card.setNumber(resultSet.getInt("number"));
            card.setDiscount(resultSet.getInt("discount"));
            return card;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
