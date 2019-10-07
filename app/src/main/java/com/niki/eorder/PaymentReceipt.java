package com.niki.eorder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.niki.eorder.model.Cart;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PaymentReceipt extends AppCompatActivity {
    TextView tvReservationID, tvSeatNumber;
    private DataPassing dataPassing = DataPassing.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Date date = new Date();
    List<Cart> carts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Payment Receipt");

        final Intent intent = getIntent();
        final long price = intent.getIntExtra("paymentPrice", 0);

        carts = dataPassing.getCarts();

        Random rand = new Random();
        int reservationID = rand.nextInt(9999) + 1;

        Map<String, Integer> orderMenu = new HashMap<>();

        for (Cart c : carts){
            orderMenu.put(c.getID(), c.getQty());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userID", firebaseAuth.getUid());
        data.put("totalPrice", price);
        data.put("dateAndTime", new Timestamp(date));
        data.put("menuOrdered", orderMenu);
        data.put("standID", dataPassing.getStandID());
        data.put("seatNumber", dataPassing.getSeatNumber());
        data.put("reservationID", reservationID);
        data.put("locationID", dataPassing.getLocation());

        DocumentReference ref = db.collection("history").document();
        ref.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("history status ", "success add to history");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PaymentReceipt.this, "Something went wrong when do your payment, please try again in a few minutes", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent1);
                finish();
            }
        });

        final DocumentReference ref1 = db.collection("users").document(firebaseAuth.getUid());
        ref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Long eBalance = documentSnapshot.getLong("eBalance");

                ref1.update("eBalance", eBalance - price);
            }
        });

        tvReservationID = findViewById(R.id.tv_reservation_id);
        tvSeatNumber = findViewById(R.id.tv_seat_number);
        tvReservationID.setText(String.format("%04d", reservationID));
        tvSeatNumber.setText("" + dataPassing.getSeatNumber());

        Button btnBackHome;
        btnBackHome = findViewById(R.id.btn_back_home);

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentReceipt.this, Dashboard.class);
                startActivity(intent);
                Toast.makeText(PaymentReceipt.this, "Thank you for ordering using eOrder :)", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public boolean shouldAllowBack(){
        return false;
    }
    public void doNothing(){

    }

    @Override
    public void onBackPressed() {
        if (!shouldAllowBack()){
            doNothing();
        }
        else{
            super.onBackPressed();
        }
    }
}
