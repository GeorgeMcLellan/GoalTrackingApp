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
import android.widget.TableLayout;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActionsViewFragment extends Fragment {

    private static final String TAG = "MainActionsViewFragment";

    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;


    public MainActionsViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_actions_view, container, false);
        ButterKnife.bind(this, view);


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
                Log.d(TAG, "onTabSelected: " +tab.getText());
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }


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
                    args.putString(Constants.KEY_TAB_SELECTED, "daily");
                    fragment.setArguments(args);
                    return fragment;
                }
                case 1: {
                    args.putString(Constants.KEY_TAB_SELECTED, "weekly");
                    fragment.setArguments(args);
                    return fragment;
                }
                case 2: {
                    args.putString(Constants.KEY_TAB_SELECTED, "monthly");
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
