package com.usman.todoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginLog" ;
    private FirebaseAuth mAuth;
    EditText email,password;
    Button signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
         password = findViewById(R.id.password);
         signInButton = findViewById(R.id.sign_in_button);

         signInButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(!email.getText().toString().equals("")){

                     if(!password.getText().toString().equals("")){

                         SignIn(email.getText().toString(),password.getText().toString());
                     }
                     else{
                         Toast.makeText(LoginActivity.this,"please Enter Password", Toast.LENGTH_LONG).show();
                     }

                 }
                 else{
                     Toast.makeText(LoginActivity.this,"please Enter Email", Toast.LENGTH_LONG).show();
                 }
             }
         });

    }




    public void SignIn(String email,String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void updateUI(FirebaseUser user) {

        if(user!=null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(LoginActivity.this, "Please Register FIrst",
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
