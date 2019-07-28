package com.development.georgemcl.goaltracker.view.MainActivity;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.view.MainActionsView.MainActionsViewFragment;
import com.development.georgemcl.goaltracker.view.MainGoalView.MainGoalViewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main container class for all fragments
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.navigation) BottomNavigationView mNavigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new MainGoalViewFragment());
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(new MainActionsViewFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("");
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //start the MainGoalViewFragment by default
        replaceFragment(new MainGoalViewFragment());
    }

    /**
     * Called by fragments to toggle whether the back button needs to be shown.
     * @param show
     */
    public void showBackButton(boolean show){
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
    }

    /**
     * Replaces the fragment being shown in the main container, and adds it to the fragment backstack
     * @param fragment
     */
    public void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container, fragment).addToBackStack(null).commit();
    }

    public void setActionBarTitle(String title) {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setTitle(title);
        }
    }
}
