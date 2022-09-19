package ru.clevertec.check.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomArrayList;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dto.ProductDto;
import ru.clevertec.check.mapper.ProductMapper;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.validator.ProductDataValidator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService implements IProductService {

    @Value("${check.pageSizeDefault}")
    private int pageSize;
    @Value("${check.pageDefault}")
    private int page;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto save(Product product) throws ServiceException {
        if (!ProductDataValidator.isValidProductParameters(product)) {
            throw new ServiceException("Ошибка при валидации данных продукта. Название продукта должно начинаться " +
                    "с прописной буквы, значение цены не должно быть отрицательным, а значение скидки - 0 или 1");
        }
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductDto findById(Long id) throws ServiceException {
        return productRepository.findById(id).map(productMapper::toDto)
                .orElseThrow(() -> new ServiceException(String
                        .format("Ошибка при попытке найти продукт по введённому id: %d", id)));
    }

    @Override
    public CustomList<ProductDto> getAll() {
        CustomList<ProductDto> products = new CustomArrayList<>();
        productRepository.findAll().stream().map(productMapper::toDto).forEach(products::add);
        return products;
    }

    @Override
    public CustomList<ProductDto> findAll(String pageSizeStr, String pageStr) {
        if (pageSizeStr != null) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }
        CustomList<ProductDto> products = new CustomArrayList<>();
        productRepository.findAll(PageRequest.of(page, pageSize)).stream()
                .map(productMapper::toDto).forEach(products::add);
        return products;
    }

    @Override
    @Transactional
    public ProductDto update(Product product, Long id) throws ServiceException {
        if (productRepository.findById(id).isEmpty()) {
            throw new ServiceException(String.format("Ошибка при попытке найти продукт по введённому id: %d", id));
        }
        if (!ProductDataValidator.isValidProductParameters(product)) {
            throw new ServiceException("Ошибка при валидации данных продукта. Название продукта должно начинаться " +
                    "с прописной буквы, значение цены не должно быть отрицательным, а значение скидки - 0 или 1");
        }
        product.setId(id);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(Long id) throws ServiceException {
        if (productRepository.findById(id).isEmpty()) {
            throw new ServiceException(String.format("Ошибка при попытке найти продукт по введённому id: %d", id));
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto findProductByName(String name) throws ServiceException {
        if (!ProductDataValidator.isValidNameProduct(name)) {
            throw new ServiceException("Ошибка при валидации названия продукта. Название продукта должно начинаться " +
                    "с прописной буквы!");
        }
        return productRepository.findProductByName(name).map(productMapper::toDto)
                .orElseThrow(() -> new ServiceException(String
                        .format("Ошибка при попытке найти продукт по введённому названию: %s", name)));
    }

    @Override
    public Integer countAllEntities() {
        return productRepository.countAllBy();
    }

    @Override
    public CustomList<ProductDto> findAllAndOrderByName() {
        CustomList<ProductDto> products = new CustomArrayList<>();
        productRepository.findAllAndOrderBy(Sort.by("name"))
                .stream().map(productMapper::toDto).forEach(products::add);
        return products;
    }

    @Override
    public CustomList<ProductDto> findAllAndOrderByPrice() {
        CustomList<ProductDto> products = new CustomArrayList<>();
        productRepository.findAllAndOrderBy(Sort.by("price"))
                .stream().map(productMapper::toDto).forEach(products::add);
        return products;
    }
}
