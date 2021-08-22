package com.example.managing_mei.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.CashFlowItem;

import java.util.List;
import java.util.Set;

public class AdapterCashFlowItem extends RecyclerView.Adapter<AdapterCashFlowItem.MyViewHolder>{

    private List<CashFlowItem> cashFlowItemList;
    private Context context;
    private OnCashFlowItemListener monCashFlowItemListener;

    public AdapterCashFlowItem(List<CashFlowItem> cashFlowItemList, Context context, OnCashFlowItemListener monCashFlowItemListener) {
        this.cashFlowItemList = cashFlowItemList;
        this.context = context;
        this.monCashFlowItemListener = monCashFlowItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ListItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cash_flow_item,parent,false);
        return new MyViewHolder(ListItem, monCashFlowItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CashFlowItem flowItem = cashFlowItemList.get(position);
        holder.primeiro.setText(""+flowItem.getDayOfMonth()+"/"+flowItem.getMonth());
        if (flowItem.getType().equals(0)){
            holder.segundo.setText("- "+flowItem.getValue());
            //holder.segundo.setTextColor(Color.parseColor(String.valueOf(R.color.vermelho)));
        }else{
            holder.segundo.setText("+ "+flowItem.getValue());
            //holder.segundo.setTextColor(Color.parseColor(String.valueOf(R.color.vermelho)));
        }
        holder.terceiro.setText(flowItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return cashFlowItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView primeiro,segundo,terceiro;
        OnCashFlowItemListener onCashFlowItemListener;

        public MyViewHolder(@NonNull View itemView, OnCashFlowItemListener onCashFlowItemListener) {
            super(itemView);

            primeiro = itemView.findViewById(R.id.textViewFirstElement);
            segundo = itemView.findViewById(R.id.textViewSecondElement);
            terceiro = itemView.findViewById(R.id.textViewThirdElement);

            this.onCashFlowItemListener = onCashFlowItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCashFlowItemListener.onCashFlowItemClick(getAdapterPosition());
        }
    }
    public interface OnCashFlowItemListener {
        void onCashFlowItemClick(int position);
    }
}
