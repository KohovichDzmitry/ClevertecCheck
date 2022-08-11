package ru.clevertec.check.api.dao;

import ru.clevertec.check.model.Order;

public interface IOrderDao extends GenericDao<Order> {

    Order getOrderByIdProduct(Long id);
}
