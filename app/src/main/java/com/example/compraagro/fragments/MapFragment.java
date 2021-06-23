package com.example.compraagro.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.compraagro.MainActivity;
import com.example.compraagro.R;
import com.example.compraagro.model.Product;
import com.example.compraagro.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);


        //Internet
        //Cargar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        return root;
    }



    //Cuando el mapa este listo
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(17, 70);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Tacna"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //Habilitar zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Caargar mapa de calor
        loadHeatMap();

    }

    private void loadHeatMap(){

        //Centrar la camara
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-16.39889, -71.535),7));
        //Agregar puntos

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");

        ArrayList<LatLng> list = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {


            HeatmapTileProvider mProvider;
            TileOverlay mOverlay;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Product product = snapshot.getValue(Product.class);
                    if(product.getLat()!=null){

                        LatLng position = new LatLng(Double.parseDouble(product.getLat()), Double.parseDouble(product.getLng()));
                        list.add(position);

                        mMap.addMarker(new MarkerOptions().position(position).title(product.getNombre()));

                    }
                }


                //Contruir el mapa de calor
                mProvider = new HeatmapTileProvider.Builder()
                        .data(list)
                        .build();
                mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}