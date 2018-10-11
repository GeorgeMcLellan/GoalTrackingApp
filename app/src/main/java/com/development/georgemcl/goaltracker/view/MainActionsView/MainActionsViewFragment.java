package com.development.georgemcl.goaltracker.view.MainActionsView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.view.ActionTab.ActionsTabFragment;
import com.development.georgemcl.goaltracker.view.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main container for the actions tabs, where the user can see all of their repeat actions
 * for each repeat time period.
 */
public class MainActionsViewFragment extends Fragment {

    private static final String TAG = "MainActionsViewFragment";

    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.main_actions_tabs)
    TabLayout mTabLayout;

    public MainActionsViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_actions_view, container, false);
        ButterKnife.bind(this, view);

        if (getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).showBackButton(false);
        }


        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());

        final PagerAdapter adapter = new PagerAdapter
                (getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: " + tab.getText());
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        return view;
    }

    /**
     * FragmentStatePagerAdapter implementation to persist 3 instances of ActionsTabFragment for each time period.
     * Integrates with the tablayout to allow for easy navigation and robust performance.
     */
    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        private String[] mTabTitles = new String[] {"daily", "weekly", "monthly"};

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new ActionsTabFragment();
            Bundle args = new Bundle();
            switch (position){
                case 0 : {
                    args.putString(Constants.KEY_TAB_SELECTED, getString(R.string.per_day));
                    fragment.setArguments(args);
                    return fragment;
                }
                case 1: {
                    args.putString(Constants.KEY_TAB_SELECTED, getString(R.string.per_week));
                    fragment.setArguments(args);
                    return fragment;
                }
                case 2: {
                    args.putString(Constants.KEY_TAB_SELECTED, getString(R.string.per_month));
                    fragment.setArguments(args);
                    return fragment;
                }
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
