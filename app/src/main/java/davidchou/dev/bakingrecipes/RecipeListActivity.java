package davidchou.dev.bakingrecipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.RecipeContent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepsActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        populateRecipesFromJson();

        if (findViewById(R.id.recipe_steps_secondary_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void populateRecipesFromJson() {

        // TODO: Modify this code so that it leverages Retrofit to pull the JSON data.
        Gson gson = new Gson();
        try {
            Type recipeListType = TypeToken.getParameterized(List.class, Recipe.class).getType();
            InputStream is = getResources().openRawResource(R.raw.baking_data);
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            mRecipes = gson.fromJson(reader, recipeListType);
            RecipeContent.populateRecipeMap(mRecipes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView
                .setAdapter(new RecipeRecyclerViewAdapter(this, mRecipes, mTwoPane));
    }

    public static class RecipeRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final List<Recipe> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = (Recipe) view.getTag();
                Log.v(RecipeListActivity.class.getSimpleName() + " Two Pane? ",
                      String.valueOf(mTwoPane));
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeStepsActivity.class);
                intent.putExtra(RecipeStepsFragment.ARG_RECIPE_ID, recipe.getId());
                intent.putExtra(RecipeStepsFragment.TWO_PANE_KEY, mTwoPane);
                context.startActivity(intent);
            }
        };

        RecipeRecyclerViewAdapter(
                RecipeListActivity parent,
                List<Recipe> recipes,
                boolean twoPane
        ) {
            mValues = recipes;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getName());

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
