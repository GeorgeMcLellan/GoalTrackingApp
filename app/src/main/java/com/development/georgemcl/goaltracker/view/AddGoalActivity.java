package com.development.georgemcl.goaltracker.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Goal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddGoalActivity extends AppCompatActivity {

    private static final String TAG = "AddGoalActivity";
    public static final String EXTRA_GOAL_TO_ADD  = "GOAL_TO_ADD";


    @BindView(R.id.add_goal_name_edittext)
    EditText mGoalNameEt;
    @BindView(R.id.add_goal_description_edittext) EditText mGoalDescriptionEt;
    @BindView(R.id.add_goal_date_textview)
    TextView mGoalDateTxt;
    @BindView(R.id.add_goal_add_fab)
    FloatingActionButton mAddGoalFab;
    int parentGoalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(Constants.KEY_PARENT_GOAL_ID)){
            parentGoalId = getIntent().getIntExtra(Constants.KEY_PARENT_GOAL_ID, -1);
            Log.i(TAG, "onCreate: parent goal id = " + parentGoalId);
        }

        mGoalDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddGoalActivity.this,
                        (new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        mGoalDateTxt.setText(simpleDateFormat.format(cal.getTime()));
                    }
                }), year, month, day );
                datePickerDialog.show();
            }

        });

        mAddGoalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goalName = mGoalNameEt.getText().toString();
                String description = mGoalDescriptionEt.getText().toString();
                String dateToAchieve = mGoalDateTxt.getText().toString();
                if (!goalName.isEmpty()) {
                    if (!dateToAchieve.isEmpty()){
                        Goal goal = new Goal(goalName, description, dateToAchieve, parentGoalId);
                        Toast.makeText(AddGoalActivity.this, "Adding goal", Toast.LENGTH_SHORT).show();
                        Intent replyIntent = new Intent();
                        replyIntent.putExtra(EXTRA_GOAL_TO_ADD, goal);
                        setResult(RESULT_OK,replyIntent);
                        finish();
                    } else {
                        Toast.makeText(AddGoalActivity.this, "Date Required", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(AddGoalActivity.this, "Name Required", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
