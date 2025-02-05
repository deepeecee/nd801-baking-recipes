package davidchou.dev.bakingrecipes.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import davidchou.dev.bakingrecipes.Constants;
import davidchou.dev.bakingrecipes.R;
import davidchou.dev.bakingrecipes.RecipeListActivity;
import davidchou.dev.bakingrecipes.StepListActivity;
import davidchou.dev.bakingrecipes.data.Ingredient;
import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.widget.BakingRecipesWidgetService;

import static android.content.Context.MODE_PRIVATE;
import static davidchou.dev.bakingrecipes.StepListActivity.ARG_RECIPE_ID;

public class RecipeRecyclerViewAdapter
        extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private final RecipeListActivity mParentActivity;
    private final List<Recipe> mValues;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Recipe recipe = (Recipe) view.getTag();
            Context context = view.getContext();
            Intent intent = new Intent(context, StepListActivity.class);

            // Update the widget to have the most recently used ingredients of the clicked recipe.
            SharedPreferences.Editor editor =
                    context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE).edit();
            editor.putString(Constants.MOST_RECENT_RECIPE, recipe.getName());
            editor.putInt(Constants.MOST_RECENT_RECIPE_ID, recipe.getId());

            List<Ingredient> ingredients = recipe.getIngredients();
            StringBuilder ingredientsString = new StringBuilder();
            if (ingredients != null ) {
                for (Ingredient ingredient : ingredients) {
                    ingredientsString.append(ingredient.toString() + "\n");
                }
            }

            editor.putString(Constants.MOST_RECENT_INGREDIENTS, ingredientsString.toString());
            editor.apply();

            context.startActivity(intent);

            BakingRecipesWidgetService.startRecipeUpdateService(context);
        }
    };

    public RecipeRecyclerViewAdapter(
            RecipeListActivity parent,
            List<Recipe> recipes
    ) {
        mValues = recipes;
        mParentActivity = parent;
    }

    @Override
    public RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new RecipeRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mContentView.setText(mValues.get(position).getName());

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.list_item_content);
        }
    }
}