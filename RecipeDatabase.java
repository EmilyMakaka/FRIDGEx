// RecipeDatabase.java
import java.util.ArrayList;
import java.util.List;

public class RecipeDatabase {
    private List<Recipe> recipes = new ArrayList<>();

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void printAllRecipes() {
        for (Recipe r : recipes) {
            r.printRecipe();
            System.out.println();
        }
    }
}
