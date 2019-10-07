package com.niki.eorder;

import android.content.Intent;
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
import com.niki.eorder.adapter.StandAdapter;
import com.niki.eorder.model.Stand;

import java.util.ArrayList;
import java.util.List;

public class StandList extends AppCompatActivity {
    private DataPassing dataPassing = DataPassing.getInstance();
    private ProgressBar progressBar;
    private StandAdapter adapter;
    private ArrayList<Stand> stands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Stand");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_list);

        RecyclerView recyclerView;
        FirebaseFirestore db;
        String path = "", location = "";

        progressBar = findViewById(R.id.pb_stand_list);
        recyclerView = findViewById(R.id.rv_stand_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stands = new ArrayList<>();
        adapter = new StandAdapter(StandList.this, stands);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        location = dataPassing.getLocation();

        // get data from fire store
        path = "foodcourt/" + location + "/stand_list";
        db.collection(path).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list){
                        Stand f = d.toObject(Stand.class);
                        f.setID(d.getId());
                        stands.add(f);
                    }
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Aw, Snap!, Your Qr Code is broken", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivityForResult(intent, 0);

        return true;
    }
}
