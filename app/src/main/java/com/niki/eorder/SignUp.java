package com.niki.eorder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity {
    Button btnSignIn;
    private EditText etFullName, etEmail, etPassword;
    ImageView ivSignUp;
    private FirebaseAuth firebaseAuth;
    private RelativeLayout relativeLayout;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // disable action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_already_have_sign_in);
        ivSignUp = findViewById(R.id.iv_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();

        // progress bar
        relativeLayout = findViewById(R.id.rl_sign_up);

        // set progress bar in front of all layout
        relativeLayout.setZ(999);


        ivSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullName = etFullName.getText().toString();
                final String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (fullName.equals("") || email.equals("") || password.equals("")){
                    Toast.makeText(SignUp.this, "All forms must be filled!", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidEmail(email)){
                    Toast.makeText(SignUp.this, "Email invalid!", Toast.LENGTH_SHORT).show();
                    etEmail.setText("");
                    etFullName.setText("");
                    etPassword.setText("");
                }
                else{
                    relativeLayout.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                relativeLayout.setVisibility(View.INVISIBLE);
                                Map<String, Object> docData = new HashMap<>();
                                docData.put("email", email);
                                docData.put("name", fullName);
                                docData.put("eBalance", 0);

                                String uid = firebaseAuth.getUid();

                                DocumentReference ref = db.collection("users").document(uid);
                                ref.set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(SignUp.this, Home.class);
                                        startActivity(intent);
                                        Toast.makeText(SignUp.this, "Thank You for registering eOrder", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                            else{
                                relativeLayout.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignUp.this, "Email or Password is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
