package services;

import models.Recipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeSearchEngine {

    public List<Recipe> searchByName(List<Recipe> recipes, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String normalizedKeyword = keyword.toLowerCase().trim();
        return recipes.stream()
                .filter(recipe -> recipe.getName() != null &&
                        recipe.getName().toLowerCase().contains(normalizedKeyword))
                .collect(Collectors.toList());
    }

    public List<Recipe> searchByIngredient(List<Recipe> recipes, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String normalizedKeyword = keyword.toLowerCase().trim();
        return recipes.stream()
                .filter(recipe -> recipe.getIngredients() != null &&
                        recipe.getIngredients().stream()
                                .anyMatch(ingredient -> ingredient.getName() != null &&
                                        ingredient.getName().toLowerCase().contains(normalizedKeyword)))
                .collect(Collectors.toList());
    }

    public List<Recipe> suggestSimilarRecipes(List<Recipe> recipes, String keyword, List<Recipe> popularRecipes) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return popularRecipes != null ? popularRecipes : new ArrayList<>();
        }

        // First try direct matches
        List<Recipe> nameMatches = searchByName(recipes, keyword);
        if (!nameMatches.isEmpty()) {
            return nameMatches;
        }

        // Then try ingredient matches
        List<Recipe> ingredientMatches = searchByIngredient(recipes, keyword);
        if (!ingredientMatches.isEmpty()) {
            return ingredientMatches;
        }

        // Try more lenient matching
        String[] keywords = keyword.toLowerCase().split(" ");
        List<Recipe> lenientMatches = recipes.stream()
                .filter(recipe -> {
                    String recipeName = recipe.getName() != null ? recipe.getName().toLowerCase() : "";
                    return Arrays.stream(keywords).anyMatch(word -> recipeName.contains(word) ||
                            (recipe.getIngredients() != null &&
                                    recipe.getIngredients().stream()
                                            .anyMatch(ingredient -> ingredient.getName() != null &&
                                                    ingredient.getName().toLowerCase().contains(word))));
                })
                .collect(Collectors.toList());

        if (!lenientMatches.isEmpty()) {
            return lenientMatches;
        }

        // Fallback to popular recipes
        return popularRecipes != null ? popularRecipes : new ArrayList<>();
    }
}