package com.example.fit5046_assignment2;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DailyStepsDao {
    @Query("SELECT * FROM dailysteps")
    List<DailySteps> getAll();

    @Query("SELECT * FROM dailysteps WHERE User_Id =:usrId")
    List<DailySteps> findByUsrid(int usrId);

    @Insert
    void insertAll(DailySteps... dailySteps);
    @Insert
    long insert(DailySteps dailySteps);
    @Delete
    void delete(DailySteps dailySteps);

    @Update(onConflict = REPLACE)
    public void update(DailySteps... dailySteps);

    @Query("DELETE FROM dailySteps")
    void deleteAll();

}

