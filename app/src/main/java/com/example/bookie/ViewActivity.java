package com.example.bookie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ViewActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        tv = findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    if(Objects.equals(singleSnapshot.getKey(), Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) {
                        tv.setText( Objects.requireNonNull(singleSnapshot.child("Name").getValue()).toString());
                        Log.println(Log.INFO, " ", Objects.requireNonNull(singleSnapshot.child("Name").getValue()).toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
