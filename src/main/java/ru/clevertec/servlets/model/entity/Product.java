package ru.clevertec.servlets.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class Product extends BaseEntity {

    private Long id;
    private String name;
    private BigDecimal price;
    private boolean onSale;
    private String barcode;
}
