package com.niki.eorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.logging.Logger;

public class PaymentMethod extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tvGopay, tvOvo;
    private Long gopay, ovo, grandTotal;
    private CardView cvOvo, cvGopay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Payment Method");
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null){
            DocumentReference ref = db.collection("users").document(firebaseAuth.getUid());
            tvGopay = findViewById(R.id.tv_gopay_payment);
            tvOvo = findViewById(R.id.tv_ovo_payment);

            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    gopay = documentSnapshot.getLong("gopay");
                    ovo = documentSnapshot.getLong("ovo");
                    Log.d("LOGGER", "Gopay balance : " + documentSnapshot.getLong("gopay"));
                    Log.d("LOGGER", "Ovo balance : " + documentSnapshot.getLong("ovo"));
                    // set eBalance to IDR format
                    Utility utility = new Utility();
                    tvGopay.setText(utility.toIDR(gopay));
                    tvOvo.setText(utility.toIDR(ovo));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PaymentMethod.this, "Error getting data, please try again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        Intent intent = getIntent();
        grandTotal = intent.getLongExtra("grandTotal", 0);
        cvGopay = findViewById(R.id.cv_gopay_payment);
        cvOvo = findViewById(R.id.cv_ovo_payment);

        cvGopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean canPay = true;
                Log.d("LOGGER", "Gopay balance cv: " + gopay);
                Log.d("LOGGER", "Ovo balance cv: " + ovo);
                Log.d("LOGGER", "grandTotal : " + grandTotal);
                if (grandTotal > gopay) canPay = false;

                if(canPay){
                    Intent intent = new Intent(getApplicationContext(), Payment.class);
                    intent.putExtra("paymentPrice", grandTotal);
                    intent.putExtra("paymentMethod", "GOPAY");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(PaymentMethod.this, "Sorry, your GOPAY balance is not enough to process payment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cvOvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean canPay = true;
                if (grandTotal > ovo) canPay = false;

                if(canPay){
                    Intent intent = new Intent(getApplicationContext(), Payment.class);
                    intent.putExtra("paymentPrice", grandTotal);
                    intent.putExtra("paymentMethod", "OVO");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(PaymentMethod.this, "Sorry, your OVO balance is not enough to process payment", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), OrderList.class);
        startActivityForResult(intent, 0);

        return true;
    }
}
