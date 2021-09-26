package com.example.managing_mei.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Client;
import com.example.managing_mei.model.entities.Provider;
import com.example.managing_mei.utils.FormatDataUtils;

import java.util.List;

import static com.example.managing_mei.utils.FormatDataUtils.formatTextToUpperOrLowerCase;

public class AdapterProvider extends RecyclerView.Adapter<AdapterProvider.MyViewHolder>{

    private List<Provider> providerList;
    private Context context;
    private AdapterProvider.OnProviderListener monProviderListener;

    public AdapterProvider(List<Provider> providerList, Context context, AdapterProvider.OnProviderListener monProviderListener) {
        this.providerList = providerList;
        this.context = context;
        this.monProviderListener = monProviderListener;
    }

    @NonNull
    @Override
    public AdapterProvider.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ListItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_client,parent,false);
        return new AdapterProvider.MyViewHolder(ListItem,monProviderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProvider.MyViewHolder holder, int position) {
        Provider provider = providerList.get(position);
        holder.nome.setText(formatTextToUpperOrLowerCase(provider.getFantasyName(),true));
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nome;
        AdapterProvider.OnProviderListener onProviderListener;

        public MyViewHolder(@NonNull View itemView, AdapterProvider.OnProviderListener onClientListener) {
            super(itemView);
            nome = itemView.findViewById(R.id.TextViewValueOfEconomicOperation);
            this.onProviderListener = onClientListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProviderListener.onProviderClick(getAdapterPosition());
        }
    }
    public interface OnProviderListener{
        void onProviderClick(int position);
    }

}
