package davidchou.dev.bakingrecipes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import davidchou.dev.bakingrecipes.adapter.StepRecyclerViewAdapter;
import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.RecipeContent;
import davidchou.dev.bakingrecipes.data.Ingredient;
import davidchou.dev.bakingrecipes.data.Step;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link StepListFragment}
 * in two-pane mode (on tablets) or a {@link StepListActivity}
 * on handsets.
 */
public class StepListFragment extends Fragment {

    private Recipe mRecipe;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private List<Step> mSteps;

    public StepListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedpreferences =
                getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        int recipeId = sharedpreferences.getInt(Constants.MOST_RECENT_RECIPE_ID,
                                                1);

        mRecipe = RecipeContent.RECIPE_MAP.get(recipeId);

        if (mRecipe != null) {
            Activity activity = this.getActivity();

            Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitle(mRecipe.getName());
            }

            mSteps = mRecipe.getSteps();
        }

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        mTwoPane = sharedPreferences.getBoolean(Constants.IS_TWO_PANE,
                                             false);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);

        if (mRecipe != null) {
            TextView ingredientsView =
                    (TextView) rootView.findViewById(R.id.recipe_ingredients);
            assert ingredientsView != null;
            for (Ingredient ingredient : mRecipe.getIngredients()) {
                ingredientsView.append("- " + ingredient.toString() + "\n");
            }

            RecyclerView recyclerView = rootView.findViewById(R.id.recipe_steps_list);
            assert recyclerView != null;
            setupRecyclerView(recyclerView);
        }

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView
                .setAdapter(
                        new StepRecyclerViewAdapter(this, mSteps,
                                                    mTwoPane, mRecipe));
    }
}
