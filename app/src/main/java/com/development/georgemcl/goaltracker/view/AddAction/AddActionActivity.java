package com.development.georgemcl.goaltracker.view.AddAction;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;

import java.text.ParseException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Used for adding and also editing an action
 */
public class AddActionActivity extends AppCompatActivity {
    private static final String TAG = "AddActionActivity";

    public static final String EXTRA_ACTION_TO_ADD = "ACTION_TO_ADD";
    public static final String EXTRA_ACTION_TO_EDIT = "ACTION_TO_EDIT";


    @BindView(R.id.add_action_name_edittext) EditText mActionNameEt;
    @BindView(R.id.add_action_repeat_switch) Switch mRepeatSwitch;
    @BindView(R.id.add_action_repeat_layout) LinearLayout mRepeatLayout;
    @BindView(R.id.add_action_repeat_per_time_period_spinner) Spinner mRepeatPerTimePeriodSpn;
    @BindView(R.id.add_action_repeat_unit_of_measurement_spinner) Spinner mRepeatUnitOfMeasurementSpn;
    @BindView(R.id.add_action_repeat_measurement_edittext) EditText mRepeatMeasurementEt;

    private int mParentGoalId;
    //If existing action is being edited
    private int mActionToEditId = -1;

    private ArrayAdapter<String> mRepeatPerTimePeriodAdapter;
    private ArrayAdapter<String> mRepeatUnitOfMeasurementAdapter;
    private int mRepeatTimeMinutes = -1;

    private Boolean dontResetMeasurementFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);
        ButterKnife.bind(this);


        mRepeatSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mRepeatLayout.setVisibility(View.VISIBLE);
            }else {
                mRepeatLayout.setVisibility(View.INVISIBLE);
            }
        });

        mRepeatPerTimePeriodAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.repeat_time_periods));
        mRepeatPerTimePeriodSpn.setAdapter(mRepeatPerTimePeriodAdapter);

        mRepeatUnitOfMeasurementAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.repeat_units_of_measurements));
        mRepeatUnitOfMeasurementSpn.setAdapter(mRepeatUnitOfMeasurementAdapter);

        if (getIntent().hasExtra(Constants.KEY_PARENT_GOAL_ID)){
            mParentGoalId = getIntent().getIntExtra(Constants.KEY_PARENT_GOAL_ID, -1);
        }

        if (getIntent().hasExtra(Constants.KEY_ACTION_TO_EDIT)) {
            populateFields((Action) getIntent().getSerializableExtra(Constants.KEY_ACTION_TO_EDIT));
            dontResetMeasurementFlag = true;
        }

        mRepeatUnitOfMeasurementSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!dontResetMeasurementFlag) {
                    mRepeatMeasurementEt.setText("");
                    mRepeatTimeMinutes = -1;
                }
                dontResetMeasurementFlag = false;
                mRepeatMeasurementEt.setFocusableInTouchMode(position == 0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        mRepeatMeasurementEt.setOnClickListener(v -> {
            if (mRepeatUnitOfMeasurementSpn.getSelectedItem().toString().equals(getString(R.string.repeat_hours_minutes))) {
                showTimePicker();
            }
        });



        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            String title;
            if (isEditingAction()) {
                title = "Edit Action";
            } else {
                title = "Add Action";
            }
            actionbar.setTitle(title);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActionActivity.this, android.R.style.Theme_Holo_Light_Dialog,
                (view, hourOfDay, minute) -> {
                    setRepeatTime(hourOfDay, minute);
                }, 0, 0, true);
        timePickerDialog.show();
        if (mRepeatTimeMinutes > 0) {
            timePickerDialog.updateTime(mRepeatTimeMinutes / 60, mRepeatTimeMinutes % 60);
        }
    }

    private void setRepeatTime(int hours, int minutes) {
        Log.d(TAG, "setRepeatTime: ");
        String timeString = hours + "h " + minutes + "m";
        mRepeatMeasurementEt.setText(timeString);
        mRepeatTimeMinutes = (hours * 60) + minutes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().hasExtra(Constants.KEY_ACTION_TO_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_action_bar_save, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_action_bar_add, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_bar_add_add ||
                item.getItemId() == R.id.menu_action_bar_save_save) {
            validateActionData();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void validateActionData(){
        String actionName = mActionNameEt.getText().toString();
        if (!actionName.isEmpty()) {
            Action action;
            if (mRepeatSwitch.isChecked()) {
                try {
                    if (!mRepeatMeasurementEt.getText().toString().isEmpty()
                            && ( mRepeatTimeMinutes > 0 || Integer.parseInt(mRepeatMeasurementEt.getText().toString()) > 0)){
                        int repeatAmount;
                        if (mRepeatTimeMinutes != -1) {
                            repeatAmount = mRepeatTimeMinutes;
                        } else {
                            repeatAmount = Integer.parseInt(mRepeatMeasurementEt.getText().toString());
                        }
                        action = new Action(actionName, mParentGoalId, repeatAmount,
                                mRepeatPerTimePeriodSpn.getSelectedItem().toString(),
                                mRepeatUnitOfMeasurementSpn.getSelectedItem().toString());
                        addOrEditAction(action);
                    }else {
                        Toast.makeText(AddActionActivity.this, getString(R.string.invalid_repeat_amount), Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(AddActionActivity.this, getString(R.string.invalid_repeat_amount), Toast.LENGTH_SHORT).show();
                }
            }else {
                action = new Action(actionName, mParentGoalId);
                addOrEditAction(action);
            }

        }
        else {
            Toast.makeText(AddActionActivity.this, getString(R.string.missing_name), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEditingAction() {
        return mActionToEditId != -1;
    }

    /**
     * Based on what the user had selected to do, this edits the action they're viewing,
     * or creates the new action in the database
     * @param action action to add/edit
     */
    private void addOrEditAction(Action action) {
        Intent replyIntent = new Intent();
        if (isEditingAction()){
            action.setId(mActionToEditId);
            replyIntent.putExtra(EXTRA_ACTION_TO_EDIT, action);
        } else {
            replyIntent.putExtra(EXTRA_ACTION_TO_ADD, action);
        }
        Log.i(TAG, "addAction: " + action);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    /**
     * If editing an action - populates fields with existing action data
     */
    private void populateFields(Action action) {
        mActionNameEt.setText(action.getActionName());
        mActionToEditId = action.getId();
        if (action.isRepeatAction()) {
            mRepeatSwitch.setChecked(true);
            mRepeatPerTimePeriodSpn.setSelection(mRepeatPerTimePeriodAdapter.getPosition(action.getRepeatTimePeriod()));
            mRepeatUnitOfMeasurementSpn.setSelection(mRepeatUnitOfMeasurementAdapter.getPosition(action.getRepeatUnitOfMeasurement()));
            if (action.getRepeatUnitOfMeasurement().equals(getString(R.string.repeat_hours_minutes))) {
                int repeatAmount = action.getRepeatAmount();
                setRepeatTime(repeatAmount / 60, repeatAmount % 60);
            } else {
                mRepeatMeasurementEt.setText(String.valueOf(action.getRepeatAmount()));
            }

        }
    }
}
