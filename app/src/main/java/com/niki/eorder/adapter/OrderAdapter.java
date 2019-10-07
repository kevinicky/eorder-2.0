package com.niki.eorder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.niki.eorder.R;
import com.niki.eorder.Utility;
import com.niki.eorder.model.Cart;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private ImageView imCancelOrder;
    private ArrayList<Cart> cartList;
    private OnItemClickListener listener;
    private Utility util = new Utility();

    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onClickButton();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public OrderAdapter(Context context, ArrayList<Cart> cartList){
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_order_card, viewGroup, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, final int i) {
        orderViewHolder.tvName.setText(cartList.get(i).getName());
        orderViewHolder.tvPrice.setText("Price : " + util.toIDR(cartList.get(i).getPrice()));
        orderViewHolder.tvSubtotal.setText("Subtotal : " + util.toIDR(cartList.get(i).getQty() * cartList.get(i).getPrice()));

        orderViewHolder.btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartList.get(i).setQty(cartList.get(i).getQty() - 1);
                if (cartList.get(i).getQty() < 2){
                    orderViewHolder.btnMin.setVisibility(View.INVISIBLE);
                    cartList.get(i).setQty(1);
                    setCardPriceText(i, orderViewHolder);
                }
                else {
                    orderViewHolder.btnMin.setVisibility(View.VISIBLE);
                    setCardPriceText(i, orderViewHolder);
                }

                if (listener != null){
                    listener.onClickButton();
                }
            }
        });

        orderViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderViewHolder.btnMin.setVisibility(View.VISIBLE);
                cartList.get(i).setQty(cartList.get(i).getQty() + 1);
                setCardPriceText(i, orderViewHolder);

                if (listener != null){
                    listener.onClickButton();
                }
            }
        });

        imCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    int position = orderViewHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            }
        });
    }

    private void setCardPriceText(int position, OrderViewHolder viewHolder){
        Utility util = new Utility();

        viewHolder.tvQty.setText("" + cartList.get(position).getQty());
        viewHolder.tvSubtotal.setText("Subtotal : " + util.toIDR(cartList.get(position).getQty() * cartList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public int getTotalPrice(){
        int totals = 0;
        for (int i = 0; i < cartList.size(); i++){
            totals += cartList.get(i).getPrice() * cartList.get(i).getQty();
        }
        return totals;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvPrice,tvSubtotal, tvQty;
        private ImageView btnAdd, btnMin;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_order_name);
            tvPrice = itemView.findViewById(R.id.tv_order_price);
            tvSubtotal = itemView.findViewById(R.id.tv_order_subtotal);
            tvQty = itemView.findViewById(R.id.tv_qty);
            btnAdd = itemView.findViewById(R.id.btn_add_qty);
            btnMin = itemView.findViewById(R.id.btn_min_qty);
            imCancelOrder = itemView.findViewById(R.id.iv_cancel_order);
        }
    }
}
