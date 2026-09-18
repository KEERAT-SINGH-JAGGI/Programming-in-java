import java.util.Objects;

enum Category {
    SNACK,
    BEVERAGE
}

public abstract class MenuItem {
    private final int id;
    private final String name;
    private final double price;
    private final Category category;

    public MenuItem(int id, String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public abstract String getDescription();

    @Override
    public String toString() {
        return String.format(
                "%-4d %-25s %-12s Rs. %.2f",
                id, name, category, price
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MenuItem)) return false;

        MenuItem other = (MenuItem) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class Snack extends MenuItem {

    public Snack(int id, String name, double price) {
        super(id, name, price, Category.SNACK);
    }

    @Override
    public String getDescription() {
        return "Fresh campus snack";
    }
}

class Beverage extends MenuItem {

    public Beverage(int id, String name, double price) {
        super(id, name, price, Category.BEVERAGE);
    }

    @Override
    public String getDescription() {
        return "Refreshing beverage";
    }
}