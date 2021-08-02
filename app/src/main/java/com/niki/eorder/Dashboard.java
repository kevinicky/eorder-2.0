package com.niki.eorder;
// test commend
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextView tvUserEmail, tvUserName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // disable action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button btnSignOut;
        CardView cvOrderMenu, cvProfile, cvHistory, cvTopUp;


        btnSignOut = findViewById(R.id.btn_sign_out);

        cvOrderMenu = findViewById(R.id.cv_order_menu);
        cvProfile = findViewById(R.id.cv_profile);
        cvHistory = findViewById(R.id.cv_history);
        cvTopUp = findViewById(R.id.cv_topup);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(Dashboard.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryList.class);
                startActivity(intent);
            }
        });

        cvOrderMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, QrCodeScanner.class);
                startActivity(intent);
            }
        });

        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        cvTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TopUp.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            DocumentReference ref = db.collection("users").document(firebaseAuth.getUid());
            tvUserEmail = findViewById(R.id.tv_user_email);
            tvUserName = findViewById(R.id.tv_user_name);

            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("LOGGER", "name " + documentSnapshot.getString("name"));
                    Log.d("LOGGER", "email " + documentSnapshot.getString("email"));

                    name = documentSnapshot.getString("name");
                    email = documentSnapshot.getString("email");
                    // set eBalance to IDR format

                    tvUserEmail.setText(email);
                    tvUserName.setText("Welcome back, " + name);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Dashboard.this, "Error getting data, please try again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }

    }

}
