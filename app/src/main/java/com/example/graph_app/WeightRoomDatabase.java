package com.example.graph_app;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DatedPoint.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class WeightRoomDatabase extends RoomDatabase {
    public abstract WeightDAO weightDAO();
    private static volatile WeightRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WeightRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeightRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeightRoomDatabase.class, "weight_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            /*databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                WeightDAO dao = INSTANCE.weightDAO();
                dao.deleteAll();

                DatedPoint weight_point = new DatedPoint(LocalDateTime.of(2020,01,01,15,30),100);
                dao.insert(weight_point);
                weight_point = new DatedPoint(LocalDateTime.of(2020,01,02,5,40),102);
                dao.insert(weight_point);
                weight_point = new DatedPoint(LocalDateTime.of(2020,01,03,7,30),105);
                dao.insert(weight_point);
                weight_point = new DatedPoint(LocalDateTime.of(2020,01,04,12,0),103);
                dao.insert(weight_point);
            });*/
        }
    };
}
