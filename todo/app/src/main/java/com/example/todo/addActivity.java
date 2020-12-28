package com.example.todo;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class addActivity extends AppCompatActivity {

    DatabaseHelper db;

    public EditText name;
    public TextView date,time;
    public Button add,back;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timesetlistener;
    String t,d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application
        setContentView(R.layout.activity_ad);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        name=findViewById(R.id.name);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        add=findViewById(R.id.addBtn);
        back=findViewById(R.id.backBtn);
        db=new DatabaseHelper(this);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog timePickerDialog = new TimePickerDialog(addActivity.this,R.style.DialogTheme, timesetlistener, hour, minute,false);
                timePickerDialog.show();
            }
        });

        timesetlistener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute){
                String hours,minutes;
                hours=Integer.toString(hour);
                minutes=Integer.toString(minute);
                if(hour<10){
                    hours="0"+hours;
                }
                if(minute<10){
                    minutes="0"+minutes;
                }

                t=hours+":"+minutes;
                time.setText(t);
            }

        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        addActivity.this,R.style.DialogTheme,dateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });


        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month=month+1;
                d=day+"/"+month+"/"+year;
                date.setText(d);

            }
        };


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(addActivity.this,"II",Toast.LENGTH_SHORT).show();
                String n=name.getText().toString();
                if(d==null||t==null||n.trim().equals(""))
                    Toast.makeText(addActivity.this,"Fill all fields",Toast.LENGTH_SHORT).show();
                else {
                    todoEntry entry = new todoEntry(n, d, t);
                    AddData(entry);
                    startActivity(new Intent(addActivity.this, MainActivity.class));
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addActivity.this,MainActivity.class));
            }
        });


    }

    public void AddData(todoEntry entry) {

        boolean insert = db.addData(entry);
        if(insert)
            Toast.makeText(this,"Inserted",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
    }

}
