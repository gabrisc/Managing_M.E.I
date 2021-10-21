package com.example.managing_mei.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.managing_mei.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static com.example.managing_mei.utils.FormatDataUtils.formatMonetaryValuePositiveOrNegative;

public class AdapterDoubleValues extends RecyclerView.Adapter<AdapterDoubleValues.MyViewHolder> {

    private List<Double> doubleList;
    private Context context;
    private OnDoubleItemListener onDoubleItemListener;

    public AdapterDoubleValues(List<Double> doubleList, Context context, OnDoubleItemListener onDoubleItemListener) {
        this.doubleList = doubleList;
        this.context = context;
        this.onDoubleItemListener = onDoubleItemListener;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterDoubleValues.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_list,parent,false);
        return new MyViewHolder(view,onDoubleItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterDoubleValues.MyViewHolder holder, int position) {
        holder.value.setText(formatMonetaryValuePositiveOrNegative(
                                                                   doubleList.get(position),
                                                                   false));
    }

    @Override
    public int getItemCount() {
        return doubleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView value;
        OnDoubleItemListener onDoubleItemListener;


        public MyViewHolder(@NonNull @NotNull View itemView,OnDoubleItemListener onDoubleItemListener) {
            super(itemView);
            this.value = itemView.findViewById(R.id.textViewSimpleListItem);
            this.onDoubleItemListener = onDoubleItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.onDoubleItemListener.OnDoubleItemClick(getAdapterPosition());
        }
    }


    public interface OnDoubleItemListener {
        void OnDoubleItemClick(int position);
    }
}
