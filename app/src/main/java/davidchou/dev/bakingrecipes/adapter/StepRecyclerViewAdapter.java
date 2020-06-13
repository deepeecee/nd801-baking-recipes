package davidchou.dev.bakingrecipes.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import davidchou.dev.bakingrecipes.IndividualStepFragment;
import davidchou.dev.bakingrecipes.R;
import davidchou.dev.bakingrecipes.StepListActivity;
import davidchou.dev.bakingrecipes.StepListFragment;
import davidchou.dev.bakingrecipes.data.Recipe;
import davidchou.dev.bakingrecipes.data.Step;

public class StepRecyclerViewAdapter
        extends RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder> {

    private final StepListFragment mParentFragment;
    private final List<Step> mValues;
    private final boolean mTwoPane;
    private final Recipe mRecipe;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step step = (Step) view.getTag();

            Bundle arguments = new Bundle();
            arguments.putInt(
                    StepListActivity.ARG_RECIPE_ID,
                    mRecipe.getId());
            arguments.putInt(
                    IndividualStepFragment.ARG_RECIPE_STEP_ID,
                    step.getId());
            IndividualStepFragment stepFragment = new IndividualStepFragment();
            stepFragment.setArguments(arguments);

            if (mTwoPane) {
                mParentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_video_container, stepFragment, "findThisFragment")
                        .addToBackStack(null).commit();
            } else {
                mParentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_steps_replaceable_container, stepFragment,
                           "findThisFragment")
                        .addToBackStack(null).commit();
            }
        }
    };

    public StepRecyclerViewAdapter(
            StepListFragment parent,
            List<Step> recipes,
            boolean twoPane,
            Recipe mRecipe
    ) {
        mValues = recipes;
        mParentFragment = parent;
        mTwoPane = twoPane;
        this.mRecipe = mRecipe;
    }

    @Override
    public StepRecyclerViewAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_content, parent, false);
        return new StepRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            final StepRecyclerViewAdapter.ViewHolder holder,
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
            mIdView = (TextView) view.findViewById(R.id.step_id);
            mContentView = (TextView) view.findViewById(R.id.step_list_content);
        }
    }
}