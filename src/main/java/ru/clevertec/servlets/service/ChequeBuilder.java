package ru.clevertec.servlets.service;

import lombok.RequiredArgsConstructor;
import ru.clevertec.servlets.exception.EntityNotFoundException;
import ru.clevertec.servlets.model.Cheque;
import ru.clevertec.servlets.model.ChequeItem;
import ru.clevertec.servlets.model.entity.DiscountCard;
import ru.clevertec.servlets.model.entity.Product;
import ru.clevertec.servlets.repository.CardRepository;
import ru.clevertec.servlets.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * Service class for cheques. Uses <b>card</b> and <b>product</b> services
 * to create cheque instance according to given parameters.
 *
 * @author Yuryeu Andrei
 */
@RequiredArgsConstructor
public class ChequeBuilder {
    /**
     * Product service field
     *
     * @see ProductRepository
     */
    private final ProductRepository productRepository;
    /**
     * Card service field
     *
     * @see CardRepository
     */
    private final CardRepository cardRepository;

    /**
     * Method for creating Cheque out of given requestParams
     *
     * @param requestParams parameters with ID and quantity of products and card ID
     * @return Cheque with products, total price, discount and total price with discount
     * @see Cheque
     */
    public Cheque buildCheque(Map<String, String> requestParams) throws EntityNotFoundException {
        Cheque cheque = new Cheque();
        fillItems(cheque, requestParams);

        BigDecimal discount = BigDecimal.valueOf(findCardDiscount(requestParams)).divide(BigDecimal.valueOf(100));
        BigDecimal total = findTotalForCheque(cheque.getProducts());
        cheque.setTotal(total.setScale(2, RoundingMode.CEILING));

        if (!discount.equals(BigDecimal.ZERO)) {
            cheque.setDiscount(total.multiply(discount).setScale(2, RoundingMode.CEILING));
        } else {
            cheque.setDiscount(BigDecimal.ZERO);
        }

        cheque.setTotalWithDiscount(cheque.getTotal().subtract(cheque.getDiscount()).setScale(2, RoundingMode.CEILING));
        return cheque;
    }

    /**
     * Private method for setting ChequeItems(products) into Cheque
     *
     * @param cheque        cheque to have products set
     * @param requestParams parameters with ID and quantity of products and card ID
     * @see ChequeBuilder#buildCheque(Map requestParams)
     */
    private void fillItems(Cheque cheque, Map<String, String> requestParams) throws EntityNotFoundException {
        for (String key : requestParams.keySet()) {
            if (!key.equals("card")) {
                Product product = productRepository.findById(Long.valueOf(key));
                if (product == null) {
                    throw new EntityNotFoundException("Product with id " + key + " does not exist");
                }
                int quantity = Integer.parseInt(requestParams.get(key));
                BigDecimal totalForItem = BigDecimal.valueOf(Integer.parseInt(requestParams.get(key)))
                        .multiply(product.getPrice());

                if (quantity > 5 && product.isOnSale()) {
                    totalForItem = totalForItem.multiply(BigDecimal.valueOf(0.9));
                }

                ChequeItem item = new ChequeItem(
                        quantity,
                        product.getName(),
                        product.getPrice(),
                        totalForItem.setScale(2, RoundingMode.CEILING));

                cheque.getProducts().add(item);
            }
        }
    }

    /**
     * Private method for finding discount of card(if present)
     *
     * @param requestParams parameters with ID and quantity of products and card ID
     * @return <b>0</b> - in case of absence of discount card, <b>other float</b> - in case of found card
     * @see ChequeBuilder#buildCheque(Map requestParams)
     */
    private int findCardDiscount(Map<String, String> requestParams) throws EntityNotFoundException {
        for (String key : requestParams.keySet()) {
            if (key.equals("card")) {
                DiscountCard card = cardRepository.findById(Long.valueOf(requestParams.get(key)));
                if (card == null) {
                    throw new EntityNotFoundException("Card with id " + requestParams.get(key) + " does not exist");
                }
                return card.getDiscount();
            }
        }
        return 0;
    }

    /**
     * Private method for finding total for the whole cheque
     *
     * @param items products with their quantity, price, total price and name
     * @return the sum of all product's total sum(quantity * price)
     * @see ChequeBuilder#buildCheque(Map requestParams)
     */
    private BigDecimal findTotalForCheque(List<ChequeItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (ChequeItem item : items) {
            total = total.add(item.getTotal());
        }
        return total;
    }
}
