package com.development.georgemcl.goaltracker.view.MainActivity

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast

import com.development.georgemcl.goaltracker.R
import com.development.georgemcl.goaltracker.view.MainActionsView.MainActionsViewFragment
import com.development.georgemcl.goaltracker.view.MainGoalView.MainGoalViewFragment

import butterknife.BindView
import butterknife.ButterKnife
import com.development.georgemcl.goaltracker.data.repository.ActionRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * Main container class for all fragments
 */
class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(MainGoalViewFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                replaceFragment(MainActionsViewFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        supportActionBar!!.title = ""
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //start the MainGoalViewFragment by default
        replaceFragment(MainGoalViewFragment())
        checkActionProgress()
    }

    /**
     * Called by fragments to toggle whether the back button needs to be shown.
     * @param show
     */
    fun showBackButton(show: Boolean) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(show)
    }

    /**
     * Replaces the fragment being shown in the main container, and adds it to the fragment backstack
     * @param fragment
     */
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_activity_fragment_container, fragment).addToBackStack(null).commit()
    }

    fun setActionBarTitle(title: String) {
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = title
        }
    }

    /**
     * Daily check to reset progress of daily repeat actions completed the previous day
     */
    private fun checkActionProgress() {
        val todayCal = Calendar.getInstance()
        val prefs = getSharedPreferences(getString(R.string.shared_prefs_file_key), Context.MODE_PRIVATE)

        val dateLastVisited = Date(prefs.getLong(SHARED_PREFS_DATE_LAST_VISITED_KEY, todayCal.timeInMillis))
        val dateLastVisitedCal = Calendar.getInstance()
        dateLastVisitedCal.time = dateLastVisited
        val actionRepo = ActionRepository(application)

        if (todayCal.get(Calendar.WEEK_OF_YEAR) != dateLastVisitedCal.get(Calendar.WEEK_OF_YEAR)
                || todayCal.get(Calendar.YEAR) != dateLastVisitedCal.get(Calendar.YEAR)) {
            Log.d(TAG, "checkActionProgress: different week")
            disposable.add(actionRepo.resetAllWeeklyActionProgress()
                    .subscribe({ completedActions ->
                        Log.d(TAG, "resetWeeklyActionProgress: $completedActions")
                    }, { e ->
                        e.printStackTrace()
                    }))
        }

        if (todayCal.get(Calendar.MONTH) != dateLastVisitedCal.get(Calendar.MONTH)) {
            disposable.add(actionRepo.resetAllMonthlyActionProgress()
                    .subscribe({ completedActions ->
                        Log.d(TAG, "resetAllMonthlyActionProgress: $completedActions")
                    }, { e ->
                        e.printStackTrace()
                    }))
        }

        if (todayCal.get(Calendar.DAY_OF_MONTH) != dateLastVisitedCal.get(Calendar.DAY_OF_MONTH) ||
                todayCal.get(Calendar.MONTH) != dateLastVisitedCal.get(Calendar.MONTH) ||
                todayCal.get(Calendar.YEAR) != dateLastVisitedCal.get(Calendar.YEAR)) {
            Log.d(TAG, "resetAllDailyActionProgress different day")
            disposable.add(actionRepo.resetAllDailyActionProgress()
                    .subscribe({ completedActions ->
                        Log.d(TAG, "resetAllDailyActionProgress: $completedActions")
                    }, { e ->
                        e.printStackTrace()
                    }))
        } else {
            Log.d(TAG, "checkActionProgress: same day")
        }


        prefs.edit().putLong(SHARED_PREFS_DATE_LAST_VISITED_KEY, todayCal.timeInMillis).apply()
    }


    companion object {
        private const val TAG = "MainActivity"
        private const val SHARED_PREFS_DATE_LAST_VISITED_KEY = "date_last_visited_millis"
    }
}
