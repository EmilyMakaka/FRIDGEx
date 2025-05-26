package models;

import java.util.List;

public class Recipe {
    private int recipeId;
    private String recipeName;
    private String recipeDescription;
    private int userId;
    private List<Ingredient> ingredients;
    private int cookingTime;

    public Recipe(int recipeId, String recipeName, String recipeDescription, int userId, List<Ingredient> ingredients) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.userId = userId;
        this.ingredients = ingredients;
    }

    public Recipe(String recipeName, String description, int userId) {
        this.recipeName = recipeName;
        this.recipeDescription = description;
        this.userId = userId;
    }

    public Recipe(int recipeId, String recipeName, String description, int userId) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeDescription = description;
        this.userId = userId;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return recipeName;
    }

    public String getDescription() {
        return recipeDescription;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    };

    public void setName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setDescription(String description) {
        this.recipeDescription = description;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Recipe() {
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getUserId() {
        return userId;
    }

}
