import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import dao.UserDAO;
import dao.RecipeDAO;
import dao.IngredientDAO;
import dao.IngredientRecipeDAO;
import dao.UnitDAO;
import models.User;
import services.RecipeSearchEngine;
import models.Unit;
import models.Recipe;
import models.IngredientRecipe;
import models.Ingredient;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        try {
            // DAO instances
            UserDAO userDAO = new UserDAO();
            RecipeDAO recipeDAO = new RecipeDAO();
            IngredientDAO ingredientDAO = new IngredientDAO();
            UnitDAO unitDAO = new UnitDAO();
            IngredientRecipeDAO irDAO = new IngredientRecipeDAO();

            // === USER INPUT ===
            System.out.println("=== Register New User/Admin ===");
            System.out.print("Enter username: ");
            String username = input.nextLine();

            System.out.print("Enter email: ");
            String email = input.nextLine();

            System.out.print("Enter password: ");
            String password = input.nextLine();

            System.out.print("Enter role (admin/user): ");
            String role = input.nextLine().trim().toLowerCase();

            // Registration or Login
            User loggedInUser = null;
            int userId = -1;

            if (role.equals("admin") || role.equals("user")) {
                // Try to authenticate first
                System.out.println("=== Login ===");
                loggedInUser = userDAO.authenticateUser(email, password);
                if (loggedInUser != null) {
                    System.out.println("Login successful! Welcome, " + loggedInUser.getUsername());
                    userId = loggedInUser.getUserId();
                } else {
                    // Register new user
                    User user = new User(username, email, password, role);
                    userId = userDAO.addUser(user);
                    System.out.println("User registered with ID: " + userId);
                    loggedInUser = userDAO.authenticateUser(email, password);
                }
            } else {
                System.out.println("Invalid role. Exiting.");
                return;
            }
            boolean authenticated = false;
            while (!authenticated) {
                System.out.print("Enter email: ");
                String loginEmail = input.nextLine();

                System.out.print("Enter password: ");
                String loginPassword = input.nextLine();

                loggedInUser = userDAO.login(loginEmail, loginPassword);
                if (loggedInUser != null) {
                    System.out.println(" Login successful. Welcome, " + loggedInUser.getUsername() + "!");
                    authenticated = true;
                    // Proceed to next step (dashboard, etc.)
                } else {

                    int maxAttempts = 3;
                    int attempts = 0;

                    while (attempts < maxAttempts && loggedInUser == null) {
                        System.out.print("Enter email: ");
                        loginEmail = input.nextLine();

                        System.out.print("Enter password: ");
                        loginPassword = input.nextLine();

                        try {
                            loggedInUser = userDAO.login(loginEmail, loginPassword);
                        } catch (SQLException e) {
                            System.out.println("Database error: " + e.getMessage());
                            return;
                        }

                        if (loggedInUser == null) {
                            attempts++;
                            int remaining = maxAttempts - attempts;
                            System.out.println(" Invalid email or password. " +
                                    (remaining > 0 ? "Attempts left: " + remaining : "No attempts left."));
                        }
                    }

                    if (loggedInUser != null) {
                        System.out.println(" Login successful. Welcome, " + loggedInUser.getUsername() + "!");
                        // Proceed to dashboard or next screen
                    } else {
                        System.out.println(" Too many failed attempts. Access denied.");
                    }

                }
            }

            // Admin dashboard
            if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
                while (true) {
                    System.out.println("\n=== ADMIN DASHBOARD ===");
                    System.out.println("1. View all users");
                    System.out.println("2. Delete user");
                    System.out.println("3. View all recipes");
                    System.out.println("4. Delete recipe");
                    System.out.println("5. Search recipes");
                    System.out.println("6. Search recipes by ingredient");
                    System.out.println("7. Logout");

                    System.out.print("Choose an option: ");
                    String choice = input.nextLine();

                    List<Recipe> allRecipes = recipeDAO.getAllRecipes();

                    switch (choice) {
                        case "1":
                            List<User> users = userDAO.getAllUsers();
                            System.out.println("=== Registered Users ===");
                            for (User u : users) {
                                System.out.println("ID: " + u.getUserId() + ", Name: " + u.getUsername() + ", Role: "
                                        + u.getRole());
                            }
                            break;

                        case "2":
                            System.out.print("Enter user ID to delete: ");
                            int deleteUserId = Integer.parseInt(input.nextLine());
                            boolean userDeleted = userDAO.deleteUser(deleteUserId);
                            System.out.println(userDeleted ? " User deleted" : " Failed to delete user");
                            break;

                        case "3":
                            List<Recipe> recipes = recipeDAO.getAllRecipes();
                            System.out.println("=== All Recipes ===");
                            for (Recipe r : recipes) {
                                System.out.println("ID: " + r.getRecipeId() + ", Name: " + r.getName());
                            }
                            break;

                        case "4":
                            System.out.print("Enter recipe ID to delete: ");
                            int deleteRecipeId = Integer.parseInt(input.nextLine());
                            boolean deleted = recipeDAO.deleteRecipe(deleteRecipeId);
                            System.out.println(deleted ? " Recipe deleted" : "Failed to delete");
                            break;

                        case "5":
                            System.out.print("Enter keyword to search: ");
                            String keyword = input.nextLine();
                            List<Recipe> matches = new java.util.ArrayList<>();
                            for (Recipe r : allRecipes) {
                                if (r.getName().toLowerCase().contains(keyword.toLowerCase())) {
                                    matches.add(r);
                                }
                            }
                            for (Recipe r : matches) {
                                System.out.println(" " + r.getName() + " - " + r.getDescription());
                            }
                            break;
                        case "6":
                            performSearch(allRecipes, input);
                            break;
                        case "7":
                            System.out.println(" Logging out...");
                            return;

                        default:
                            System.out.println("Invalid choice.");
                    }
                }
            }
            if (!loggedInUser.getRole().equalsIgnoreCase("admin")) {
                List<Recipe> userRecipes = recipeDAO.getRecipesByUserId(userId);
                while (true) {
                    System.out.println("\n=== USER DASHBOARD ===");
                    System.out.println("1. Create a new recipe");
                    System.out.println("2. View my recipes");
                    System.out.println("3. Logout");
                    System.out.println("4. Edit a recipe");
                    System.out.println("5. Delete a recipe");
                    System.out.println("6. Edit an ingredient in a recipe");
                    System.out.println("7. Search recipes");
                    System.out.println("8. Exit");

                    System.out.print("Choose an option: ");
                    String choice = input.nextLine();

                    switch (choice) {
                        case "1":
                            // Create recipe flow (you've done this already)
                            System.out.print("Enter recipe name: ");
                            String newRecipeName = input.nextLine();
                            System.out.print("Enter recipe description: ");
                            String newDescription = input.nextLine();

                            Recipe newRecipe = new Recipe(newRecipeName, newDescription, userId);
                            int newRecipeId = recipeDAO.addRecipe(newRecipe);
                            System.out.println(" Recipe created with ID: " + newRecipeId);

                            // Ingredient loop
                            while (true) {
                                System.out.println("\n=== Add Ingredient to Recipe ===");
                                System.out.print("Enter ingredient name: ");
                                String ingName = input.nextLine();

                                Ingredient ingredient = new Ingredient(ingName);
                                int ingId = ingredientDAO.addIngredient(ingredient);

                                System.out.print("Enter unit name: ");
                                String unitName = input.nextLine();
                                System.out.print("Enter abbreviation: ");
                                String abbrev = input.nextLine();

                                // Check for existing unit
                                Unit existingUnit = unitDAO.getUnitByNameAndAbbreviation(unitName, abbrev);
                                int unitId;
                                if (existingUnit != null) {
                                    unitId = existingUnit.getUnitId();
                                    System.out.println("Reusing unit with ID: " + unitId);
                                } else {
                                    Unit newUnit = new Unit(unitName, abbrev);
                                    unitId = unitDAO.addUnit(newUnit);
                                }

                                double quantity = 0;
                                while (true) {
                                    System.out.print("Enter quantity: ");
                                    if (input.hasNextDouble()) {
                                        quantity = input.nextDouble();
                                        input.nextLine();
                                        break;
                                    } else {
                                        System.out.println("Invalid number. Try again.");
                                        input.next();
                                    }
                                }

                                IngredientRecipe ir = new IngredientRecipe(newRecipeId, ingId, quantity, unitId);
                                irDAO.addIngredientToRecipe(ir);
                                System.out.println("Ingredient added.");

                                System.out.print("Add another ingredient? (yes/no): ");
                                String more = input.nextLine().trim().toLowerCase();
                                if (!more.equals("yes"))
                                    break;
                            }
                            // Refresh userRecipes after adding a new recipe
                            userRecipes = recipeDAO.getRecipesByUserId(userId);
                            break;

                        case "2":
                            // View recipes + ingredients
                            System.out.println("=== Your Recipes ===");
                            userRecipes = recipeDAO.getRecipesByUserId(userId);
                            for (Recipe r : userRecipes) {
                                System.out.println("\n▶ " + r.getName() + ": " + r.getDescription());
                                irDAO.getIngredientsByRecipeId(r.getRecipeId());
                            }
                            break;

                        case "3":
                            System.out.println("👋 Logged out.");
                            return;
                        case "4":
                            System.out.print("Enter recipe ID to edit: ");
                            int editId = Integer.parseInt(input.nextLine());

                            System.out.print("Enter new name: ");
                            String newName = input.nextLine();

                            System.out.print("Enter new description: ");
                            String newDesc = input.nextLine();

                            Recipe updatedRecipe = new Recipe(newName, newDesc, loggedInUser.getUserId());
                            updatedRecipe.setRecipeId(editId);
                            boolean success = recipeDAO.updateRecipe(updatedRecipe);
                            System.out.println(success ? "Recipe updated." : " Update failed.");
                            break;
                        case "5":
                            System.out.print("Enter recipe ID to delete: ");
                            int deleteId = Integer.parseInt(input.nextLine());

                            boolean deleted = recipeDAO.deleteRecipe(deleteId);
                            System.out.println(deleted ? "Recipe deleted." : "Delete failed.");
                            break;
                        case "6":
                            System.out.print("Enter recipe ID: ");
                            int rid = Integer.parseInt(input.nextLine());
                            irDAO.getIngredientsByRecipeId(rid);

                            System.out.print("Enter ingredient ID to edit: ");
                            int ingId = Integer.parseInt(input.nextLine());

                            System.out.print("Enter new quantity: ");
                            double qty = input.nextDouble();
                            input.nextLine();

                            System.out.print("Enter new unit name: ");
                            String uName = input.nextLine();
                            System.out.print("Enter abbreviation: ");
                            String abbr = input.nextLine();

                            Unit existing = unitDAO.getUnitByNameAndAbbreviation(uName, abbr);
                            int unitId;
                            if (existing != null) {
                                unitId = existing.getUnitId();
                            } else {
                                unitId = unitDAO.addUnit(new Unit(uName, abbr));
                                System.out.println("New unit added with ID: " + unitId);
                            }

                            IngredientRecipe updated = new IngredientRecipe(rid, ingId, qty, unitId);
                            boolean changed = irDAO.updateIngredientInRecipe(updated);
                            System.out.println(changed ? " Ingredient updated." : " Update failed.");
                            break;
                        case "7":
                            performSearch(userRecipes, input);
                            break;
                        case "8":
                            System.out.println("Exiting the application. Goodbye!");
                            return;

                        default:
                            System.out.println("Invalid option. Try again.");
                    }
                }

            }

        } catch (Exception e) {
            System.out.println(" An error occurred:");
            e.printStackTrace();
        } finally {
            input.close();
        }
    }

    public static void performSearch(List<Recipe> recipes, Scanner input) {
        RecipeSearchEngine engine = new RecipeSearchEngine();

        System.out.println("\nSearch by:");
        System.out.println("1. Name");
        System.out.println("2. Ingredient");
        System.out.print("Enter your choice: ");
        String choice = input.nextLine();

        if (choice.equals("1")) {
            System.out.print("Enter recipe name keyword: ");
            String keyword = input.nextLine();
            List<Recipe> results = engine.searchByName(recipes, keyword);
            System.out.println("\n=== Search Results ===");
            results.forEach(r -> System.out.println(" " + r.getName() + " - " + r.getDescription()));
        } else if (choice.equals("2")) {
            System.out.print("Enter ingredient keyword: ");
            String keyword = input.nextLine();
            List<Recipe> results = engine.searchByIngredient(recipes, keyword);
            System.out.println("\n=== Ingredient Match Results ===");
            results.forEach(r -> System.out.println(" " + r.getName()));
        } else {
            System.out.println(" Invalid choice.");
        }
    }

    public static void performSearch(List<Recipe> recipes, Scanner input, IngredientRecipeDAO irDAO,
            List<Recipe> popularRecipes) throws SQLException {
        RecipeSearchEngine engine = new RecipeSearchEngine();

        System.out.println("\nSearch by:");
        System.out.println("1. Name");
        System.out.println("2. Ingredient");
        System.out.print("Enter your choice: ");
        String choice = input.nextLine();

        List<Recipe> results = new ArrayList<>();

        if (choice.equals("1")) {
            System.out.print("Enter recipe name keyword: ");
            String keyword = input.nextLine();
            results = engine.searchByName(recipes, keyword);
        } else if (choice.equals("2")) {
            System.out.print("Enter ingredient keyword: ");
            String keyword = input.nextLine();
            results = engine.searchByIngredient(recipes, keyword);
        } else {
            System.out.println(" Invalid choice.");
            return;
        }

        if (results.isEmpty()) {
            System.out.println("No exact matches found. Suggesting similar recipes...");
            System.out.println(" Suggestions:");
            results = engine.suggestSimilarRecipes(recipes, " ", popularRecipes);
        }

        System.out.println("\n=== Results ===");
        for (Recipe r : results) {
            System.out.println(" " + r.getName() + " - " + r.getDescription());
            irDAO.getIngredientsByRecipeId(r.getRecipeId());
        }
    }

}
