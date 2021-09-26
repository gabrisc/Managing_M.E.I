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
import com.example.managing_mei.utils.FormatDataUtils;

import java.util.List;

import static com.example.managing_mei.utils.FormatDataUtils.formatTextToUpperOrLowerCase;

public class AdapterClient extends RecyclerView.Adapter<AdapterClient.MyViewHolder>{

    private List<Client> clientList;
    private Context context;
    private OnClientListener monClientListener;

    public AdapterClient(List<Client> clientList, Context context, OnClientListener monClientListener) {
        this.clientList = clientList;
        this.context = context;
        this.monClientListener = monClientListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ListItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_client,parent,false);
        return new MyViewHolder(ListItem,monClientListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Client client = clientList.get(position);
        holder.nome.setText(formatTextToUpperOrLowerCase(client.getNome(),true));
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nome;
        OnClientListener onClientListener;

        public MyViewHolder(@NonNull View itemView, OnClientListener onClientListener) {
            super(itemView);
            nome = itemView.findViewById(R.id.TextViewValueOfEconomicOperation);
            this.onClientListener =onClientListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClientListener.onClientOperationClick(getAdapterPosition());
        }
    }
    public interface OnClientListener{
        void onClientOperationClick(int position);
    }
}
