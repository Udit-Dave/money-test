import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartTest {

    private ShoppingCart cart;
    private MockInventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new MockInventory();
        cart = new ShoppingCart(inventory);
    }

    @Test
    void testAddItem() {
        cart.addItem("apple", 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
        assertEquals("apple", cart.getItems().get(0).getName());
    }

    @Test
    void testRemoveItem() {
        cart.addItem("banana", 3);
        cart.removeItem("banana", 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(1, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testRemoveItemCompletely() {
        cart.addItem("orange", 1);
        cart.removeItem("orange", 1);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testCalculateTotal() {
        cart.addItem("apple", 2);  // $1.00 each
        cart.addItem("banana", 3); // $0.50 each
        assertEquals(3.50, cart.calculateTotal(), 0.001);
    }

    @Test
    void testCheckout() {
        cart.addItem("apple", 2);
        cart.addItem("banana", 3);
        assertTrue(cart.checkout());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testCheckoutInsufficientInventory() {
        cart.addItem("apple", 10); // Only 5 in inventory
        assertFalse(cart.checkout());
        assertEquals(1, cart.getItems().size());
    }

    // Main class being tested
    public class ShoppingCart {
        private List<Item> items;
        private Inventory inventory;

        public ShoppingCart(Inventory inventory) {
            this.items = new ArrayList<>();
            this.inventory = inventory;
        }

        public void addItem(String name, int quantity) {
            items.add(new Item(name, quantity));
        }

        public void removeItem(String name, int quantity) {
            items.removeIf(item -> item.getName().equals(name) && (item.getQuantity() <= quantity));
            items.forEach(item -> {
                if (item.getName().equals(name)) {
                    item.setQuantity(item.getQuantity() - quantity);
                }
            });
        }

        public List<Item> getItems() {
            return items;
        }

        public double calculateTotal() {
            return items.stream()
                    .mapToDouble(item -> item.getQuantity() * inventory.getPrice(item.getName()))
                    .sum();
        }

        public boolean checkout() {
            if (items.stream().allMatch(item -> inventory.hasStock(item.getName(), item.getQuantity()))) {
                items.forEach(item -> inventory.removeStock(item.getName(), item.getQuantity()));
                items.clear();
                return true;
            }
            return false;
        }
    }

    // Item class
    public class Item {
        private String name;
        private int quantity;

        public Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    // Inventory interface
    public interface Inventory {
        double getPrice(String itemName);
        boolean hasStock(String itemName, int quantity);
        void removeStock(String itemName, int quantity);
    }

    // Mock implementation of Inventory
    public class MockInventory implements Inventory {
        private List<Item> stock;

        public MockInventory() {
            stock = new ArrayList<>();
            stock.add(new Item("apple", 5));
            stock.add(new Item("banana", 10));
            stock.add(new Item("orange", 7));
        }

        @Override
        public double getPrice(String itemName) {
            switch (itemName) {
                case "apple":
                    return 1.00;
                case "banana":
                    return 0.50;
                case "orange":
                    return 0.75;
                default:
                    return 0.00;
            }
        }

        @Override
        public boolean hasStock(String itemName, int quantity) {
            return stock.stream()
                    .filter(item -> item.getName().equals(itemName))
                    .anyMatch(item -> item.getQuantity() >= quantity);
        }

        @Override
        public void removeStock(String itemName, int quantity) {
            stock.stream()
                    .filter(item -> item.getName().equals(itemName))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(item.getQuantity() - quantity));
        }
    }
}