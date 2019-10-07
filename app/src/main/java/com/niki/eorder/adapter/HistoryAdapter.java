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

import com.niki.eorder.Bill;
import com.niki.eorder.DataPassing;
import com.niki.eorder.R;
import com.niki.eorder.Utility;
import com.niki.eorder.model.History;

import java.util.ArrayList;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context context;
    private ArrayList<History> histories;
    private Utility util = new Utility();


    public HistoryAdapter(Context context, ArrayList<History> histories){
        this.context = context;
        this.histories = histories;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_history_card, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, final int position) {

        holder.tvDate.setText(histories.get(position).getDate());
        holder.tvPrice.setText(util.toIDR(histories.get(position).getTotalPrice()));

        String locationID = histories.get(position).getLocationID();
        String temp = locationID.replaceAll("_", " ");
        temp = util.capitalizeString(temp);

        holder.tvLocation.setText(temp);

        holder.tvSeatNumber.setText("" + histories.get(position).getSeatNumber());

        holder.cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Bill.class);
                DataPassing dataPassing = DataPassing.getInstance();
                dataPassing.setHistory(histories.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocation, tvPrice, tvDate, tvSeatNumber;
        private CardView cvHistory;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            cvHistory = itemView.findViewById(R.id.cv_history_card);
            tvDate = itemView.findViewById(R.id.tv_history_time);
            tvLocation = itemView.findViewById(R.id.tv_history_location);
            tvPrice = itemView.findViewById(R.id.tv_history_total);
            tvSeatNumber = itemView.findViewById(R.id.tv_history_seat_number);
        }
    }
}
