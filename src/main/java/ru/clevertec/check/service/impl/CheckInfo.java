package ru.clevertec.check.service.impl;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.check.entity.Product;

import java.util.List;

@Data
@Builder
public class CheckInfo {

    private final List<Product> itemList;
    private final Integer discount;
}
