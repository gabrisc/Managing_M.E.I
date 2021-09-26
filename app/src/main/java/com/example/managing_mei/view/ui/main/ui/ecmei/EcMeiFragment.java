package com.example.managing_mei.view.ui.main.ui.ecmei;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.managing_mei.R;
import com.example.managing_mei.model.entities.Business;
import com.example.managing_mei.view.ui.company_health.CompanyHealthActivity;
import com.example.managing_mei.view.ui.login.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.managing_mei.utils.FireBaseConfig.firebaseInstance;
import static com.example.managing_mei.utils.FireBaseConfig.getFireBaseAutenticacao;
import static com.example.managing_mei.utils.FireBaseConfig.getIdUser;
import static com.example.managing_mei.utils.FormatDataUtils.formatCpfOrCnpj;
import static com.example.managing_mei.utils.FormatDataUtils.formatPhoneNumber;

public class EcMeiFragment extends Fragment {
    private ImageButton openHealthCompany,logoutButton;
    private Business provider;
    private TextView email, fantasyName, CNPJ, adress, phoneNumber, personName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        openHealthCompany = view.findViewById(R.id.imageButtonOpenHealthCompany);
        logoutButton = view.findViewById(R.id.imageButtonLogout);
        fantasyName = view.findViewById(R.id.textViewFantasyName);
        email = view.findViewById(R.id.textViewEmailBussines);
        CNPJ = view.findViewById(R.id.textViewCNPJBussines);
        adress = view.findViewById(R.id.textViewEnderecoBussines);

        phoneNumber = view.findViewById(R.id.textViewPhoneNumber);
        personName = view.findViewById(R.id.textViewPersonName);

        firebaseInstance.getReference()
                .child(getIdUser())
                .child("MEI")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            provider = ds.getValue(Business.class);
                            fantasyName.setText(provider.getFantasyName());
                            email.setText(provider.getEmail());
                            CNPJ.setText(formatCpfOrCnpj(provider.getCNPJ()));
                            adress.setText(provider.getAdress());
                            phoneNumber.setText(formatPhoneNumber(provider.getPhoneNumber()));
                            personName.setText(provider.getPersonName());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String x = String.valueOf(error);
                    }
                });



        OpenCompanyHealth();
        actionForLogoutButton();
        return view;
    }



    private void actionForLogoutButton() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseAuth.getInstance().signOut()
                FirebaseAuth autenticacao = getFireBaseAutenticacao();
                autenticacao.signOut();
                startActivity(new Intent( getContext(), MainActivity.class));
                try {
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void OpenCompanyHealth() {
        openHealthCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CompanyHealthActivity.class));
            }
        });

    }


}