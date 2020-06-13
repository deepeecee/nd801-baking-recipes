package davidchou.dev.bakingrecipes.data;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeContent {

    public static final Map<Integer, Recipe> RECIPE_MAP = new HashMap<Integer, Recipe>();

    public static void populateRecipeMap(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            RECIPE_MAP.put(recipe.getId(), recipe);
        }
    }
}
