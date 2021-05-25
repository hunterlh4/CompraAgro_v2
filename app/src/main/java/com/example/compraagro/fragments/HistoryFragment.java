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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);



        context = container.getContext();
        recyclerView = root.findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        listTransactions = new ArrayList<Transaction>();

        readTransactions();


        return root;
    }

    private void readTransactions() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transactions");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTransactions.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transactions = snapshot.getValue(Transaction.class);

                    listTransactions.add(transactions);

                }

                historyAdapter = new HistoryAdapter(getContext(), listTransactions);
                recyclerView.setAdapter(historyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}