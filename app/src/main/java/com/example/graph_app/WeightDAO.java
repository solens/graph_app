package com.example.graph_app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface WeightDAO {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DatedPoint datedPoint);

    @Query("DELETE FROM weight_table")
    void deleteAll();

    @Query("SELECT * from weight_table ORDER BY measurementDate ASC")
    LiveData<List<DatedPoint>> getAllWeights();

}
