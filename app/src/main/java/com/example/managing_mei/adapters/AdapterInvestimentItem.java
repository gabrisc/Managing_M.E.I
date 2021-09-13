package com.example.managing_mei.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.InvestimentItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterInvestimentItem extends RecyclerView.Adapter<AdapterInvestimentItem.MyViewHolder> {

    private List<InvestimentItem> investimentItemList;
    private Context context;
    private OnInvestimentItemListener monInvestimentItemListener;

    public AdapterInvestimentItem(List<InvestimentItem> investimentItemList, Context context, OnInvestimentItemListener onInvestimentItemListener) {
        this.investimentItemList = investimentItemList;
        this.context = context;
        this.monInvestimentItemListener = onInvestimentItemListener;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_investiment,parent,false);
        return new MyViewHolder(view, monInvestimentItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterInvestimentItem.MyViewHolder holder, int position) {
        InvestimentItem investimentItem = investimentItemList.get(position);

        holder.nameOfInvest.setText(investimentItem.getNameFromInvestiment());

        holder.valueOfInvest.setText(investimentItem.getValueFromInvestiment().toString());
/*
        holder.checkBox.setChecked(investimentItem.getIsSettled());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                investimentItem.setIsSettled(b);
                investimentItem.save();
            }
        });
        holder.checkBox.setChecked(investimentItem.getIsSettled());

 */
    }

    @Override
    public int getItemCount() {
        return investimentItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnInvestimentItemListener onInvestimentItemListener;
        CheckBox checkBox;
        TextView nameOfInvest,valueOfInvest;

        public MyViewHolder(@NonNull View itemView, OnInvestimentItemListener onInvestimentItemListener) {
            super(itemView);

            this.onInvestimentItemListener = onInvestimentItemListener;
            this.valueOfInvest = itemView.findViewById(R.id.textViewValueOfInvestiment);
            //this.checkBox = itemView.findViewById(R.id.checkBox2State);
            this.nameOfInvest = itemView.findViewById(R.id.textViewNameOfInvest);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onInvestimentItemListener.onInvestimentItemClick(getAdapterPosition());
        }
    }

    public interface OnInvestimentItemListener {
        void onInvestimentItemClick(int position);
    }


}

