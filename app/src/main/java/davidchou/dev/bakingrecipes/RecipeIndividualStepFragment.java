package davidchou.dev.bakingrecipes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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

    private PlayerView mPlayerView;
    private SimpleExoPlayer player;
    private String mVideoUrl;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(RecipeStepsFragment.ARG_RECIPE_ID)) {
            mRecipe = RecipeContent.RECIPE_MAP.get(getArguments().getInt(RecipeStepsFragment.ARG_RECIPE_ID));

            if (getArguments().containsKey(ARG_RECIPE_STEP_ID)) {
                mRecipeStep = mRecipe.getSteps().get(getArguments().getInt(ARG_RECIPE_STEP_ID));

                if (mRecipeStep != null) {
                    mVideoUrl = mRecipeStep.getVideoURL();

                    CollapsingToolbarLayout appBarLayout = this.getActivity().findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(mRecipeStep.getShortDescription());
                    }
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

        Context context = getContext();
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
        mPlayerView = rootView.findViewById(R.id.video_view);
        mPlayerView.setPlayer(player);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                                                                            Util.getUserAgent(context, "yourApplicationName"));

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(mVideoUrl));
        Log.v(RecipeIndividualStepFragment.class.getSimpleName() + "URL: ", mVideoUrl);

        // Prepare the player with the source.
        player.prepare(videoSource);

        return rootView;
    }
}
