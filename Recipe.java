// Recipe.java
import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe(String name) {
        this.name = name;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void printRecipe() {
        System.out.println("Recipe: " + name);
        for (Ingredient i : ingredients) {
            System.out.println(" - " + i);
        }
    }
}
