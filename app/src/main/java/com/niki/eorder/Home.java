package com.niki.eorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    ImageView ivSignUp, ivSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // disable action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ivSignIn = findViewById(R.id.iv_home_sign_in);
        ivSignUp = findViewById(R.id.iv_home_sign_up);

        ivSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, SignUp.class);
                startActivity(intent);
            }
        });

        ivSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, SignIn.class);
                startActivity(intent);
            }
        });
    }
}
