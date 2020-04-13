package com.development.georgemcl.goaltracker.view.AddAction

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.development.georgemcl.goaltracker.Constants
import com.development.georgemcl.goaltracker.R
import com.development.georgemcl.goaltracker.model.Action
import com.development.georgemcl.goaltracker.utils.KeyboardUtils

import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_add_action.*

/**
 * Used for adding and also editing an action
 */
class AddActionActivity : AppCompatActivity() {

    private var parentGoalId: Int = 0
    //If existing action is being edited
    private var actionToEditId = -1

    private lateinit var repeatPerTimePeriodAdapter: ArrayAdapter<String>
    private lateinit var repeatUnitOfMeasurementAdapter: ArrayAdapter<String>
    private var repeatTimeMinutes = -1

    private var dontResetMeasurementFlag: Boolean = false

    private val isEditingAction: Boolean
        get() = actionToEditId != -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_action)
        ButterKnife.bind(this)

        add_action_repeat_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                add_action_repeat_layout.visibility = View.VISIBLE
            } else {
                add_action_repeat_layout.visibility = View.INVISIBLE
            }
        }

        repeatPerTimePeriodAdapter = ArrayAdapter(
                this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.repeat_time_periods))
        add_action_repeat_per_time_period_spinner.adapter = repeatPerTimePeriodAdapter

        repeatUnitOfMeasurementAdapter = ArrayAdapter(
                this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.repeat_units_of_measurements))
        add_action_repeat_unit_of_measurement_spinner.adapter = repeatUnitOfMeasurementAdapter

        if (intent.hasExtra(Constants.KEY_PARENT_GOAL_ID)) {
            parentGoalId = intent.getIntExtra(Constants.KEY_PARENT_GOAL_ID, -1)
        }

        if (intent.hasExtra(Constants.KEY_ACTION_TO_EDIT)) {
            populateFields(intent.getSerializableExtra(Constants.KEY_ACTION_TO_EDIT) as Action)
            dontResetMeasurementFlag = true
        }

        add_action_repeat_unit_of_measurement_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (!dontResetMeasurementFlag) {
                    add_action_repeat_measurement_edittext.setText("")
                    repeatTimeMinutes = -1
                    KeyboardUtils.hideSoftKeyboard(this@AddActionActivity)
                }
                dontResetMeasurementFlag = false
                add_action_repeat_measurement_edittext.isFocusableInTouchMode = position == 0
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        add_action_repeat_measurement_edittext.setOnClickListener { v ->
            if (add_action_repeat_unit_of_measurement_spinner.selectedItem.toString() == getString(R.string.repeat_hours_minutes)) {
                showTimePicker()
            }
        }


        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (isEditingAction) {
                "Edit Action"
            } else {
                "Add Action"
            }
        }

    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(this@AddActionActivity,
                { view, hourOfDay, minute -> setRepeatTime(hourOfDay, minute) }, 0, 0, true)
        timePickerDialog.show()
        if (repeatTimeMinutes > 0) {
            timePickerDialog.updateTime(repeatTimeMinutes / 60, repeatTimeMinutes % 60)
        }
    }

    private fun setRepeatTime(hours: Int, minutes: Int) {
        Log.d(TAG, "setRepeatTime: ")
        val timeString = hours.toString() + "h " + minutes + "m"
        add_action_repeat_measurement_edittext.setText(timeString)
        repeatTimeMinutes = hours * 60 + minutes
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (intent.hasExtra(Constants.KEY_ACTION_TO_EDIT)) {
            menuInflater.inflate(R.menu.menu_action_bar_save, menu)
        } else {
            menuInflater.inflate(R.menu.menu_action_bar_add, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_action_bar_add_add || item.itemId == R.id.menu_action_bar_save_save) {
            validateActionData()
            return true
        } else if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    private fun validateActionData() {
        val actionName = add_action_name_edittext.text.toString()
        if (actionName.isNotEmpty()) {
            val action: Action
            if (add_action_repeat_switch.isChecked) {
                try {
                    Log.d(TAG, "repeat amount ${add_action_repeat_measurement_edittext.text}")
                    if (add_action_repeat_measurement_edittext.text.toString().isNotEmpty() && (repeatTimeMinutes > 0 || Integer.parseInt(add_action_repeat_measurement_edittext.text.toString()) > 0)) {
                        val repeatAmount = if (repeatTimeMinutes != -1) {
                            repeatTimeMinutes
                        } else {
                            Integer.parseInt(add_action_repeat_measurement_edittext.text.toString())
                        }
                        action = Action(actionName = actionName, parentGoalId = parentGoalId, isRepeatAction = true, repeatAmount = repeatAmount,
                               repeatTimePeriod = add_action_repeat_per_time_period_spinner.selectedItem.toString(),
                               repeatUnitOfMeasurement = add_action_repeat_unit_of_measurement_spinner.selectedItem.toString())
                        addOrEditAction(action)
                    } else {
                        Toast.makeText(this@AddActionActivity, getString(R.string.invalid_repeat_amount), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this@AddActionActivity,"EXEC $e", Toast.LENGTH_SHORT).show()
                }

            } else {
                action = Action(actionName = actionName, parentGoalId = parentGoalId)
                addOrEditAction(action)
            }

        } else {
            Toast.makeText(this@AddActionActivity, getString(R.string.missing_name), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Based on what the user had selected to do, this edits the action they're viewing,
     * or creates the new action in the database
     * @param action action to add/edit
     */
    private fun addOrEditAction(action: Action) {
        val replyIntent = Intent()
        if (isEditingAction) {
            action.id = actionToEditId
            replyIntent.putExtra(EXTRA_ACTION_TO_EDIT, action)
        } else {
            replyIntent.putExtra(EXTRA_ACTION_TO_ADD, action)
        }
        Log.i(TAG, "addAction: $action")
        setResult(RESULT_OK, replyIntent)
        finish()
    }

    /**
     * If editing an action - populates fields with existing action data
     */
    private fun populateFields(action: Action) {
        add_action_name_edittext.setText(action.actionName)
        actionToEditId = action.id
        if (action.isRepeatAction) {
            add_action_repeat_switch.isChecked = true
            add_action_repeat_per_time_period_spinner.setSelection(repeatPerTimePeriodAdapter.getPosition(action.repeatTimePeriod))
            add_action_repeat_unit_of_measurement_spinner.setSelection(repeatUnitOfMeasurementAdapter.getPosition(action.repeatUnitOfMeasurement))
            if (action.repeatUnitOfMeasurement == getString(R.string.repeat_hours_minutes)) {
                val repeatAmount = action.repeatAmount
                setRepeatTime(repeatAmount / 60, repeatAmount % 60)
            } else {
                add_action_repeat_measurement_edittext.setText(action.repeatAmount.toString())
            }

        }
    }

    companion object {
        private const val TAG = "AddActionActivity"
        const val EXTRA_ACTION_TO_ADD = "ACTION_TO_ADD"
        const val EXTRA_ACTION_TO_EDIT = "ACTION_TO_EDIT"
    }
}
