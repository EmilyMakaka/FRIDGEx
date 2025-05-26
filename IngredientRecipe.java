package models;

public class IngredientRecipe {
    private int recipeId;
    private int ingredientId;
    private double quantity;
    private int unitId;

    public IngredientRecipe(int recipeId, int ingredientId, double quantity, int unitId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.unitId = unitId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
