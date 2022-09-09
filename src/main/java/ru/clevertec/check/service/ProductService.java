package ru.clevertec.check.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.stereotype.Service;
import ru.clevertec.check.api.dao.IProductDao;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.api.service.IProductService;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.validator.ProductDataValidator;

import java.util.Comparator;
import java.util.Map;

@Service
@Value
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductService extends AbstractService<Product, IProductDao> implements IProductService {

    IProductDao productDao;

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
            throw new ServiceException("Ошибка при валидации данных продукта. Название продукта должно начинаться " +
                    "с прописной буквы, значение цены не должно быть отрицательным, а значение скидки - 0 или 1");
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
