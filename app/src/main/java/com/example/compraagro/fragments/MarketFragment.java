package com.example.compraagro.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.compraagro.AddProductActivity;
import com.example.compraagro.DetailActivity;
import com.example.compraagro.R;
import com.example.compraagro.RegisterActivity;
import com.example.compraagro.adapter.ProductAdapter;
import com.example.compraagro.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MarketFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<Product> listaProductos;
    private Context context;
    private ProductAdapter productAdapter;
    private SearchView svSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_market, container, false);



        context = container.getContext();
        recyclerView = root.findViewById(R.id.rvProducts);
        svSearch = root.findViewById(R.id.svSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        listaProductos = new ArrayList<Product>();
        readPublications();
        /*
        Product product= new Product("https://agro.bayer.pe/-/media/bcs-inter/ws_peru/cultivos/papa/papa.png","Papa","Papa","Papa","Papa","Papa","Papa");
        listaProductos.add(product);
        listaProductos.add(product);
        listaProductos.add(product);
        listaProductos.add(product);
        productAdapter = new ProductAdapter(context,listaProductos);

        recyclerView.setAdapter(productAdapter);

         */
        FloatingActionButton addProduct = root.findViewById(R.id.addProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddProductActivity.class);
                startActivity(intent);
            }
        });

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProduct(newText);
                return true;
            }
        });

        /*

        productAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                startActivity(intent);
            }
        });
         */


        return root;
    }
    private void readPublications() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaProductos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product products = snapshot.getValue(Product.class);

                    listaProductos.add(products);

                }

                productAdapter = new ProductAdapter(getContext(), listaProductos);
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void searchProduct(String newText){
        ArrayList<Product> filterProducts = new ArrayList<>();
        for (Product obj: listaProductos){
            if (obj.getNombre().toLowerCase().contains(newText.toLowerCase()) || obj.getDescripcion().toLowerCase().contains(newText.toLowerCase())){
                filterProducts.add(obj);
            }
        }
        ProductAdapter adapterProduct = new ProductAdapter(context, filterProducts);
        recyclerView.setAdapter(adapterProduct);
    }


}