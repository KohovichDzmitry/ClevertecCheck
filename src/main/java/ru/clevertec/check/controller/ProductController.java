package ru.clevertec.check.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.check.dto.ProductDto;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.service.IProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductDto> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @PutMapping
    public ResponseEntity<ProductDto> update(@RequestBody Product product) {
        return ResponseEntity.ok(productService.update(product, product.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> findById() {
        return ResponseEntity.ok(productService.countAllEntities());
    }

    @GetMapping("/sorted_by_alphabet")
    public ResponseEntity<List<ProductDto>> findAllAndOrderByName() {
        return ResponseEntity.ok(productService.findAllAndOrderByName());
    }

    @GetMapping("/sorted_by_price")
    public ResponseEntity<List<ProductDto>> findAllAndOrderByPrice() {
        return ResponseEntity.ok(productService.findAllAndOrderByPrice());
    }
}
