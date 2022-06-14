package ru.clevertec.check.task.resolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.check.model.product.Product;

import java.util.Random;

import static ru.clevertec.check.task.model.ModelTest.productDao;

public class ProductParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Product.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Random rand = new Random();
        System.out.println(productDao.getAllProducts().size());
        return productDao.getAllProducts().get(rand.nextInt(productDao.getAllProducts().size()));
    }
}
