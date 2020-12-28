package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    public RecyclerView lv;
    private RecyclerView.LayoutManager layoutManager;
    public DatabaseHelper db;
    public Button add;
    private RecyclerView.Adapter adapter;
    private ArrayList<todoEntry> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        lv=findViewById(R.id.list);
        add=findViewById(R.id.add1Btn);
        db=new DatabaseHelper(this);

        layoutManager=new LinearLayoutManager(this);
        lv.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        adapter=new EntryList(list,this);
        lv.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,addActivity.class));
            }
        });
        populateListview();

    }
    private void populateListview(){
        Cursor data=db.getData();
        ArrayList<String> l=new ArrayList<>();
        while (data.moveToNext()){
            int id=data.getInt(0);
            String n=data.getString(1);
            String d=data.getString(2);
            String t=data.getString(3);
            int check=data.getInt(4);
            todoEntry entry=new todoEntry(n,d,t,id,check);
            list.add(entry);
            adapter.notifyDataSetChanged();

        }

    }

}
