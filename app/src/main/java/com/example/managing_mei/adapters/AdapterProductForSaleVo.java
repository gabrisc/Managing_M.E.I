package com.example.managing_mei.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.ProductForSaleVo;
import com.example.managing_mei.utils.FormatDataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;
import static com.example.managing_mei.utils.FormatDataUtils.formatTextToUpperOrLowerCase;

public class AdapterProductForSaleVo extends RecyclerView.Adapter<AdapterProductForSaleVo.MyViewholder> {

    private List<ProductForSaleVo> economicOperationForSaleVos=  new ArrayList<>();
    private Context context;
    private OnEconomicOperationForSaleVo mOnEconomicOperationForSaleVo;
    private boolean newVizualization;

    public AdapterProductForSaleVo(Set<ProductForSaleVo> e, Context context, OnEconomicOperationForSaleVo mOnEconomicOperationForSaleVo,boolean newVizualization) {
        economicOperationForSaleVos.addAll(e);
        this.context = context;
        this.mOnEconomicOperationForSaleVo = mOnEconomicOperationForSaleVo;
        this.newVizualization = newVizualization;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (newVizualization) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_new_item_economic_operation_for_sale,parent,false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_economic_operation_for_sale,parent,false);
        }


        return new MyViewholder(view, mOnEconomicOperationForSaleVo);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        ProductForSaleVo economicOperationForSaleVo = economicOperationForSaleVos.get(position);

        holder.name.setText(formatTextToUpperOrLowerCase(economicOperationForSaleVo.getProduct().getName(),true));
        holder.quantity.setText(""+economicOperationForSaleVo.getQuantitySelect());
        holder.value.setText(formatMonetaryValue(economicOperationForSaleVo.getProduct().getSealValue()));

    }

    @Override
    public int getItemCount() {
        return economicOperationForSaleVos.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView quantity;
        TextView value;
        OnEconomicOperationForSaleVo onEconomicOperationForSaleVo;
        public MyViewholder(@NonNull View itemView,OnEconomicOperationForSaleVo onEconomicOperationForSaleVo) {
            super(itemView);
            if (newVizualization){
                name = itemView.findViewById(R.id.nameItemNew);
                quantity = itemView.findViewById(R.id.qtdItemNew);
                value = itemView.findViewById(R.id.textViewValueOfEconomicOperationNew);
            }else{
                name = itemView.findViewById(R.id.nameItem2);
                quantity = itemView.findViewById(R.id.QtdItem3);
                value = itemView.findViewById(R.id.TextViewValueOfEconomicOperation);
            }
            this.onEconomicOperationForSaleVo = onEconomicOperationForSaleVo;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEconomicOperationForSaleVo.onEconomicOperationForSaleVoClick(getAdapterPosition());
        }
    }
    public interface OnEconomicOperationForSaleVo{
        void onEconomicOperationForSaleVoClick(int position);
    }

}
