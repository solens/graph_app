package com.example.graph_app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WeightViewModel extends AndroidViewModel{
    private DataRepository dataRepo;
    private LiveData<List<DatedPoint>> allDatedPoints;

    public WeightViewModel(@NonNull Application application) {
        super(application);
        dataRepo = new DataRepository(application);
        allDatedPoints = dataRepo.getAllDatedPoints();
    }

    LiveData<List<DatedPoint>> getAllWeights() { return allDatedPoints; }

    public void insert(DatedPoint newPoint) { dataRepo.insert(newPoint); }
    public void deleteAll(){dataRepo.deleteAll();}
}
