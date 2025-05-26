public class Recipe {
    private String title;
    private String instructions;
    private IngredientRecipe ingredientRecipe;

    public Recipe(String title, String instructions, IngredientRecipe ingredientRecipe) {
        this.title = title;
        this.instructions = instructions;
        this.ingredientRecipe = ingredientRecipe;
    }

    public String getTitle() {
        return this.title;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public IngredientRecipe getIngredientRecipe() {
        return this.ingredientRecipe;
    }
}
