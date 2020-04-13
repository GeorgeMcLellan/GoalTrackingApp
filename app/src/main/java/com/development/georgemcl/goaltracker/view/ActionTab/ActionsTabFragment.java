package com.development.georgemcl.goaltracker.view.ActionTab;


import androidx.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.view.AddAction.AddActionActivity;
import com.development.georgemcl.goaltracker.view.ViewGoal.ViewGoalRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

import static android.app.Activity.RESULT_OK;

/**
 * Represents a daily/weekly/monthly tab in the tablayout in MainActionsView.
 */
public class ActionsTabFragment extends Fragment implements ViewGoalRecyclerViewAdapter.OnItemSelectedListener{

    private static final String TAG = "ActionsTabFragment";
    private static final int EDIT_ACTION_REQUEST_CODE = 60;

    //daily/weekly/monthly
    private String mActionTabCategory;
    private ActionTabViewModel mActionsTabViewModel;
    private ViewGoalRecyclerViewAdapter mRecyclerViewAdapter;
    private CompositeDisposable disposables = new CompositeDisposable();

    @BindView(R.id.actions_tab_recycler_view)
    RecyclerView mRecyclerView;


    public ActionsTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_actions_tab, container, false);

        ButterKnife.bind(this, view);

        mRecyclerViewAdapter = new ViewGoalRecyclerViewAdapter(getContext(), this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mActionTabCategory = getArguments().getString(Constants.KEY_TAB_SELECTED);

        mActionsTabViewModel = new ViewModelProvider(this).get(ActionTabViewModel.class);
        mActionsTabViewModel.setActionRepeatCategory(mActionTabCategory);

        mActionsTabViewModel.getActionsForRepeatCategory().observe(getViewLifecycleOwner(), new Observer<List<Action>>() {
            @Override
            public void onChanged(@Nullable List<Action> actions) {
                mRecyclerViewAdapter.setActions(actions);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EDIT_ACTION_REQUEST_CODE: {
                    Log.d(TAG, "onActivityResult: edit");
                    updateAction((Action) data.getSerializableExtra(AddActionActivity.EXTRA_ACTION_TO_EDIT));
                    break;
                }
            }
        }
    }

    //RecyclerView listener

    @Override
    public void onSubGoalSelected(int goalId) {}

    @Override
    public void onActionSelected(int actionId) {}

    @Override
    public void onActionEdit(Action action) {
        Intent intent = new Intent(getContext(), AddActionActivity.class);
        intent.putExtra(Constants.KEY_PARENT_GOAL_ID, action.getParentGoalId());
        intent.putExtra(Constants.KEY_ACTION_TO_EDIT, action);
        startActivityForResult(intent, EDIT_ACTION_REQUEST_CODE);
    }

    @Override
    public void updateAction(Action action) {
        disposables.add(mActionsTabViewModel.editAction(action)
                .subscribe(
                        () -> Toast.makeText(getContext(), "Action updated", Toast.LENGTH_SHORT).show(),
                        e -> Toast.makeText(getContext(), "Failed to update action. Please try again", Toast.LENGTH_SHORT).show()
                ));
    }

    @Override
    public void deleteAction(Action action) {
        disposables.add(mActionsTabViewModel.deleteAction(action)
                .subscribe(
                        () -> Toast.makeText(getContext(), "Action deleted", Toast.LENGTH_SHORT).show(),
                        e -> Toast.makeText(getContext(), "Failed to delete action. Please try again", Toast.LENGTH_SHORT).show()
                ));
    }
}
