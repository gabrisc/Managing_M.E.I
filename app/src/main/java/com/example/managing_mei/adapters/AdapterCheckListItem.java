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
import com.example.managing_mei.model.entities.CheckListItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterCheckListItem extends RecyclerView.Adapter<AdapterCheckListItem.MyViewHolder> {

    private List<CheckListItem> checkListItems;
    private Context context;
    private OnCheckListItemListener monCheckListItemListener;

    public AdapterCheckListItem(List<CheckListItem> checkListItems, Context context, OnCheckListItemListener onCheckListItemListener) {
        this.checkListItems = checkListItems;
        this.context = context;
        this.monCheckListItemListener = onCheckListItemListener;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_list_item,parent,false);
        return new MyViewHolder(view,monCheckListItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        CheckListItem checkListItem = checkListItems.get(position);
        holder.texto.setText(checkListItem.getName());
        holder.checkBox.setChecked(checkListItem.getStatus());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkListItems.get(position).setStatus(b);
                checkListItems.get(position).save();
            }
        });
        holder.checkBox.setChecked(checkListItem.getStatus());
    }

    @Override
    public int getItemCount() {
        return checkListItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView texto;
        CheckBox checkBox;
        OnCheckListItemListener onCheckListItemListener;

        public MyViewHolder(@NonNull View itemView, OnCheckListItemListener onCheckListItemListener) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.chipCheckedElement);
            this.texto = itemView.findViewById(R.id.textViewItemCheckListItem);
            this.onCheckListItemListener = onCheckListItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCheckListItemListener.OnCheckListItemClick(getAdapterPosition());
        }
    }

    public interface OnCheckListItemListener {
        void OnCheckListItemClick(int position);
    }

}
