package ru.clevertec.check.service;

import ru.clevertec.check.api.dao.GenericDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.api.service.GenericService;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.AEntity;

import java.util.Map;

public abstract class AbstractService<T extends AEntity, D extends GenericDao<T>> implements GenericService<T> {

    protected abstract D getDao();

    @Override
    public T save(Map<String, String> parameters) {
        try {
            return getDao().save(actionForSave(parameters));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public T getById(Long id) {
        try {
            return getDao().getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public CustomList<T> getAll() {
        try {
            return getDao().getAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public T update(Map<String, String> parameters, Long id) {
        try {
            return getDao().update(actionForSave(parameters), id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            getDao().delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Integer countAllEntity() {
        return getDao().getAll().size();
    }

    protected abstract T actionForSave(Map<String, String> parameters);
}
