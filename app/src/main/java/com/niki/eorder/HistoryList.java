package com.niki.eorder;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.niki.eorder.adapter.HistoryAdapter;
import com.niki.eorder.model.History;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HistoryList extends AppCompatActivity {
    private HistoryAdapter adapter;
    private ArrayList<History> histories;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tvTransactionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("History");

        RecyclerView recyclerView;

        histories = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_history_list);
        tvTransactionStatus = findViewById(R.id.tv_never_make_transaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryAdapter(getApplicationContext(), histories);
        recyclerView.setAdapter(adapter);


        CollectionReference historyRef = db.collection("history");
        historyRef.whereEqualTo("userID", firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    tvTransactionStatus.setVisibility(View.INVISIBLE);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list){
                        History h = d.toObject(History.class);

                        Set z = h.getMenuOrdered().keySet();
                        Log.d("LOGGER", "key set : " + z);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyy, HH:mm");
                        Timestamp timestamp = h.getDateAndTime();
                        h.setDate(simpleDateFormat.format(timestamp.toDate()));


                        Log.d("LOG", "History date : " + h.getDateAndTime().toDate());
                        histories.add(h);
                    }

                    adapter.notifyDataSetChanged();
                }
                else {
                    tvTransactionStatus.setVisibility(View.VISIBLE);
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
