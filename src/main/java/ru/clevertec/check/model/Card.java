package ru.clevertec.check.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card extends AEntity {

    private Integer number;
    private Integer discount;
}
