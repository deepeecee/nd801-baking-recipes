package davidchou.dev.bakingrecipes.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import davidchou.dev.bakingrecipes.Constants;
import davidchou.dev.bakingrecipes.adapter.RecipeRecyclerViewAdapter;
import davidchou.dev.bakingrecipes.data.Ingredient;
import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.RecipeContent;

public class BakingRecipesWidgetService extends IntentService {

    private static final String ACTION_UPDATE_INGREDIENTS = "update_ingredients";

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 25) {
            String CHANNEL_ID = Constants.RECIPE_NOTIFICATION_CHANNEL;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                                                                  Constants.RECIPE_NOTIFICATION,
                                                                  NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(Constants.APP_NAME)
                    .setContentText(Constants.NOTIFICATION_TEXT).build();

            startForeground(1, notification);
        }
    }

    public BakingRecipesWidgetService(String name) {
        super(name);
    }
    public BakingRecipesWidgetService() {
        super(BakingRecipesWidgetService.class.getSimpleName());
    };

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS.equals(action)) {
                updateIngredientsInWidget();
            }
        }
    }

    public static void startRecipeUpdateService(Context context) {
        Intent intent = new Intent(context, BakingRecipesWidgetService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS);
        if(Build.VERSION.SDK_INT > 25) {
            ContextCompat.startForegroundService(context, intent);
        } else {
            context.startService(intent);
        }
    }

    private void updateIngredientsInWidget() {
        SharedPreferences sharedpreferences =
                getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        String ingredients = sharedpreferences.getString(Constants.MOST_RECENT_INGREDIENTS,
                                                        "No ingredients yet!");

        Log.v(BakingRecipesWidgetService.class.getSimpleName(), ingredients);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                                                                                BakingRecipesWidgetProvider.class));
        BakingRecipesWidgetProvider.updateWidgetIngredients(this, ingredients,
                                                            appWidgetManager,
                                                            appWidgetIds);
    }
}
