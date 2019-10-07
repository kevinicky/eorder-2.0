package com.niki.eorder;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.niki.eorder.adapter.OrderAdapter;
import com.niki.eorder.model.Cart;

import java.util.ArrayList;

public class OrderList extends AppCompatActivity {
    RecyclerView recyclerView;
    private TextView tvTotal, tvTax, tvFee, tvGrandTotal;
    Button btnOrder, btnCancel;
    private OrderAdapter adapter;
    int total = 0, fee = 100, tax = 0, grandTotal;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean canPay = true;
    private ArrayList<Cart> cartList;
    private DataPassing dataPassing = DataPassing.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // disable action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Order");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        final DocumentReference ref = db.collection("users").document(firebaseAuth.getUid());

        final Bundle bundle = getIntent().getExtras();
        cartList = (ArrayList<Cart>) bundle.getSerializable("dataCart");

        recyclerView = findViewById(R.id.rv_order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderAdapter(OrderList.this, cartList);
        recyclerView.setAdapter(adapter);

        tvTotal = findViewById(R.id.tv_order_total);
        tvTax = findViewById(R.id.tv_order_tax);
        tvFee = findViewById(R.id.tv_order_fee);
        tvGrandTotal = findViewById(R.id.tv_order_grand_total);
        btnOrder = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);

        setItemPriceView();

        adapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
                if (total == 0){
                    btnOrder.setBackgroundColor(Color.parseColor("#696969"));
                    btnOrder.setClickable(false);
                }
            }

            @Override
            public void onClickButton() {
                setItemPriceView();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        long eBalance = documentSnapshot.getLong("eBalance");
                        canPay = true;
                        Log.d("LOGGER" , "eBalance in order : " + eBalance);
                        Log.d("LOGGER","remaining eBalance in order : " + (eBalance - grandTotal));

                        if (eBalance - grandTotal < 0){
                            canPay = false;
                        }
                        Log.d("LOGGER", "Can pay status : " + canPay);

                        if (canPay){
                            Intent intent = new Intent(OrderList.this, Payment.class);
                            intent.putExtra("paymentPrice", grandTotal);
                            dataPassing.setCarts(cartList);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(OrderList.this, "Sorry, your eBalance is not enough to process payment", Toast.LENGTH_SHORT).show();
                        }

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
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartList.clear();
                Toast.makeText(OrderList.this, "Order canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void setItemPriceView(){
        total = adapter.getTotalPrice();
        tax = total / 10;
        fee = 100;
        grandTotal = total + tax + fee;

        Utility util = new Utility();


        tvTotal.setText("Total: " + util.toIDR(total));
        tvTax.setText("Tax(10%): " + util.toIDR(tax));
        tvFee.setText("Admin Fee: " + util.toIDR(fee));
        tvGrandTotal.setText("Grand Total : " + util.toIDR(grandTotal));
    }

    private void removeItem(int position){
        cartList.remove(position);
        adapter.notifyItemRemoved(position);
        setItemPriceView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderList.this, MenuList.class);
        Toast.makeText(OrderList.this, "Order canceled", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MenuList.class);
        Toast.makeText(this, "Order canceled", Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, 0);

        return true;
    }
}
