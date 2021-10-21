package com.example.managing_mei.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.PaymentType;
import com.example.managing_mei.model.entities.QuantityType;
import com.example.managing_mei.view.ui.main.ui.ecmei.config.payments.PaymentsConfigActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class AdapterConfigElement extends RecyclerView.Adapter<AdapterConfigElement.MyViewHolder> {

    private Context context;
    private List<QuantityType> quantityTypes;
    private List<PaymentType> paymentTypes;
    private OnClickListener onClickListener;


    public AdapterConfigElement(Context context, List<QuantityType> quantityTypes, OnClickListener onClickListener) {
        this.context = context;
        this.quantityTypes = quantityTypes;
        this.onClickListener = onClickListener;
    }

    public AdapterConfigElement(Context applicationContext, List<PaymentType> paymentTypeList, PaymentsConfigActivity onClickListener) {
        this.context = applicationContext;
        this.paymentTypes = paymentTypeList;
        this.onClickListener = onClickListener;

    }

    @NonNull
    @NotNull
    @Override
    public AdapterConfigElement.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_element,parent,false);
        return new MyViewHolder(view,this.onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterConfigElement.MyViewHolder holder, int position) {
        if (Objects.nonNull(quantityTypes)){
            holder.textViewName.setText(quantityTypes.get(position).getNome().toUpperCase());
            if (quantityTypes.get(position).getStatus()) {
                holder.textViewStatus.setText("ATIVO");
            } else {
                holder.textViewStatus.setText("INATIVO");
            }

        }
        if (Objects.nonNull(paymentTypes)) {
            holder.textViewName.setText(paymentTypes.get(position).getNome().toUpperCase());
            if (paymentTypes.get(position).getStatus()) {
                holder.textViewStatus.setText("ATIVO");
            } else {
                holder.textViewStatus.setText("INATIVO");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (Objects.nonNull(paymentTypes)) {
            return paymentTypes.size();
        } else if (!quantityTypes.equals(null)){
            return quantityTypes.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnClickListener onClickListener;
        TextView textViewName;
        TextView textViewStatus;


        public MyViewHolder(@NonNull @NotNull View itemView,OnClickListener onClickListener) {
            super(itemView);
            this.onClickListener = onClickListener;
            this.textViewName = itemView.findViewById(R.id.textViewNameOfConfigElement);
            this.textViewStatus = itemView.findViewById(R.id.textViewStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onConfigElementClick(getAdapterPosition());
        }
    }

    public interface OnClickListener{
        void onConfigElementClick(int position);
    }
}
