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
import com.example.managing_mei.model.entities.Product;
import com.example.managing_mei.model.entities.Sale;
import com.example.managing_mei.model.enuns.TypeOfProduct;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.MyViewHolder>{

    private List<Product> saleList;
    private Context context;
    private AdapterProduct.OnProductListerner monProductListerner;

    public AdapterProduct(List<Product> saleList, Context context, AdapterProduct.OnProductListerner monSaleListener) {
        this.saleList = saleList;
        this.context = context;
        this.monProductListerner = monSaleListener;
    }

    @NonNull
    @Override
    public AdapterProduct.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ListItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product,parent,false);
        return new AdapterProduct.MyViewHolder(ListItem, monProductListerner) ;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterProduct.MyViewHolder holder, int position) {
        Product economicOperation = saleList.get(position);
        if (economicOperation.getType().equals(TypeOfProduct.SERVIÇO.toString())){
            holder.name.setText(economicOperation.getName().toUpperCase());
            holder.sealValue.setText("Venda: "+String.valueOf(economicOperation.getSealValue())+"R$");
            holder.quantity.setText("Custo: " +String.valueOf(economicOperation.getExpenseValue())+" R$");
        } else {
            holder.name.setText(economicOperation.getName());
            holder.quantity.setText(String.format("%d %s", economicOperation.getQuantity(), economicOperation.getTypeQuantity()));
            holder.sealValue.setText("R$: "+String.valueOf(economicOperation.getSealValue()));
        }
    }

    @Override
    public int getItemCount() {
        return saleList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView sealValue;
        TextView quantity;
        OnProductListerner onProductListerner;

        public MyViewHolder(@NonNull View itemView, AdapterProduct.OnProductListerner onSaleListerner) {
            super(itemView);

            name = itemView.findViewById(R.id.TextViewValueOfEconomicOperation);
            sealValue= itemView.findViewById(R.id.textViewSealValueEconomicOperation);
            quantity = itemView.findViewById(R.id.textViewQuantityEconomicOperation);
            this.onProductListerner = onSaleListerner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.onProductListerner.onProductListenerClick(getAdapterPosition());
        }
    }
    public interface OnProductListerner {
        void onProductListenerClick(int position);
    }


}
