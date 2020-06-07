package davidchou.dev.bakingrecipes.data;

public class RecipeIngredient {
    private float quantity;
    private String measure;
    private String ingredient;

    public RecipeIngredient(float quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String toString() {
        return this.quantity + " " + this.measure.toLowerCase() + " " + this.ingredient.toLowerCase();
    }
}
