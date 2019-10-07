package com.niki.eorder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Profile extends AppCompatActivity {
    private TextView tvName, tvEmail, tveBalance;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name, email;
    private long eBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            tvName = findViewById(R.id.tv_profile_name);
            tvEmail = findViewById(R.id.tv_profile_email);
            tveBalance = findViewById(R.id.tv_profile_eBalance);

            DocumentReference ref = db.collection("users").document(firebaseAuth.getUid());

            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d("LOGGER", "name " + documentSnapshot.getString("name"));
                    Log.d("LOGGER", "email " + documentSnapshot.getString("email"));
                    Log.d("LOGGER", "eBalance " + documentSnapshot.getLong("eBalance"));

                    name = documentSnapshot.getString("name");
                    email = documentSnapshot.getString("email");
                    eBalance = documentSnapshot.getLong("eBalance");

                    // set eBalance to IDR format
                    DecimalFormat indonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                    DecimalFormatSymbols rupiah = new DecimalFormatSymbols();

                    rupiah.setCurrencySymbol("IDR ");
                    rupiah.setGroupingSeparator('.');
                    indonesia.setMinimumFractionDigits(0);
                    indonesia.setDecimalFormatSymbols(rupiah);

                    tvEmail.setText(email);
                    tvName.setText(name);
                    tveBalance.setText("" +  indonesia.format(eBalance));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error getting data, please try again", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(intent, 0);

        return true;
    }
}
