package davidchou.dev.bakingrecipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import davidchou.dev.bakingrecipes.R;
import davidchou.dev.bakingrecipes.StepListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingRecipesWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(
            Context context, String ingredients, AppWidgetManager appWidgetManager,
            int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views =
                new RemoteViews(context.getPackageName(), R.layout.baking_recipes_widget);

        // Update the text of the ingredients widget.
        if (ingredients == null) {
            ingredients = "No ingredients yet!";
        }
        Log.v(BakingRecipesWidgetProvider.class.getSimpleName(), ingredients);

        views.setTextViewText(R.id.ingredients_widget_text, ingredients);

        // Update the pending intent of the ingredients widget.
        Intent intent = new Intent(context, StepListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intent, 0);
        views.setOnClickPendingIntent(R.id.ingredients_widget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BakingRecipesWidgetService.startRecipeUpdateService(context);
    }

    public static void updateWidgetIngredients(Context context, String ingredients,
                                     AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds){
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, ingredients, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

