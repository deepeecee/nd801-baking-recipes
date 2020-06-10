package davidchou.dev.bakingrecipes;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.RecipeContent;
import davidchou.dev.bakingrecipes.data.RecipeIngredient;
import davidchou.dev.bakingrecipes.data.RecipeStep;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeStepsFragment}
 * in two-pane mode (on tablets) or a {@link RecipeStepsActivity}
 * on handsets.
 */
public class RecipeStepsFragment extends Fragment {

    public static final String ARG_RECIPE_ID = "recipe_id";
    public static final String TWO_PANE_KEY = "is_two_pane_key";

    private Recipe mRecipe;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private List<RecipeStep> mRecipeSteps;

    public RecipeStepsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: This needs to be modified such that "two pane" status is dictated by the
        //  original activity and passed to these fragments.


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

                mRecipeSteps = mRecipe.getSteps();
            }
        }

        if (getArguments().containsKey(TWO_PANE_KEY)) {
            mTwoPane = getArguments().getBoolean(TWO_PANE_KEY);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps_list, container, false);

        if (mRecipe != null) {
            Log.v(RecipeStepsFragment.class.getSimpleName() + "Inflation: ", mRecipe.getName());
            TextView ingredientsView =
                    (TextView) rootView.findViewById(R.id.recipe_ingredients);
            assert ingredientsView != null;
            for (RecipeIngredient ingredient : mRecipe.getIngredients()) {
                ingredientsView.append("- " + ingredient.toString() + "\n\n");
            }

            View recyclerView = rootView.findViewById(R.id.recipe_steps_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
        }

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView
                .setAdapter(
                        new RecipeStepsFragment.RecipeStepRecyclerViewAdapter(this, mRecipeSteps,
                                                                              mTwoPane, mRecipe));
    }

    public static class RecipeStepRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeStepsFragment.RecipeStepRecyclerViewAdapter.ViewHolder> {

        private final RecipeStepsFragment mParentFragment;
        private final List<RecipeStep> mValues;
        private final boolean mTwoPane;
        private final Recipe mRecipe;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeStep recipeStep = (RecipeStep) view.getTag();
                Log.v(RecipeStepsFragment.class.getSimpleName() + " Two Pane? ",
                      String.valueOf(mTwoPane));

                Bundle arguments = new Bundle();
                arguments.putInt(
                        RecipeStepsFragment.ARG_RECIPE_ID,
                        mRecipe.getId());
                arguments.putInt(
                        RecipeIndividualStepFragment.ARG_RECIPE_STEP_ID,
                        recipeStep.getId());
                RecipeIndividualStepFragment stepFragment = new RecipeIndividualStepFragment();
                stepFragment.setArguments(arguments);
                mParentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_video_container, stepFragment, "findThisFragment")
                        .addToBackStack(null).commit();
            }
        };

        RecipeStepRecyclerViewAdapter(
                RecipeStepsFragment parent,
                List<RecipeStep> recipes,
                boolean twoPane,
                Recipe mRecipe
        ) {
            mValues = recipes;
            mParentFragment = parent;
            mTwoPane = twoPane;
            this.mRecipe = mRecipe;
        }

        @Override
        public RecipeStepsFragment.RecipeStepRecyclerViewAdapter.ViewHolder onCreateViewHolder(
                ViewGroup parent, int viewType
        ) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_steps_list_content, parent, false);
            return new RecipeStepsFragment.RecipeStepRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(
                final RecipeStepsFragment.RecipeStepRecyclerViewAdapter.ViewHolder holder,
                int position
        ) {
            holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getShortDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.previous_step_button);
                mContentView = (TextView) view.findViewById(R.id.next_step_button);
            }
        }
    }
}
