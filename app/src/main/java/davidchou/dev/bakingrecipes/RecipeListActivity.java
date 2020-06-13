package davidchou.dev.bakingrecipes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.view.View;
import android.widget.Toast;

import davidchou.dev.bakingrecipes.adapter.RecipeRecyclerViewAdapter;
import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.RecipeContent;
import davidchou.dev.bakingrecipes.network.RetrofitRecipeAPI;
import davidchou.dev.bakingrecipes.network.RetrofitRecipeInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
 * lead to a {@link StepListActivity} representing
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

        populateRecipesFromNetworkJson();
    }

    private void populateRecipesFromNetworkJson() {
        RetrofitRecipeAPI service = RetrofitRecipeInstance.getInstance().create(RetrofitRecipeAPI.class);
        Call<List<Recipe>> call = service.loadRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mRecipes = response.body();
                RecipeContent.populateRecipeMap(mRecipes);

                View recyclerView = findViewById(R.id.recipe_list);
                assert recyclerView != null;
                setupRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(RecipeListActivity.this, "Couldn't retrieve recipes! Please make " +
                                       "sure you are connected to the internet.",
                               Toast.LENGTH_SHORT).show();
                populateRecipesFromLocalJson();
            }
        });
    }

    private void populateRecipesFromLocalJson() {
        Gson gson = new Gson();
        try {
            Type recipeListType = TypeToken.getParameterized(List.class, Recipe.class).getType();
            InputStream is = getResources().openRawResource(R.raw.baking_data);
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            mRecipes = gson.fromJson(reader, recipeListType);
            RecipeContent.populateRecipeMap(mRecipes);
            setupRecyclerView();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        recyclerView
                .setAdapter(new RecipeRecyclerViewAdapter(this, mRecipes));
    }
}
