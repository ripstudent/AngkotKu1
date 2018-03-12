package com.example.yogadarma.angkotku;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PenumpangActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login, registrasi;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penumpang);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(PenumpangActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPass);

        login = findViewById(R.id.btnLogin);
        registrasi = findViewById(R.id.btnRegistrasi);

        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailUser = email.getText().toString();
                final String passUser = password.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(PenumpangActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(PenumpangActivity.this, "Register not sucsess", Toast.LENGTH_SHORT).show();
                        }else{
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference userId_sekarang_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Penumpang").child(userId);
                            userId_sekarang_db.setValue(true);
                        }
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailUser = email.getText().toString();
                final String passUser = password.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(PenumpangActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(PenumpangActivity.this, "Login not success!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthlistener);
    }
}
