package com.development.georgemcl.goaltracker.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.development.georgemcl.goaltracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddActionActivity extends AppCompatActivity {
    @BindView(R.id.add_action_name_edittext)
    EditText mActionNameEt;
    @BindView(R.id.add_action_repeat_switch)
    Switch mRepeatSwitch;
    @BindView(R.id.add_action_repeat_layout)
    LinearLayout mRepeatLayout;
    @BindView(R.id.add_action_repeat_per_time_period_spinner)
    Spinner mRepeatPerTimePeriodSpn;
    @BindView(R.id.add_action_repeat_unit_of_measurement_spinner) Spinner mRepeatUnitOfMeasurementSpn;
    @BindView(R.id.add_action_repeat_measurement_edittext) EditText mRepeatMeasurementEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);
        ButterKnife.bind(this);

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

        ArrayAdapter<String> repeatPerTimePeriodAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.repeat_time_periods));
        mRepeatPerTimePeriodSpn.setAdapter(repeatPerTimePeriodAdapter);

        ArrayAdapter<String> repeatUnitOfMeasurementAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.repeat_units_of_measurements));
        mRepeatUnitOfMeasurementSpn.setAdapter(repeatUnitOfMeasurementAdapter);

    }
}
