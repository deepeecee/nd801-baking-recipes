package davidchou.dev.bakingrecipes;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeStepsActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private final int FIRST_STEP_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send some data to another person.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipe_video_container) != null) {
            mTwoPane = true;
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(
                    RecipeStepsFragment.ARG_RECIPE_ID,
                    getIntent().getIntExtra(RecipeStepsFragment.ARG_RECIPE_ID, 1));

            arguments.putBoolean(RecipeStepsFragment.TWO_PANE_KEY,
                                 mTwoPane);

            arguments.putInt(
                    RecipeIndividualStepFragment.ARG_RECIPE_STEP_ID,
                    FIRST_STEP_ID);

            RecipeStepsFragment fragment = new RecipeStepsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_steps_secondary_container, fragment)
                    .commit();

            if (mTwoPane) {
                Log.v(RecipeStepsActivity.class.getSimpleName(), "In mTwoPane logic.");
                RecipeIndividualStepFragment individualFragment = new RecipeIndividualStepFragment();
                individualFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_video_container, individualFragment)
                        .commit();
            }
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
