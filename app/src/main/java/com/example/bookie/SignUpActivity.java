package com.example.bookie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public EditText mail,password,repass,name;
    public Button signUp;
    public TextView signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.etName);
        mail = findViewById(R.id.etMail);
        password = findViewById(R.id.etPassword);
        repass = findViewById(R.id.etRepass);
        signUp = findViewById(R.id.btnSignup);
        signIn = findViewById(R.id.tvSignin);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(repass.getText().toString())) {
                    signUp(mail.getText().toString(), password.getText().toString());
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Both password fields should match", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    repass.setText("");
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));

            }
        });


    }

    public void signUp(String mail, String password){
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("Users");
                            users.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(new User(name.getText().toString(), 0));
                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
