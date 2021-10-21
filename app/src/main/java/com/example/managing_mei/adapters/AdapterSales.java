package com.example.managing_mei.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Sale;

import java.util.List;

import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValue;
import static com.example.managing_mei.utils.FormatDataUtils.formatTextToUpperOrLowerCase;

public class AdapterSales extends RecyclerView.Adapter<AdapterSales.MyViewHolder>{

private List<Sale> saleList;
private Context context;
private OnSaleListerner monSaleListener;

public AdapterSales(List<Sale> saleList, Context context, OnSaleListerner monSaleListener) {
        this.saleList = saleList;
        this.context = context;
        this.monSaleListener = monSaleListener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View ListItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sale,parent,false);
                return new MyViewHolder(ListItem,monSaleListener) ;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                Sale sale = saleList.get(position);
                holder.name.setText(formatTextToUpperOrLowerCase(sale.getClient().getNome().toUpperCase(), true));
                holder.totalValue.setText(formatMonetaryValue(sale.getTotalValueFromProductsAndDiscount()));
                holder.gain.setText(formatMonetaryValue(sale.getGain()));
                if (sale.getPaymentType()!= null) {
                        holder.paymentType.setText(formatTextToUpperOrLowerCase(sale.getPaymentType(),true));
                } else {
                        holder.paymentType.setText("NÂO ESPECIFICADO");
                }
        }

        @Override
        public int getItemCount() {
                return saleList.size();
        }

        public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
                TextView name,gain,totalValue,paymentType;
                OnSaleListerner onSaleListerner;
                public MyViewHolder(@NonNull View itemView,OnSaleListerner onSaleListerner) {
                        super(itemView);
                        name = itemView.findViewById(R.id.textViewClientNameListSale);
                        gain = itemView.findViewById(R.id.textViewLucroListSale);
                        totalValue = itemView.findViewById(R.id.textViewTotalListSale);
                        paymentType = itemView.findViewById(R.id.textViewpaymentType);
                        this.onSaleListerner = onSaleListerner;
                        itemView.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {
                        onSaleListerner.onSaleListenerClick(getAdapterPosition());
                }
        }
        public interface OnSaleListerner{
                void onSaleListenerClick(int position);
        }

}
