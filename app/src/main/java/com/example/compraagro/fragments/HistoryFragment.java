package com.example.compraagro.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.compraagro.DetailActivity;
import com.example.compraagro.R;
import com.example.compraagro.adapter.HistoryAdapter;
import com.example.compraagro.adapter.ProductAdapter;
import com.example.compraagro.model.Product;
import com.example.compraagro.model.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Transaction> listTransactions;
    private Context context;
    private HistoryAdapter historyAdapter;
    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);



        context = container.getContext();
        recyclerView = root.findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        listTransactions = new ArrayList<Transaction>();
        spinner= root.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.historySpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        readTransactions("Compras");
                        break;
                    case 1:
                        readTransactions("Ventas");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        return root;
    }

    private void readTransactions(String tipo) {


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transactions");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTransactions.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transactions = snapshot.getValue(Transaction.class);

                    if(tipo.equals("Compras")){

                        if(transactions.getIdBuyer().equals(firebaseUser.getUid())){

                            listTransactions.add(transactions);
                        }
                    } else
                    if(tipo.equals("Ventas")){

                        if(transactions.getIdSeller().equals(firebaseUser.getUid())){

                            listTransactions.add(transactions);



                        }
                    }
                    historyAdapter = new HistoryAdapter(getContext(), listTransactions);
                    recyclerView.setAdapter(historyAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}