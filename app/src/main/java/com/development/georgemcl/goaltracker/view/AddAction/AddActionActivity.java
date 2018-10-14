package com.development.georgemcl.goaltracker.view.AddAction;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Action;

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
    @BindView(R.id.add_action_submit_fab) FloatingActionButton mSubmitFab;

    private int mParentGoalId;
    //If existing action is being edited
    private int mActionToEditId;

    private ArrayAdapter<String> mRepeatPerTimePeriodAdapter;
    private ArrayAdapter<String> mRepeatUnitOfMeasurementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(Constants.KEY_PARENT_GOAL_ID)){
            mParentGoalId = getIntent().getIntExtra(Constants.KEY_PARENT_GOAL_ID, -1);
        }

        mRepeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mRepeatLayout.setVisibility(View.VISIBLE);
                }else {
                    mRepeatLayout.setVisibility(View.INVISIBLE);
                }
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

        if (getIntent().hasExtra(Constants.KEY_ACTION_TO_EDIT)) {
            populateFields((Action) getIntent().getSerializableExtra(Constants.KEY_ACTION_TO_EDIT));
        }

        mSubmitFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionName = mActionNameEt.getText().toString();
                if (!actionName.isEmpty()) {
                    Action action;
                    if (mRepeatSwitch.isChecked()) {
                        if (!mRepeatMeasurementEt.getText().toString().isEmpty()
                                && Integer.parseInt(mRepeatMeasurementEt.getText().toString()) > 0) {
                                    action = new Action(actionName, mParentGoalId,
                                    Integer.parseInt(mRepeatMeasurementEt.getText().toString()),
                                    mRepeatPerTimePeriodSpn.getSelectedItem().toString(),
                                    mRepeatUnitOfMeasurementSpn.getSelectedItem().toString());
                                addOrEditAction(action);
                        }else {
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
        });
    }

    /**
     * Based on what the user had selected to do, this edits the action they're viewing,
     * or creates the new action in the database
     * @param action action to add/edit
     */
    private void addOrEditAction(Action action) {
        Intent replyIntent = new Intent();
        if (getIntent().hasExtra(Constants.KEY_ACTION_TO_EDIT)){
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
            mRepeatMeasurementEt.setText(String.valueOf(action.getRepeatAmount()));
            mRepeatPerTimePeriodSpn.setSelection(mRepeatPerTimePeriodAdapter.getPosition(action.getRepeatTimePeriod()));
            mRepeatUnitOfMeasurementSpn.setSelection(mRepeatUnitOfMeasurementAdapter.getPosition(action.getRepeatUnitOfMeasurement()));
        }
    }
}