package com.example.graph_app;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.*;

import java.time.LocalDateTime;
import java.util.ResourceBundle;


@Entity(tableName = "weight_table")
public class DatedPoint implements Parcelable{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "measurementDate")
    private LocalDateTime measurementDate;

    @ColumnInfo(name = "weight")
    private double weight;


    public DatedPoint(LocalDateTime measurementDate,double weight){
        this.measurementDate = measurementDate;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
    public LocalDateTime getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(@NonNull LocalDateTime measurementDate) {
        this.measurementDate = measurementDate;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "" + measurementDate +": "+ weight + "lb";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(weight);
        dest.writeSerializable(measurementDate);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DatedPoint(Parcel parcel){
        this.weight = parcel.readDouble();
        this.measurementDate = (LocalDateTime) parcel.readSerializable();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public DatedPoint createFromParcel(Parcel in) {
            return new DatedPoint(in);
        }

        public DatedPoint[] newArray(int size) {
            return new DatedPoint[size];
        }
    };
}
