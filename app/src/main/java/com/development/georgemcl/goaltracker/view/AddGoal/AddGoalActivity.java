package com.development.georgemcl.goaltracker.view.AddGoal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.goaltracker.Constants;
import com.development.georgemcl.goaltracker.R;
import com.development.georgemcl.goaltracker.model.Goal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * For adding goals/sub-goals
 */
public class AddGoalActivity extends AppCompatActivity {

    private static final String TAG = "AddGoalActivity";
    public static final String EXTRA_GOAL_TO_ADD  = "GOAL_TO_ADD";
    public static final String EXTRA_GOAL_TO_EDIT  = "GOAL_TO_EDIT";

    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @BindView(R.id.add_goal_name_edittext)
    EditText mGoalNameEt;
    @BindView(R.id.add_goal_description_edittext) EditText mGoalDescriptionEt;
    @BindView(R.id.add_goal_date_textview)
    TextView mGoalDateTxt;
    @BindView(R.id.add_goal_submit_fab)
    FloatingActionButton mSubmitFab;
    @BindView(R.id.add_goal_date_clear)
    ImageView mClearDateImg;
    private int mParentGoalId;

    //If existing goal is being edited
    private int mGoalToEditId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(Constants.KEY_PARENT_GOAL_ID)){
            mParentGoalId = getIntent().getIntExtra(Constants.KEY_PARENT_GOAL_ID, -1);
        }

        if (getIntent().hasExtra(Constants.KEY_GOAL_TO_EDIT)) {
            populateFields((Goal) getIntent().getSerializableExtra(Constants.KEY_GOAL_TO_EDIT));
        } else {
            setGoalDateToEndOfYear();
        }

        mGoalDateTxt.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            final int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddGoalActivity.this,
                    ((view, year1, month1, dayOfMonth) -> {
                        Calendar cal = new GregorianCalendar(year1, month1, dayOfMonth);
                        mGoalDateTxt.setText(mSimpleDateFormat.format(cal.getTime()));
                        mClearDateImg.setVisibility(View.VISIBLE);
                    }), year, month, day );
            datePickerDialog.show();
        });

        mClearDateImg.setOnClickListener(v -> {
            mGoalDateTxt.setText("");
            mClearDateImg.setVisibility(View.INVISIBLE);
        });

        mSubmitFab.setOnClickListener(v -> {
            String goalName = mGoalNameEt.getText().toString();
            String description = mGoalDescriptionEt.getText().toString();
            String dateToAchieve = mGoalDateTxt.getText().toString();


            if (!goalName.isEmpty()) {
                if (!dateToAchieve.isEmpty()){
                    try {
                        Date date = mSimpleDateFormat.parse((mGoalDateTxt.getText().toString()));
                        Date todaysDate = Calendar.getInstance().getTime();
                        if (date.getTime() > todaysDate.getTime()) {
                            Goal goal = new Goal(goalName, description, dateToAchieve, mParentGoalId);
                            addOrEditGoal(goal);
                        } else {
                            Toast.makeText(AddGoalActivity.this, getString(R.string.date_too_early), Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        Toast.makeText(AddGoalActivity.this, getString(R.string.invalid_date), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Goal goal = new Goal(goalName, description, "", mParentGoalId);
                    addOrEditGoal(goal);
                }
            } else{
                Toast.makeText(AddGoalActivity.this, getString(R.string.missing_name), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setGoalDateToEndOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        mGoalDateTxt.setText(mSimpleDateFormat.format(calendar.getTime()));
        mClearDateImg.setVisibility(View.VISIBLE);
    }

    /**
     * Based on what the user had selected to do, this edits the goal they're viewing,
     * or creates the new goal in the database
     * @param goal goal to add/edit
     */
    private void addOrEditGoal(Goal goal) {

        Intent replyIntent = new Intent();
        if (getIntent().hasExtra(Constants.KEY_GOAL_TO_EDIT)){
            goal.setId(mGoalToEditId);
            replyIntent.putExtra(EXTRA_GOAL_TO_EDIT, goal);
        } else {
            replyIntent.putExtra(EXTRA_GOAL_TO_ADD, goal);
        }

        replyIntent.putExtra(EXTRA_GOAL_TO_ADD, goal);
        setResult(RESULT_OK, replyIntent);
        finish();
    }


    /**
     * If editing a goal - populates fields with existing goal data
     */
    private void populateFields(Goal goal) {
        mGoalToEditId = goal.getId();
        mGoalNameEt.setText(goal.getGoalName());
        mGoalDateTxt.setText(goal.getCompletionDate());
        mGoalDescriptionEt.setText(goal.getDescription());
        mClearDateImg.setVisibility(View.VISIBLE);
    }
}
