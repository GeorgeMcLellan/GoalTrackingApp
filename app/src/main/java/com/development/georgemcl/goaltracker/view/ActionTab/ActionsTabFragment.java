package com.development.georgemcl.goaltracker.view.ActionTab;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;
import com.development.georgemcl.goaltracker.view.ViewGoal.ViewGoalViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionsTabFragment extends Fragment {


    private String mActionTabCategory;
    private ActionTabViewModel mActionsTabViewModel;

    public ActionsTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_actions_tab, container, false);
        Bundle args = getArguments();

        mActionTabCategory = args.getString(Constants.KEY_TAB_SELECTED);

        String actionTab = "";
        if (mActionTabCategory.equals(getString(R.string.daily))){
            actionTab = "per day";
        }
        else if (mActionTabCategory.equals(getString(R.string.weekly))){
            actionTab = "per week";

        }
        else if (mActionTabCategory.equals(getString(R.string.monthly))) {
            actionTab = "per month";

        }

        mActionsTabViewModel = ViewModelProviders.of(this).get(ActionTabViewModel.class);
        mActionsTabViewModel.setActionRepeatCategory(actionTab);

        mActionsTabViewModel.getActionsForRepeatCategory().observe(this, new Observer<List<Action>>() {
            @Override
            public void onChanged(@Nullable List<Action> actions) {

            }
        });

        return view;
    }

}
