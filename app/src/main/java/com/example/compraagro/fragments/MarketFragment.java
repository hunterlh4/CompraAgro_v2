package com.example.compraagro.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Toast;

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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MarketFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<Product> listaProductos;
    private Context context;
    private ProductAdapter productAdapter;
    private SearchView svSearch;
    private ImageButton btnFilter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_market, container, false);



        context = container.getContext();
        recyclerView = root.findViewById(R.id.rvProducts);
        svSearch = root.findViewById(R.id.svSearch);
        btnFilter = root.findViewById(R.id.btnFilter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        listaProductos = new ArrayList<>();

        readPublications();



//        Product product= new Product("https://agro.bayer.pe/-/media/bcs-inter/ws_peru/cultivos/papa/papa.png","Papa","Papa","Papa","Papa","Papa","Papa");
//        listaProductos.add(product);
//        listaProductos.add(product);
//        listaProductos.add(product);
//        listaProductos.add(product);
//        productAdapter = new ProductAdapter(context,listaProductos);
//
//        recyclerView.setAdapter(productAdapter);


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

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v);
            }
        });

//        productAdapter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DetailActivity.class);
//                startActivity(intent);
//            }
//        });


        return root;
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_filter, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, view.findViewById(R.id.btnFilter).TEXT_ALIGNMENT_CENTER, 0, -430);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

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

                    if(products.getEstado()!=null){

                        if(products.getEstado().equals("Activo")){

                            listaProductos.add(products);
                        }
                    }

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