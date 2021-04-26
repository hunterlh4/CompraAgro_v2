package com.example.compraagro.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.compraagro.DetailActivity;
import com.example.compraagro.R;
import com.example.compraagro.RegisterActivity;
import com.example.compraagro.adapter.ProductAdapter;
import com.example.compraagro.model.Product;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class MarketFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<Product> listaProductos;
    private Context context;
    private ProductAdapter productAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_market, container, false);



        context = container.getContext();
        recyclerView = root.findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        listaProductos = new ArrayList<Product>();
        Product product= new Product("https://agro.bayer.pe/-/media/bcs-inter/ws_peru/cultivos/papa/papa.png","Papa","Papa","Papa","Papa","Papa","Papa");
        listaProductos.add(product);
        listaProductos.add(product);
        listaProductos.add(product);
        listaProductos.add(product);
        productAdapter = new ProductAdapter(context,listaProductos);

        recyclerView.setAdapter(productAdapter);

        productAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }
}