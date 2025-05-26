import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // === STEP 1: Create Units ===
        Unit grams = new Unit("grams");
        Unit ml = new Unit("ml");
        Unit pcs = new Unit("pcs");
        Unit litres = new Unit("litres");

        // === STEP 2: Sample Recipes ===
        Recipe pancakes = new Recipe("Pancakes");
        pancakes.addIngredient(new Ingredient("Flour", 200, grams));
        pancakes.addIngredient(new Ingredient("Milk", 300, ml));
        pancakes.addIngredient(new Ingredient("Eggs", 2, pcs));

        Recipe applePie = new Recipe("Apple Pie");
        applePie.addIngredient(new Ingredient("Apples", 3, pcs));
        applePie.addIngredient(new Ingredient("Sugar", 100, grams));
        applePie.addIngredient(new Ingredient("Butter", 50, grams));

        // === STEP 3: Store in Database ===
        RecipeDatabase db = new RecipeDatabase();
        db.addRecipe(pancakes);
        db.addRecipe(applePie);

        // === STEP 4: Ask User to Add a Recipe ===
        Scanner scanner = new Scanner(System.in);
        System.out.print("Would you like to add your own recipe? (yes/no): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("yes")) {
            System.out.print("Enter recipe name: ");
            String recipeName = scanner.nextLine();
            Recipe userRecipe = new Recipe(recipeName);

            while (true) {
                System.out.print("Enter ingredient name (or 'done'): ");
                String ingName = scanner.nextLine();
                if (ingName.equalsIgnoreCase("done")) break;

                System.out.print("Enter quantity: ");
                double qty = Double.parseDouble(scanner.nextLine());

                System.out.print("Enter unit (grams/ml/pcs/litres): ");
                String unitName = scanner.nextLine();
                Unit customUnit = new Unit(unitName);

                Ingredient ing = new Ingredient(ingName, qty, customUnit);
                userRecipe.addIngredient(ing);
            }

            db.addRecipe(userRecipe);
        }

        // === STEP 5: Show All Recipes ===
        System.out.println("\n--- All Recipes ---");
        db.printAllRecipes();
    }
}
