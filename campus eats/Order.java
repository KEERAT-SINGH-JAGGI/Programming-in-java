import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

enum OrderStatus {
    QUEUED,
    PREPARING,
    READY,
    COMPLETED,
    CANCELLED
}

class OrderLine {
    private final MenuItem item;
    private int quantity;

    public OrderLine(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int amount) {
        quantity += amount;
    }

    public double getSubtotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format(
                "%-25s x%-3d Rs. %.2f",
                item.getName(), quantity, getSubtotal()
        );
    }
}

public class Order {
    private static int nextId = 1001;

    private final int orderId;
    private final List<OrderLine> lines;
    private final LocalDateTime orderTime;
    private OrderStatus status;

    public Order() {
        this.orderId = nextId++;
        this.lines = new ArrayList<>();
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.QUEUED;
    }

    public Order(int orderId, LocalDateTime orderTime, OrderStatus status) {
        this.orderId = orderId;
        this.lines = new ArrayList<>();
        this.orderTime = orderTime;
        this.status = status;

        if (orderId >= nextId) {
            nextId = orderId + 1;
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void addItem(MenuItem item, int quantity)
            throws InvalidQuantityException {

        if (quantity <= 0) {
            throw new InvalidQuantityException(
                    "Quantity must be greater than zero."
            );
        }

        for (OrderLine line : lines) {
            if (line.getItem().equals(item)) {
                line.increaseQuantity(quantity);
                return;
            }
        }

        lines.add(new OrderLine(item, quantity));
    }

    public void removeItem(int itemId)
            throws ItemNotFoundException {

        boolean removed = lines.removeIf(
                line -> line.getItem().getId() == itemId
        );

        if (!removed) {
            throw new ItemNotFoundException(
                    "Item is not present in this order."
            );
        }
    }

    public double getTotal() {
        return lines.stream()
                .mapToDouble(OrderLine::getSubtotal)
                .sum();
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public void printOrder() {

        System.out.println("\n==============================================");
        System.out.println("                 ORDER #" + orderId);
        System.out.println("==============================================");

        if (lines.isEmpty()) {
            System.out.println("No items in order.");
        } else {
            for (OrderLine line : lines) {
                System.out.println(line);
            }

            System.out.println("----------------------------------------------");
            System.out.printf("TOTAL: Rs. %.2f%n", getTotal());
        }

        System.out.println("STATUS: " + status);
        System.out.println("TIME  : " +
                orderTime.format(
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                ));
        System.out.println("==============================================");
    }
}