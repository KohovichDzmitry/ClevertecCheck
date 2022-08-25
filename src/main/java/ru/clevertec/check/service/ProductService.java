package ru.clevertec.check.service;

import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.api.service.IProductService;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.validator.ProductDataValidator;

import java.util.Comparator;
import java.util.Map;

public class ProductService extends AbstractService<Product, IProductDao> implements IProductService {

    private static final ProductService INSTANCE = new ProductService();
    private final IProductDao productDao = ProductDao.getInstance();

    private ProductService() {
    }

    public static ProductService getInstance() {
        return INSTANCE;
    }

    @Override
    protected IProductDao getDao() {
        return productDao;
    }

    @Override
    protected Product actionForSave(Map<String, String> parameters) throws ServiceException {
        Product product = new Product();
        if (ProductDataValidator.isValidProductParameters(parameters)) {
            product.setName(parameters.get("product_name"));
            product.setPrice(Double.parseDouble(parameters.get("price")));
            product.setStock(Integer.parseInt(parameters.get("stock")));
            return product;
        } else {
            throw new ServiceException("Ошибка при валидации данных продукта");
        }
    }

    @Override
    public Product getProductByName(String name) throws ServiceException {
        if (ProductDataValidator.isValidNameProduct(name)) {
            return productDao.getProductByName(name);
        } else {
            throw new ServiceException("Ошибка при валидации названия продукта. Название продукта должно начинаться " +
                    "с прописной буквы!");
        }
    }

    @Override
    public CustomList<Product> getAllSortedByAlphabet() throws ServiceException {
        try {
            return productDao.getAllSorted(Comparator.comparing(Product::getName));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public CustomList<Product> getAllSortedByPrice() throws ServiceException {
        try {
            return productDao.getAllSorted(Comparator.comparing(Product::getPrice));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}