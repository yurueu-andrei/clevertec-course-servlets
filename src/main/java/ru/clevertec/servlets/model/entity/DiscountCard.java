package ru.clevertec.servlets.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DiscountCard extends BaseEntity {

    private Long id;
    private Integer discount;
}
