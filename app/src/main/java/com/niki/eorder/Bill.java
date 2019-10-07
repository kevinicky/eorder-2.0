package com.niki.eorder;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.niki.eorder.adapter.BillListAdapter;
import com.niki.eorder.model.Cart;
import com.niki.eorder.model.History;

import java.util.ArrayList;

public class Bill extends AppCompatActivity {
    private Utility util = new Utility();
    private ArrayList<Cart> carts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Bill");


        TextView locationName, standName, date, totalPrice, seatNumber;
        DataPassing dataPassing = DataPassing.getInstance();
        History history = dataPassing.getHistory();
        BillListAdapter adapter;
        RecyclerView recyclerView;

        for(String key : history.getMenuOrdered().keySet()){
            Cart c = new Cart();
            c.setName(key);
            c.setLocationID(history.getLocationID());
            c.setStandID(history.getStandID());
            c.setQty(history.getMenuOrdered().get(key));
            c.setPrice(0);

            carts.add(c);
        }

        Log.d("LOGGER", "carts size at bill : " + carts.size());

        recyclerView = findViewById(R.id.rv_bill);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BillListAdapter(getApplicationContext(), carts);
        recyclerView.setAdapter(adapter);

        locationName = findViewById(R.id.bill_locationName);
        standName = findViewById(R.id.bill_stand_name);
        date = findViewById(R.id.bill_time);
        totalPrice = findViewById(R.id.bill_total_price);
        seatNumber = findViewById(R.id.bill_seat_number);

        locationName.setText(util.capitalizeString(history.getLocationID().replaceAll("_", " ")));
        standName.setText(util.capitalizeString(history.getStandID()));
        date.setText(history.getDate());
        totalPrice.setText("" + util.toIDR(history.getTotalPrice()));
        seatNumber.setText("Seat number : " + history.getSeatNumber());

        Log.d("LOGGER", "" + history.getSeatNumber());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), HistoryList.class);
        startActivityForResult(intent, 0);

        return true;
    }

}

