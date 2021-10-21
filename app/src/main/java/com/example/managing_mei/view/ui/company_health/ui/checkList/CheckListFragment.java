package com.example.managing_mei.view.ui.company_health.ui.checkList;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.managing_mei.R;
import com.example.managing_mei.adapters.AdapterCheckListItem;
import com.example.managing_mei.model.entities.CheckListItem;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;

public class CheckListFragment extends Fragment implements AdapterCheckListItem.OnCheckListItemListener{

    private AdapterCheckListItem adapterCashFlowItem;
    private RecyclerView recyclerView;
    private ImageButton imageButtonAddNewCheckList;
    private List<CheckListItem> checkListItems = new ArrayList<>();
    private Date actualDate = new Date();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCheckList);
        imageButtonAddNewCheckList = view.findViewById(R.id.imageButtonAddNewCheckList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext().getApplicationContext()));
        adapterCashFlowItem = new AdapterCheckListItem(checkListItems,this.getContext().getApplicationContext(),this::OnCheckListItemClick);

        firebaseInstance.getReference()
                .child(getIdUser())
                .child("CheckListItem")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        checkListItems.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            CheckListItem checkListItem = ds.getValue(CheckListItem.class);
                            checkListItems.add(checkListItem);
                        }
                        reloadRecyclerClient();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });

        imageButtonAddNewCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyDiscount(false,0);
            }
        });

        return view;
    }

    private void reloadRecyclerClient(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterCashFlowItem);
    }

    private void applyDiscount(Boolean isEditAndDelete, int position){
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_check_list_item_add);

        Button buttonAdd = dialog.findViewById(R.id.buttonCheckListItem);
        Button buttonDelete = dialog.findViewById(R.id.buttonCheckListDelete);
        TextInputLayout editTextCheckListItem = dialog.findViewById(R.id.editTextNameForCheckListItem);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if(isEditAndDelete) {
            editTextCheckListItem.getEditText().setText(checkListItems.get(position).getName().toUpperCase());
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkListItems.get(position).delete();
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckListItem checkListItem = new CheckListItem(editTextCheckListItem.getEditText().getText().toString(),false, new Date());
                    checkListItem.save();
                    checkListItems.clear();
                    dialog.dismiss();
                }
            });

        } else {
            buttonDelete.setVisibility(View.INVISIBLE);
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckListItem checkListItem = new CheckListItem(editTextCheckListItem.getEditText().getText().toString(),false, new Date());
                    checkListItem.save();
                    checkListItems.clear();
                    dialog.dismiss();
                }
            });
        }
        dialog.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void OnCheckListItemClick(int position) {
        applyDiscount(true,position);
    }

}