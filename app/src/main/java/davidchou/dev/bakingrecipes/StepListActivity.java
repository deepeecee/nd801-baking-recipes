package davidchou.dev.bakingrecipes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import davidchou.dev.bakingrecipes.adapter.StepRecyclerViewAdapter;
import davidchou.dev.bakingrecipes.data.Ingredient;
import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.RecipeContent;
import davidchou.dev.bakingrecipes.data.Step;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class StepListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private int mRecipeId;
    private Recipe mRecipe;
    private List<Step> mSteps;

    private final int FIRST_STEP_ID = 0;

    public static final String ARG_RECIPE_ID = "recipe_id";
    public static final String TWO_PANE_KEY = "is_two_pane_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTwoPane = findViewById(R.id.recipe_video_container) != null;
        mRecipeId = getIntent().getIntExtra(ARG_RECIPE_ID, 1);
        mRecipe = RecipeContent.RECIPE_MAP.get(mRecipeId);

        Bundle arguments = new Bundle();
        arguments.putInt(
                ARG_RECIPE_ID,
                getIntent().getIntExtra(ARG_RECIPE_ID, 1));

        arguments.putBoolean(
                TWO_PANE_KEY,
                mTwoPane);

        arguments.putInt(
                IndividualStepFragment.ARG_RECIPE_STEP_ID,
                FIRST_STEP_ID);

        maybeSetupStepList(toolbar, arguments);
        maybeSetupVideoFragment(arguments);
    }

    private void maybeSetupStepList(Toolbar toolbar, Bundle arguments) {
        if (mRecipe != null && toolbar != null) {
            toolbar.setTitle(mRecipe.getName());
            mSteps = mRecipe.getSteps();
        }

        StepListFragment fragment = new StepListFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_steps_replaceable_container, fragment)
                .commit();
    }

    private void maybeSetupVideoFragment(Bundle arguments) {
        Log.v(StepListActivity.class.getSimpleName(), "Is Two Pane?" + mTwoPane);
        if (mTwoPane) {
            IndividualStepFragment individualFragment = new IndividualStepFragment();
            individualFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_video_container, individualFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
