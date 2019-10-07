package com.niki.eorder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.niki.eorder.adapter.MenuAdapter;
import com.niki.eorder.model.Cart;
import com.niki.eorder.model.Menu;


import java.util.ArrayList;
import java.util.List;

public class MenuList extends AppCompatActivity {
    private DataPassing dataPassing = DataPassing.getInstance();
    private ProgressBar progressBar;
    private MenuAdapter adapter;
    private ArrayList<Menu> menus;
    private ArrayList<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // disable action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Menu");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        FloatingActionButton fab = findViewById(R.id.fab_order_now);
        RecyclerView recyclerView;
        FirebaseFirestore db;
        String location, standID, path;

        progressBar = findViewById(R.id.pb_menu_list);
        recyclerView = findViewById(R.id.rv_menu_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menus = new ArrayList<>();
        adapter = new MenuAdapter(MenuList.this, menus);

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        location = dataPassing.getLocation();
        standID = dataPassing.getStandID();

        // get data from firestore menu
        path = "foodcourt/" + location + "/stand_list/" + standID + "/menu";
        db.collection(path).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list){
                        Menu f = d.toObject(Menu.class);
                        f.setID(d.getId());
                        menus.add(f);
                    }
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MenuList.this, "Aw, Snap!, please try again in a few minutes", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MenuList.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartList = adapter.getCartItem();
                if (cartList.isEmpty()){
                    Toast.makeText(MenuList.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MenuList.this,  OrderList.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dataCart", cartList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), StandList.class);
        startActivityForResult(intent, 0);

        return true;
    }
}
