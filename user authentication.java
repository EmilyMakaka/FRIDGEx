
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String email;
    private List<String> recipes;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.recipes = new ArrayList<>();
    }

    public void createRecipe(String recipeName) {
        recipes.add(recipeName);
        System.out.println(username + " created a recipe: " + recipeName);
    }

    public void viewRecipes() {
        System.out.println(username + "'s Recipes:");
        for (String recipe : recipes) {
            System.out.println("- " + recipe);
        }
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRecipes() {
        return recipes;
    }
}
public class UserTest {
    public static void main(String[] args) {
        // Create dummy users
        User user1 = new User("faysal", "faysal@example.com");
        User user2 = new User("sara", "sara@example.com");

        // User 1 actions
        user1.createRecipe("Smocha");
        user1.createRecipe("ugali kuku");
        user1.viewRecipes();

        // User 2 actions
        user2.createRecipe("Chapo dondo");
        user2.viewRecipes();
    }
}

