package com.niki.eorder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.niki.eorder.adapter.OrderAdapter;
import com.niki.eorder.model.Cart;

import java.util.ArrayList;

public class OrderList extends AppCompatActivity {
    RecyclerView recyclerView;
    private TextView tvTotal, tvTax, tvFee, tvGrandTotal;
    Button btnOrder, btnCancel;
    private OrderAdapter adapter;
    long total = 0, fee = 100, tax = 0, grandTotal;
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

        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) cartList = (ArrayList<Cart>) bundle.getSerializable("dataCart");

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
                dataPassing.setCarts(cartList);
                Intent intent = new Intent(OrderList.this, PaymentMethod.class);
                intent.putExtra("grandTotal", grandTotal);
                startActivity(intent);
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
