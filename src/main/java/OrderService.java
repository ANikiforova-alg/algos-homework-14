import java.util.List;

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
 * </ol>
 * <p>
 * Примечание: скидки применяются последовательно, не взаимно исключая друг друга.
 * Например, VIP-клиент с заказом на 1200₽ получит: 1200 * 0.9 = 1080 → 1080 - 50 = 1030.
 *
 */
public class OrderService {

    /**
     * Рассчитывает итоговую стоимость заказа с учетом типа клиента.
     *
     * @param items список товаров в заказе, не может быть null
     * @param type  тип клиента: "VIP", "NEW" или любой другой (без скидки)
     * @return итоговая стоимость заказа после применения всех скидок
     * @throws IllegalArgumentException если items == null
     */
    public double calc(List<Item> items, String type) {
        if (items == null) {
            throw new IllegalArgumentException("Items list cannot be null");
        }

        double s = 0;
        for (Item i : items) {
            s += i.getPrice() * i.getQuantity();
        }

        if (type.equals("VIP")) {
            s = s * 0.9;
        }

        if (type.equals("NEW")) {
            s = s * 0.95;
        }

        if (s > 1000) {
            s = s - 50;
        }

        return s;
    }
}
