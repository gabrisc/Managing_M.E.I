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
import com.example.managing_mei.utils.FormatDataUtils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterCheckListItem extends RecyclerView.Adapter<AdapterCheckListItem.MyViewHolder> {

    private List<CheckListItem> checkListItems;
    private Context context;
    private OnCheckListItemListener monCheckListItemListener;
    private SimpleDateFormat dateFullFormat;

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
        holder.texto.setText(FormatDataUtils.formatTextToUpperOrLowerCase(checkListItem.getName(),true));
        dateFullFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (dateFullFormat.parse(dateFullFormat.format(checkListItem.getDate())).before(dateFullFormat.parse(dateFullFormat.format(new Date())))){
                checkListItem.setDate(new Date());
                checkListItem.setStatus(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.checkBox.setChecked(checkListItem.getStatus());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkListItem.setStatus(b);
                checkListItem.setDate(new Date());
                checkListItem.save();
            }
        });

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
