package davidchou.dev.bakingrecipes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.RecipeContent;
import davidchou.dev.bakingrecipes.data.Step;

public class IndividualStepFragment extends Fragment {

    public static final String ARG_RECIPE_STEP_ID = "recipe_step_id";

    private Recipe mRecipe;
    private Step mStep;

    private PlayerView mPlayerView;
    private SimpleExoPlayer player;
    private String mVideoUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(StepListActivity.ARG_RECIPE_ID)) {
            mRecipe = RecipeContent.RECIPE_MAP.get(getArguments().getInt(StepListActivity.ARG_RECIPE_ID));

            if (getArguments().containsKey(ARG_RECIPE_STEP_ID)) {
                mStep = mRecipe.getSteps().get(getArguments().getInt(ARG_RECIPE_STEP_ID));

                if (mStep != null) {
                    mVideoUrl = mStep.getVideoURL();

                    Toolbar toolbar = this.getActivity().findViewById(R.id.toolbar);
                    if (toolbar != null) {
                        toolbar.setTitle(mStep.getShortDescription());
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
        Log.v(IndividualStepFragment.class.getSimpleName(), "Created view for new " +
                "individual step fragment.");
        View rootView = inflater.inflate(R.layout.fragment_individual_step, container, false);

        if (mStep != null) {
            initializePlayer(rootView);
            ((TextView) rootView.findViewById(R.id.step_instruction))
                    .setText("Step " + mStep.getId() + ": " + mStep.getDescription());

            if (mStep.getId() == 0) {
                ((Button) rootView.findViewById(R.id.previous_step_button))
                        .setVisibility(View.INVISIBLE);
            }

            if (mStep.getId() == mRecipe.getSteps().size()-1) {
                ((Button) rootView.findViewById(R.id.next_step_button))
                        .setVisibility(View.INVISIBLE);
            }
        }

        return rootView;
    }

    private void initializePlayer(View rootView) {
        Context context = getContext();
        player = new SimpleExoPlayer.Builder(context).build();
        mPlayerView = rootView.findViewById(R.id.video_view);
        mPlayerView.setPlayer(player);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                                                                            Util.getUserAgent(context, "yourApplicationName"));

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(mVideoUrl));
        Log.v(IndividualStepFragment.class.getSimpleName() + "URL: ", mVideoUrl);

        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            player.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            player.release();
        }
    }
}
