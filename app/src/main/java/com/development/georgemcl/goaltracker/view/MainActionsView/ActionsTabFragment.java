package com.development.georgemcl.goaltracker.view.MainActionsView;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionsTabFragment extends Fragment {


    public ActionsTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_actions_tab, container, false);
        Bundle args = getArguments();
        if (args != null ) {
            ((TextView) view.findViewById(R.id.test)).setText(args.getString(Constants.KEY_TAB_SELECTED));
        }
        return view;
    }

}
