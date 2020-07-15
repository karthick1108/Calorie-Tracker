package com.example.fit5046_assignment2;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class DailySteps {

    @PrimaryKey(autoGenerate = true)
    public int stepId;

    @ColumnInfo(name="User_Id")
    public int usrId;

    @ColumnInfo(name = "Steps")
    public int usrSteps;

    @ColumnInfo(name = "Date")
    public Date entryDate;

    public DailySteps(int usrId, int usrSteps, Date entryDate) {
        this.usrId = usrId;
        this.usrSteps = usrSteps;
        this.entryDate = entryDate;
    }

    public int getStepId() {
        return stepId;
    }

    public int getUsrId() {
        return usrId;
    }

    public int getUsrSteps() {
        return usrSteps;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setUsrId(int usrId) {
        this.usrId=usrId;
    }

    public void setUsrSteps(int usrSteps) {
        this.usrSteps = usrSteps;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

}

