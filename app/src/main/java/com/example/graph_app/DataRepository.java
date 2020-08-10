package com.example.graph_app;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class DataRepository {
    private WeightDAO weightDAO;
    private LiveData<List<DatedPoint>> allDatedPoints;

    DataRepository(Application application) {
        WeightRoomDatabase db = WeightRoomDatabase.getDatabase(application);
        weightDAO = db.weightDAO();
        allDatedPoints = weightDAO.getAllWeights();
    }

    LiveData<List<DatedPoint>> getAllDatedPoints(){
        return allDatedPoints;
    }

    void insert(DatedPoint datedPoint) {
        WeightRoomDatabase.databaseWriteExecutor.execute(() -> {
            weightDAO.insert(datedPoint);
        });
    }
    void deleteAll(){
        WeightRoomDatabase.databaseWriteExecutor.execute(() -> {
            weightDAO.deleteAll();
        });
    }

}
