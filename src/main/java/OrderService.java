import java.util.List;
import java.util.Objects;

/**
 * Сервис для расчета итоговой стоимости заказа с учетом скидок.
 * <p>
 * Рассчитывает общую сумму заказа по формуле:
 * <pre>
 *   total = Σ(item.price * item.quantity)
 * </pre>
 * Затем применяет скидки в следующем порядке:
 * <ol>
 *   <li>10% скидка для клиентов типа "VIP"</li>
 *   <li>5% скидка для клиентов типа "NEW"</li>
 *   <li>Фиксированная скидка 50₽, если итоговая сумма > 1000₽</li>
 *   <li>1% скидка, если товаров > 10</li>
 * </ol>
 * <p>
 * Примечание: скидки применяются последовательно, не взаимно исключая друг друга.
 * Например, VIP-клиент с заказом на 1200₽ получит: 1200 * 0.9 = 1080 → 1080 - 50 = 1030.
 *
 */
public class OrderService {

    private static final double VIP_DISCOUNT_RATE = 0.9;
    private static final double NEW_DISCOUNT_RATE = 0.95;
    private static final double FIXED_DISCOUNT_THRESHOLD = 1000.0;
    private static final double FIXED_DISCOUNT_AMOUNT = 50.0;
    private static final double EXTRA_DISCOUNT_THRESHOLD = 10.0;
    private static final double EXTRA_DISCOUNT_RATE = 0.99; // 1% скидка

    /**
     * Рассчитывает итоговую стоимость заказа с учетом типа клиента.
     *
     * @param items список товаров в заказе, не может быть null
     * @param type  тип клиента: "VIP", "NEW" или любой другой (без скидки)
     * @return итоговая стоимость заказа после применения всех скидок
     * @throws IllegalArgumentException если items == null
     */
    public double calc(List<Item> items, String type) {
        Objects.requireNonNull(items, "Items list cannot be null");

        double subtotal = calculateSubtotal(items);
        int totalItemCount = getTotalItemCount(items);
        double discountedTotal = applyDiscounts(subtotal, type, totalItemCount);
        return discountedTotal;
    }

    private double calculateSubtotal(List<Item> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private double applyDiscounts(double subtotal, String type, int totalItemCount) {
        double total = subtotal;

        if ("VIP".equals(type)) {
            total *= VIP_DISCOUNT_RATE;
        }

        if ("NEW".equals(type)) {
            total *= NEW_DISCOUNT_RATE;
        }

        if (total > FIXED_DISCOUNT_THRESHOLD) {
            total -= FIXED_DISCOUNT_AMOUNT;
        }

        if (totalItemCount > EXTRA_DISCOUNT_THRESHOLD) {
            total *= EXTRA_DISCOUNT_RATE;
        }

        return total;
    }

    private int getTotalItemCount(List<Item> items) {
        return items.stream()
                .mapToInt(Item::getQuantity)
                .sum();
    }
}
