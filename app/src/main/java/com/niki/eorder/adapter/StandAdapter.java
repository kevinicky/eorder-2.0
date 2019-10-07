package com.niki.eorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niki.eorder.DataPassing;
import com.niki.eorder.MenuList;
import com.niki.eorder.R;
import com.niki.eorder.model.Stand;

import java.util.ArrayList;

public class StandAdapter extends RecyclerView.Adapter<StandAdapter.StandViewHolder> {
    private Context context;
    private ArrayList<Stand> standList;

    public StandAdapter(Context context, ArrayList<Stand> standList){
        this.context = context;
        this.standList = standList;
    }

    @NonNull
    @Override
    public StandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_stand_card, parent, false);
        return new StandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StandViewHolder holder, final int position) {
        holder.tvName.setText(standList.get(position).getName());
        holder.tvDescription.setText(standList.get(position).getDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MenuList.class);
                DataPassing dataPassing = DataPassing.getInstance();
                dataPassing.setStandID(standList.get(position).getID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return standList.size();
    }

    class StandViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDescription;
        private CardView cardView;

        public StandViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_stand_name);
            tvDescription = itemView.findViewById(R.id.tv_stand_description);
            cardView = itemView.findViewById(R.id.cv_stand_card);
        }
    }
}
