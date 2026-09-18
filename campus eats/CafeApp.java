import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CafeApp {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final List<MenuItem> menu =
            new ArrayList<>();

    private static final List<Order> orders =
            new ArrayList<>();

    private static final ExecutorService kitchen =
            Executors.newFixedThreadPool(3);

    public static void main(String[] args) {

        loadMenu();

        orders.addAll(Storage.loadOrders(menu));

        System.out.println("\n==============================================");
        System.out.println("             WELCOME TO CAMPUSEATS");
        System.out.println("          VIT Bhopal Campus Canteen");
        System.out.println("==============================================");

        boolean running = true;

        while (running) {

            printMainMenu();

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    showMenu();
                    break;

                case 2:
                    createOrder();
                    break;

                case 3:
                    showAllOrders();
                    break;

                case 4:
                    salesReport();
                    break;

                case 5:
                    Storage.saveOrders(orders);
                    break;

                case 6:
                    running = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice."
                    );
            }
        }

        shutdownKitchen();

        System.out.println(
                "\nThank you for using CampusEats!"
        );

        scanner.close();
    }

    private static void loadMenu() {

        menu.add(new Snack(
                1, "Samosa", 20
        ));

        menu.add(new Snack(
                2, "Veg Sandwich", 60
        ));

        menu.add(new Snack(
                3, "Paneer Roll", 80
        ));

        menu.add(new Snack(
                4, "French Fries", 70
        ));

        menu.add(new Snack(
                5, "Aloo Tikki", 40
        ));

        menu.add(new Beverage(
                6, "Masala Chai", 25
        ));

        menu.add(new Beverage(
                7, "Cold Coffee", 70
        ));

        menu.add(new Beverage(
                8, "Lemonade", 40
        ));

        menu.add(new Beverage(
                9, "Mango Shake", 80
        ));
    }

    private static void printMainMenu() {

        System.out.println("\n--------------- CAMPUSEATS ----------------");
        System.out.println("1. View Menu");
        System.out.println("2. Place Order");
        System.out.println("3. View Orders");
        System.out.println("4. Sales Report");
        System.out.println("5. Save Orders");
        System.out.println("6. Exit");
        System.out.println("--------------------------------------------");
    }

    private static void showMenu() {

        System.out.println("\n================ MENU ======================");
        System.out.printf(
                "%-4s %-25s %-12s %s%n",
                "ID", "ITEM", "CATEGORY", "PRICE"
        );
        System.out.println("---------------------------------------------");

        for (MenuItem item : menu) {
            System.out.println(item);
        }

        System.out.println("=============================================");
    }

    private static void createOrder() {

        Order order = new Order();

        System.out.println("\nCreating Order #" +
                order.getOrderId());

        boolean adding = true;

        while (adding) {

            showMenu();

            int itemId =
                    readInt("Enter item ID (0 to finish): ");

            if (itemId == 0) {
                adding = false;
                continue;
            }

            MenuItem item = findMenuItem(itemId);

            if (item == null) {
                System.out.println(
                        "Item not found."
                );
                continue;
            }

            int quantity =
                    readInt("Enter quantity: ");

            try {

                order.addItem(item, quantity);

                System.out.println(
                        quantity + " x " +
                        item.getName() +
                        " added."
                );

            } catch (CafeException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }

        if (order.isEmpty()) {

            System.out.println(
                    "Order cancelled - no items selected."
            );

            return;
        }

        order.printOrder();

        String confirm =
                readString("\nPlace this order? (y/n): ");

        if (confirm.equalsIgnoreCase("y")) {

            orders.add(order);

            System.out.println(
                    "\nOrder placed successfully!"
            );

            startKitchen(order);

        } else {

            order.setStatus(
                    OrderStatus.CANCELLED
            );

            System.out.println(
                    "Order cancelled."
            );
        }
    }

    private static void showAllOrders() {

        if (orders.isEmpty()) {

            System.out.println(
                    "\nNo orders available."
            );

            return;
        }

        System.out.println(
                "\n================ ORDERS ===================="
        );

        for (Order order : orders) {
            order.printOrder();
        }
    }

    private static void salesReport() {

        if (orders.isEmpty()) {

            System.out.println(
                    "\nNo sales data available."
            );

            return;
        }

        double totalSales =
                orders.stream()
                        .filter(o ->
                                o.getStatus() !=
                                OrderStatus.CANCELLED)
                        .mapToDouble(Order::getTotal)
                        .sum();

        long completedOrders =
                orders.stream()
                        .filter(o ->
                                o.getStatus() ==
                                OrderStatus.COMPLETED)
                        .count();

        long totalOrders =
                orders.stream()
                        .filter(o ->
                                o.getStatus() !=
                                OrderStatus.CANCELLED)
                        .count();

        double averageOrder =
                totalOrders == 0
                        ? 0
                        : totalSales / totalOrders;

        Map<String, Integer> itemSales =
                orders.stream()
                        .filter(o ->
                                o.getStatus() !=
                                OrderStatus.CANCELLED)
                        .flatMap(o ->
                                o.getLines().stream())
                        .collect(Collectors.groupingBy(
                                line ->
                                        line.getItem().getName(),
                                Collectors.summingInt(
                                        OrderLine::getQuantity
                                )
                        ));

        Map<Category, Double> categorySales =
                orders.stream()
                        .filter(o ->
                                o.getStatus() !=
                                OrderStatus.CANCELLED)
                        .flatMap(o ->
                                o.getLines().stream())
                        .collect(Collectors.groupingBy(
                                line ->
                                        line.getItem()
                                                .getCategory(),
                                Collectors.summingDouble(
                                        OrderLine::getSubtotal
                                )
                        ));

        System.out.println(
                "\n=============== SALES REPORT ==============="
        );

        System.out.printf(
                "Total Orders       : %d%n",
                totalOrders
        );

        System.out.printf(
                "Completed Orders   : %d%n",
                completedOrders
        );

        System.out.printf(
                "Total Sales        : Rs. %.2f%n",
                totalSales
        );

        System.out.printf(
                "Average Order      : Rs. %.2f%n",
                averageOrder
        );

        System.out.println(
                "\n----- ITEM SALES -----"
        );

        itemSales.entrySet()
                .stream()
                .sorted(
                        Map.Entry
                                .<String, Integer>comparingByValue()
                                .reversed()
                )
                .forEach(entry ->
                        System.out.println(
                                entry.getKey() +
                                " : " +
                                entry.getValue() +
                                " sold"
                        )
                );

        System.out.println(
                "\n----- CATEGORY SALES -----"
        );

        categorySales.forEach(
                (category, amount) ->
                        System.out.printf(
                                "%s : Rs. %.2f%n",
                                category,
                                amount
                        )
        );

        System.out.println(
                "============================================="
        );
    }

    private static void startKitchen(
            Order order) {

        kitchen.submit(() -> {

            try {

                System.out.println(
                        "\n[KITCHEN] Order #" +
                        order.getOrderId() +
                        " is being prepared..."
                );

                order.setStatus(
                        OrderStatus.PREPARING
                );

                Thread.sleep(3000);

                order.setStatus(
                        OrderStatus.READY
                );

                System.out.println(
                        "[KITCHEN] Order #" +
                        order.getOrderId() +
                        " is READY!"
                );

                Thread.sleep(2000);

                order.setStatus(
                        OrderStatus.COMPLETED
                );

                System.out.println(
                        "[KITCHEN] Order #" +
                        order.getOrderId() +
                        " completed."
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                order.setStatus(
                        OrderStatus.CANCELLED
                );

                System.out.println(
                        "[KITCHEN] Order #" +
                        order.getOrderId() +
                        " interrupted."
                );
            }
        });
    }

    private static MenuItem findMenuItem(
            int id) {

        return menu.stream()
                .filter(item ->
                        item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private static int readInt(
            String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private static String readString(
            String message) {

        System.out.print(message);

        return scanner.nextLine().trim();
    }

    private static void shutdownKitchen() {

        kitchen.shutdown();

        try {

            if (!kitchen.awaitTermination(
                    10,
                    TimeUnit.SECONDS)) {

                kitchen.shutdownNow();
            }

        } catch (InterruptedException e) {

            kitchen.shutdownNow();

            Thread.currentThread().interrupt();
        }
    }
}