package ru.clevertec.check.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.check.dto.ProductDto;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.mapper.ProductMapper;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.service.IProductService;
import ru.clevertec.check.validator.ProductDataValidator;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService implements IProductService {

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
    public List<ProductDto> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).stream().map(productMapper::toDto).collect(toList());
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
    public ProductDto findByName(String name) throws ServiceException {
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
    public List<ProductDto> findAllAndOrderByName() {
        return productRepository.findAllAndOrderBy(Sort.by("name"))
                .stream().map(productMapper::toDto).collect(toList());
    }

    @Override
    public List<ProductDto> findAllAndOrderByPrice() {
        return productRepository.findAllAndOrderBy(Sort.by("price"))
                .stream().map(productMapper::toDto).collect(toList());
    }
}
