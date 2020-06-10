package davidchou.dev.bakingrecipes.network;

import java.util.List;

import davidchou.dev.bakingrecipes.data.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitRecipeAPI {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> loadRecipes();
}
