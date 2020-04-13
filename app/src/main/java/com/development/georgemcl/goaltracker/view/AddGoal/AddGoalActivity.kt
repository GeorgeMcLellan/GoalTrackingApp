package com.development.georgemcl.goaltracker.view.AddGoal

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.development.georgemcl.goaltracker.Constants
import com.development.georgemcl.goaltracker.R
import com.development.georgemcl.goaltracker.model.Goal

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar

import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_add_goal.*

/**
 * For adding goals/sub-goals
 */
class AddGoalActivity : AppCompatActivity() {

    private var dateFormat = SimpleDateFormat("dd MMMM yyyy")
    private var parentGoalId: Int = -1
    //If existing goal is being edited
    private var goalToEditId = -1
    private val isEditingGoal: Boolean
        get() = goalToEditId != -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)
        ButterKnife.bind(this)


        if (intent.hasExtra(Constants.KEY_PARENT_GOAL_ID)) {
            parentGoalId = intent.getIntExtra(Constants.KEY_PARENT_GOAL_ID, -1)
        }

        add_goal_date_textview.setOnClickListener { v ->
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this@AddGoalActivity,
                    { view, year1, month1, dayOfMonth ->
                        val cal = GregorianCalendar(year1, month1, dayOfMonth)
                        add_goal_date_textview.text = dateFormat.format(cal.time)
                        add_goal_date_clear.visibility = View.VISIBLE
                    }, year, month, day)
            datePickerDialog.show()
        }

        add_goal_date_clear.setOnClickListener { v ->
            add_goal_date_textview.text = ""
            add_goal_date_clear.visibility = View.INVISIBLE
        }

        if (intent.hasExtra(Constants.KEY_GOAL_TO_EDIT)) {
            populateFields(intent.getSerializableExtra(Constants.KEY_GOAL_TO_EDIT) as Goal)
        } else {
            setGoalDateToEndOfYear()
        }

        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            val title = if (isEditingGoal) {
                "Edit Goal"
            } else {
                "Add Goal"
            }
            actionbar.title = title
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEditingGoal) {
            menuInflater.inflate(R.menu.menu_action_bar_save, menu)
        } else {
            menuInflater.inflate(R.menu.menu_action_bar_add, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_action_bar_add_add || item.itemId == R.id.menu_action_bar_save_save) {
            validateGoalData()
            return true
        } else if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    private fun validateGoalData() {
        val goalName = add_goal_name_edittext.text.toString()
        val description = add_goal_description_edittext.text.toString()
        val dateToAchieve = add_goal_date_textview.text.toString()
        if (!goalName.isEmpty()) {
            if (!dateToAchieve.isEmpty()) {
                try {
                    val date = dateFormat.parse(add_goal_date_textview.text.toString())
                    val todaysDate = Calendar.getInstance().time
                    if (date.time > todaysDate.time) {
                        val goal = Goal(goalName = goalName, description = description, completionDate = dateToAchieve, parentGoalId = parentGoalId)
                        addOrEditGoal(goal)
                    } else {
                        Toast.makeText(this@AddGoalActivity, getString(R.string.date_too_early), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: ParseException) {
                    Toast.makeText(this@AddGoalActivity, getString(R.string.invalid_date), Toast.LENGTH_SHORT).show()
                }

            } else {
                val goal = Goal(goalName = goalName, description = description, completionDate = "", parentGoalId = parentGoalId)
                addOrEditGoal(goal)
            }
        } else {
            Toast.makeText(this@AddGoalActivity, getString(R.string.missing_name), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setGoalDateToEndOfYear() {
        val calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 31)
        add_goal_date_textview.text = dateFormat.format(calendar.time)
        add_goal_date_clear.visibility = View.VISIBLE
    }

    /**
     * Based on what the user had selected to do, this edits the goal they're viewing,
     * or creates the new goal in the database
     * @param goal goal to add/edit
     */
    private fun addOrEditGoal(goal: Goal) {

        val replyIntent = Intent()
        if (isEditingGoal) {
            goal.id = goalToEditId
            replyIntent.putExtra(EXTRA_GOAL_TO_EDIT, goal)
        } else {
            replyIntent.putExtra(EXTRA_GOAL_TO_ADD, goal)
        }

        replyIntent.putExtra(EXTRA_GOAL_TO_ADD, goal)
        setResult(RESULT_OK, replyIntent)
        finish()
    }


    /**
     * If editing a goal - populates fields with existing goal data
     */
    private fun populateFields(goal: Goal) {
        Log.d(TAG, "populateFields: $goal")
        goalToEditId = goal.id
        add_goal_name_edittext.setText(goal.goalName)
        add_goal_date_textview.text = goal.completionDate
        if (goal.completionDate.isEmpty()) {
            add_goal_date_clear.visibility = View.INVISIBLE
        } else {
            add_goal_date_clear.visibility = View.VISIBLE
        }
        add_goal_description_edittext.setText(goal.description)
        add_goal_date_clear.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = "AddGoalActivity"
        const val EXTRA_GOAL_TO_ADD = "GOAL_TO_ADD"
        const val EXTRA_GOAL_TO_EDIT = "GOAL_TO_EDIT"
    }
}
