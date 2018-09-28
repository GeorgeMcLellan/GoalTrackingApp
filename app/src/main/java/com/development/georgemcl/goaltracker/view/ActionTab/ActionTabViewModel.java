package com.development.georgemcl.goaltracker.view.ActionTab;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.development.georgemcl.goaltracker.data.repository.ActionRepository;
import com.development.georgemcl.goaltracker.model.Action;

import java.util.List;

/**
 * ViewModel class for each action tab
 */

public class ActionTabViewModel extends AndroidViewModel {
    private ActionRepository mActionRepository;
    private LiveData<List<Action>> mActions;
    private LiveData<List<Action>> mActionsForRepeatCategory;
    /**
     * Daily, Monthly or Weekly
     */
    private String mActionRepeatTimePeriod;

    public ActionTabViewModel(Application application) {
        super(application);
        mActionRepository = new ActionRepository(application);
        mActions = mActionRepository.getAllActions();
    }


    /**
     * This class requires the consumer to specify which repeat time period (per day/week/month)
     * of which Actions are required
     * @param actionRepeatCategory per day/week/month of Actions to retrieve
     */
    public void setActionRepeatCategory(String actionRepeatCategory) {
        mActionsForRepeatCategory = mActionRepository.getActionsByRepeatTimePeriod(actionRepeatCategory);
    }

    /**
     * @return list of Actions for the specified repeat time period
     */
    public LiveData<List<Action>> getActionsForRepeatCategory() {
        return mActionsForRepeatCategory;
    }
}
