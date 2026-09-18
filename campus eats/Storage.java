import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    private static final Path FILE =
            Paths.get("orders.csv");

    public static void saveOrders(List<Order> orders) {

        List<String> lines = new ArrayList<>();

        lines.add(
                "orderId,orderTime,status,itemId,itemName,category,price,quantity"
        );

        for (Order order : orders) {

            for (OrderLine line : order.getLines()) {

                MenuItem item = line.getItem();

                String row = String.join(",",
                        String.valueOf(order.getOrderId()),
                        order.getOrderTime().toString(),
                        order.getStatus().name(),
                        String.valueOf(item.getId()),
                        item.getName().replace(",", " "),
                        item.getCategory().name(),
                        String.valueOf(item.getPrice()),
                        String.valueOf(line.getQuantity())
                );

                lines.add(row);
            }
        }

        try {
            Files.write(FILE, lines);
            System.out.println("Orders saved successfully.");

        } catch (IOException e) {
            System.out.println(
                    "Error saving orders: " + e.getMessage()
            );
        }
    }

    public static List<Order> loadOrders(List<MenuItem> menu) {

        List<Order> orders = new ArrayList<>();

        if (!Files.exists(FILE)) {
            return orders;
        }

        try {

            List<String> lines = Files.readAllLines(FILE);

            for (int i = 1; i < lines.size(); i++) {

                String[] data = lines.get(i).split(",");

                if (data.length < 8) {
                    continue;
                }

                int orderId = Integer.parseInt(data[0]);

                LocalDateTime time =
                        LocalDateTime.parse(data[1]);

                OrderStatus status =
                        OrderStatus.valueOf(data[2]);

                int itemId =
                        Integer.parseInt(data[3]);

                int quantity =
                        Integer.parseInt(data[7]);

                MenuItem item = findItem(menu, itemId);

                if (item == null) {
                    continue;
                }

                Order order = findOrder(orders, orderId);

                if (order == null) {

                    order = new Order(
                            orderId,
                            time,
                            status
                    );

                    orders.add(order);
                }

                order.addItem(item, quantity);
            }

            System.out.println(
                    orders.size() + " previous order(s) loaded."
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading orders: " + e.getMessage()
            );
        }

        return orders;
    }

    private static MenuItem findItem(
            List<MenuItem> menu,
            int id) {

        for (MenuItem item : menu) {
            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    private static Order findOrder(
            List<Order> orders,
            int id) {

        for (Order order : orders) {
            if (order.getOrderId() == id) {
                return order;
            }
        }

        return null;
    }
}