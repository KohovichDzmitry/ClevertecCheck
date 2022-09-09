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

    private static final Integer PAGE_SIZE_DEFAULT = 20;
    private static final Integer PAGE_DEFAULT = 0;

    @Override
    public T save(Map<String, String> parameters) throws ServiceException {
        try {
            return getDao().save(actionForSave(parameters));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public T getById(Long id) throws ServiceException {
        try {
            return getDao().getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public CustomList<T> getAll() throws ServiceException {
        try {
            return getDao().getAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public CustomList<T> findAll(String pageSizeStr, String pageStr) throws ServiceException {
        int pageSize = PAGE_SIZE_DEFAULT;
        int page = PAGE_DEFAULT * pageSize;
        if (pageSizeStr != null) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        if (pageStr != null) {
            page = Integer.parseInt(pageStr) * pageSize;
        }
        try {
            return getDao().findAll(pageSize, page);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public T update(Map<String, String> parameters, Long id) throws ServiceException {
        try {
            return getDao().update(actionForSave(parameters), id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            getDao().delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Integer countAllEntities() throws ServiceException {
        try {
            return getDao().getAll().size();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    protected abstract T actionForSave(Map<String, String> parameters) throws ServiceException;
}
