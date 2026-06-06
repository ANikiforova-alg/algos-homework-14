import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    private final OrderService service = new OrderService();

    @Test
    void shouldCalculateTotalForEmptyList() {
        List<Item> items = List.of();
        double result = service.calc(items, "REGULAR");
        assertEquals(0.0, result);
    }

    @Test
    void shouldCalculateTotalForSingleItem() {
        List<Item> items = List.of(new Item("Book", 100.0, 2));
        double result = service.calc(items, "REGULAR");
        assertEquals(200.0, result);
    }

    @Test
    void shouldApplyVIPDiscount() {
        List<Item> items = List.of(new Item("Laptop", 1000.0, 1));
        double result = service.calc(items, "VIP");
        assertEquals(900.0, result); // 1000 * 0.9
    }

    @Test
    void shouldApplyNEWDiscount() {
        List<Item> items = List.of(new Item("Phone", 500.0, 1));
        double result = service.calc(items, "NEW");
        assertEquals(475.0, result); // 500 * 0.95
    }

    @Test
    void shouldApplyFixedDiscountOver1000() {
        List<Item> items = List.of(new Item("TV", 1100.0, 1));
        double result = service.calc(items, "REGULAR");
        assertEquals(1050.0, result); // 1100 - 50
    }

    @Test
    void shouldApplyBothVIPAndFixedDiscount() {
        List<Item> items = List.of(new Item("TV", 1200.0, 1));
        double result = service.calc(items, "VIP");
        assertEquals(1030.0, result); // 1200 * 0.9 = 1080 → 1080 - 50 = 1030
    }

    @Test
    void shouldNotApplyDiscountIfBelow1000() {
        List<Item> items = List.of(new Item("Pen", 10.0, 50));
        double result = service.calc(items, "VIP");
        assertEquals(445.5, result); // 500 * 0.9 * 0.99 = 445.5 — <1000, no fixed discount
    }

    @Test
    void shouldApplyExtra1PercentDiscountForMoreThan10Items() {
        List<Item> items = List.of(
                new Item("Pen", 10.0, 11) // 11 товаров
        );
        double result = service.calc(items, "REGULAR");
        double expected = 110.0 * 0.99; // 110 * 0.99 = 108.9
        assertEquals(expected, result, 0.001);
    }
}
