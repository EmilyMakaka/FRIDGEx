import java.util.ArrayList;
import java.util.List;

public class RecipeDatabase {
    private List<Recipe> recipes = new ArrayList();

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return this.recipes;
    }

    public boolean deleteRecipe(String title) {
        return this.recipes.removeIf((recipe) -> recipe.getTitle().equalsIgnoreCase(title));
    }
}
