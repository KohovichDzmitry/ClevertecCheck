package ru.clevertec.check.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.check.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByName(String name);

    Integer countAllBy();

    List<Product> findAllAndOrderBy(Sort sort);
}
