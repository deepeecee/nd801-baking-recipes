package davidchou.dev.bakingrecipes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.RecipeContent;
import davidchou.dev.bakingrecipes.data.RecipeStep;

public class RecipeIndividualStepFragment extends Fragment {

    public static final String ARG_RECIPE_STEP_ID = "recipe_step_id";

    private Recipe mRecipe;
    private RecipeStep mRecipeStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(RecipeStepsFragment.ARG_RECIPE_ID)) {
            mRecipe = RecipeContent.RECIPE_MAP.get(getArguments().getInt(RecipeStepsFragment.ARG_RECIPE_ID));

            if (getArguments().containsKey(ARG_RECIPE_STEP_ID)) {
                mRecipeStep = mRecipe.getSteps().get(getArguments().getInt(ARG_RECIPE_STEP_ID));

                CollapsingToolbarLayout appBarLayout = this.getActivity().findViewById(R.id.toolbar_layout);
                if (appBarLayout != null && mRecipeStep != null) {
                    appBarLayout.setTitle(mRecipeStep.getShortDescription());
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        Log.v(RecipeIndividualStepFragment.class.getSimpleName(), "Created view for new " +
                "individual step fragment.");
        View rootView = inflater.inflate(R.layout.fragment_individual_step, container, false);
        return rootView;
    }
}
