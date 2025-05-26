// Ingredient.java
public class Ingredient {
    private String name;
    private double quantity;
    private Unit unit;

    public Ingredient(String name, double quantity, Unit unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String toString() {
        return quantity + " " + unit.getName() + " of " + name;
    }
}
