package com.example.managing_mei.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.DebtsItem;
import com.example.managing_mei.model.enuns.TypeOfDebts;
import com.example.managing_mei.utils.FormatDataUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdapterDebts extends RecyclerView.Adapter<AdapterDebts.MyViewHolder>{

    private List<DebtsItem> debtsItems;
    private Context context;
    private OnDebtsItemListener onDebtsItemListener;

    public AdapterDebts(List<DebtsItem> debtsItems, Context context, OnDebtsItemListener onDebtsItemListener) {
        this.debtsItems = debtsItems;
        this.context = context;
        this.onDebtsItemListener = onDebtsItemListener;
    }
    public AdapterDebts(Set<DebtsItem> debtsItems, Context context, OnDebtsItemListener onDebtsItemListener) {
        this.debtsItems = debtsItems.stream().collect(Collectors.toList());
        this.context = context;
        this.onDebtsItemListener = onDebtsItemListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_debt,parent,false);
        return new MyViewHolder(view,onDebtsItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        DebtsItem debtsItem = debtsItems.get(position);
        holder.textViewDescriptionDebts.setText(debtsItem.getNameDebts());
        holder.textViewDebtsItemType.setText("Divida "+debtsItem.getTypeOfDebts().toString());

        if (debtsItem.getTypeOfDebts().toString().equals(TypeOfDebts.PARCELADA.toString())) {
            holder.textViewValueFromDebts.setText(debtsItem.getNumberOfParcels().toString()+"x "+ FormatDataUtils.formatMonetaryValue(debtsItem.getNumberOfParcels()/debtsItem.getDebtsValue()));
        }
        if (debtsItem.getTypeOfDebts().toString().equals(TypeOfDebts.UNICA.toString())) {
            holder.textViewValueFromDebts.setText(FormatDataUtils.formatMonetaryValue(debtsItem.getDebtsValue()));
        }
        if (debtsItem.getTypeOfDebts().toString().equals(TypeOfDebts.RECORRENTE.toString())) {
            holder.textViewValueFromDebts.setText(FormatDataUtils.formatMonetaryValue(debtsItem.getDebtsValue()));
            holder.textViewFrequencyDebts.setText(debtsItem.getFrequencyDebts().toString());
        }
    }

    @Override
    public int getItemCount() {
        return debtsItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewValueFromDebts,textViewDescriptionDebts,textViewFrequencyDebts,textViewDebtsItemType;
        AdapterDebts.OnDebtsItemListener onDebtsItemListener;

        public MyViewHolder(@NonNull View itemView, OnDebtsItemListener onDebtsItemListener) {
            super(itemView);
            this.onDebtsItemListener = onDebtsItemListener;

            this.textViewDebtsItemType = itemView.findViewById(R.id.textViewDebtsItemType);
            this.textViewFrequencyDebts = itemView.findViewById(R.id.textViewFrequencyDebts);

            this.textViewDescriptionDebts  = itemView.findViewById(R.id.textViewDescriptionDebts);
            this.textViewValueFromDebts = itemView.findViewById(R.id.textViewValueFromDebts);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDebtsItemListener.OnCheckListItemClick(getAdapterPosition());
        }
    }

    public interface OnDebtsItemListener {
        void OnCheckListItemClick(int position);
    }

}
