package ru.clevertec.servlets.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Cheque class
 *
 * @author Yuryeu Andrei
 */
@Data
public class Cheque {
    /**
     * Products field
     *
     * @see ChequeItem
     */
    private List<ChequeItem> products = new ArrayList<>();
    /**
     * Total field
     */
    private BigDecimal total;
    /**
     * Discount field
     */
    private BigDecimal discount;
    /**
     * Total with discount field
     */
    private BigDecimal totalWithDiscount;
}
