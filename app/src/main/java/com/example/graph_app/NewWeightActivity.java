package com.example.graph_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class NewWeightActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_REPLY = "com.example.android.graph_app.REPLY";

    private EditText mEditWeightView;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DateTimeFormatter formatter;
    LocalDateTime newDate;
    String dateTxt, timeTxt;
    Button button;

    public NewWeightActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_weight);
        mEditWeightView = findViewById(R.id.edit_weight);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        // Set as Current Date
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        dateTxt = mYear+"-"+subs(mMonth)+"-"+subs(mDay);
        timeTxt = subs(mHour)+":"+subs(mMinute);
        txtDate.setText(dateTxt);
        txtTime.setText(timeTxt);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm");
        newDate = LocalDateTime.parse(dateTxt+"/"+timeTxt,formatter);

        //Save Button
        button = findViewById(R.id.button_save);
        button.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        Intent replyIntent = new Intent();
        if(v == button){
            if (TextUtils.isEmpty(mEditWeightView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                LocalDateTime pointDate = LocalDateTime.parse(dateTxt+"/"+timeTxt,formatter);
                DatedPoint newWeightedPoint = new DatedPoint(pointDate,Double.valueOf(mEditWeightView.getText().toString()));
                replyIntent.putExtra(EXTRA_REPLY, (Parcelable) newWeightedPoint);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        }
        if(v == btnDatePicker){
            // Get Current Date
            //final Calendar c = Calendar.getInstance();
            //mYear = c.get(Calendar.YEAR);
            //mMonth = c.get(Calendar.MONTH);
            //mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            dateTxt = year+"-"+subs(monthOfYear+1)+"-"+subs(dayOfMonth);
                            txtDate.setText(dateTxt);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if(v== btnTimePicker){
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            timeTxt = subs(hourOfDay) + ":"+subs(minute);

                            txtTime.setText(timeTxt);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }


    }

    private String subs(String str){
        str = "00"+ str;
        return str.substring(str.length()-2);

    }

    private String subs(int i){
        String str = "00"+ i;
        return str.substring(str.length()-2);

    }
}