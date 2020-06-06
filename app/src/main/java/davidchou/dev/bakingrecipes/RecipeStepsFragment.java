package davidchou.dev.bakingrecipes;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.RecipeContent;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepsActivity}
 * on handsets.
 */
public class RecipeStepsFragment extends Fragment {

    public static final String ARG_RECIPE_ID = "recipe_id";

    private Recipe mRecipe;

    public RecipeStepsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (Recipe recipe : RecipeContent.RECIPE_MAP.values()) {
            Log.v(RecipeStepsFragment.class.getSimpleName(), recipe.getName());
        }

        if (getArguments().containsKey(ARG_RECIPE_ID)) {
            mRecipe = RecipeContent.RECIPE_MAP.get(getArguments().getInt(ARG_RECIPE_ID));

            if (mRecipe != null) {
                Log.v(RecipeStepsFragment.class.getSimpleName() + "Creation: ", mRecipe.getName());
                Activity activity = this.getActivity();
                CollapsingToolbarLayout appBarLayout =
                        (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mRecipe.getName());
                }
            }
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // TODO: We will need to change this later to populate important recipe steps.
        if (mRecipe != null) {
            Log.v(RecipeStepsFragment.class.getSimpleName() + "Inflation: ", mRecipe.getName());
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mRecipe.getName());
        }

        return rootView;
    }
}
