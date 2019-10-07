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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    Button btnSignIn;
    ImageView ivSignIn;
    private EditText etEmail, etPassword;
    private FirebaseAuth firebaseAuth;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // disable action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ivSignIn = findViewById(R.id.iv_sign_in);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_dont_have_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();

        // progress bar
        relativeLayout = findViewById(R.id.rl_sign_in);

        // set progress bar in front of all layout
        relativeLayout.setZ(999);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        ivSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.equals("") || password.equals("")){
                    Toast.makeText(SignIn.this, "All forms must be filled!", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidEmail(email)){
                    Toast.makeText(SignIn.this, "Email invalid!", Toast.LENGTH_SHORT).show();
                    etEmail.setText("");
                    etPassword.setText("");
                }
                else{
                    relativeLayout.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                relativeLayout.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(SignIn.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                relativeLayout.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignIn.this, "Username or Password is Invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
