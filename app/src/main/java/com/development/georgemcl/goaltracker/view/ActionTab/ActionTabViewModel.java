package com.development.georgemcl.goaltracker.view.ActionTab;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.development.georgemcl.goaltracker.data.repository.ActionRepository;
import com.development.georgemcl.goaltracker.model.Action;

import java.util.List;

/**
 * Created by george on 25/09/18.
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


    public void setActionRepeatCategory(String actionRepeatCategory) {
        mActionsForRepeatCategory = mActionRepository.getActionsByRepeatTimePeriod(actionRepeatCategory);
    }

    public LiveData<List<Action>> getActionsForRepeatCategory() {
        return mActionsForRepeatCategory;
    }
}
