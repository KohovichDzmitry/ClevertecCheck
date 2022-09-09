package ru.clevertec.check.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AEntity {

    private String name;
    private double price;
    private int stock;
    private int quantity;
}
