package com.niki.eorder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.niki.eorder.R;
import com.niki.eorder.Utility;
import com.niki.eorder.model.Cart;

import java.util.ArrayList;

public class BillListAdapter extends RecyclerView.Adapter<BillListAdapter.BillListViewHolder> {
    private Context context;
    private ArrayList<Cart> carts;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Utility util = new Utility();

    public BillListAdapter(Context context, ArrayList<Cart> carts){
        this.context = context;
        this.carts = carts;
    }


    @NonNull
    @Override
    public BillListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_bill_list, parent, false);

        return new BillListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BillListViewHolder holder, int position) {
        holder.qty.setText("" + carts.get(position).getQty());
        String foodID = carts.get(position).getName();
        holder.name.setText(util.capitalizeString(foodID));
        DocumentReference ref = db.collection("foodcourt/" + carts.get(position).getLocationID() + "/stand_list/" + carts.get(position).getStandID() + "/menu").document(carts.get(position).getName());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Long price = documentSnapshot.getLong("price");
                holder.price.setText("" + util.toIDR(price));

            }
        });
        
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class BillListViewHolder extends RecyclerView.ViewHolder {
        private TextView qty, name, price;

        public BillListViewHolder(View itemView) {
            super(itemView);

            qty = itemView.findViewById(R.id.bill_qty);
            name = itemView.findViewById(R.id.bill_name);
            price = itemView.findViewById(R.id.bill_price);
        }
    }
}
