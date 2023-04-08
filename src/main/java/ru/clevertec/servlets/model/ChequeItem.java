package ru.clevertec.servlets.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Cheque item(product in cheque) class
 *
 * @author Yuryeu Andrei
 * @see Cheque
 */
@Data
@AllArgsConstructor
public class ChequeItem {
    /**
     * Quantity field
     */
    private int quantity;
    /**
     * Item name field
     */
    private String itemName;
    /**
     * Price field
     */
    private BigDecimal price;
    /**
     * Total field
     */
    private BigDecimal total;
}
