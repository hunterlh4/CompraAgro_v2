package com.example.compraagro.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.compraagro.MainActivity;
import com.example.compraagro.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        return root;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(17, 70);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Tacna"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.getUiSettings().setZoomControlsEnabled(true);

        loadHeatMap();

    }

    private void loadHeatMap(){

        HeatmapTileProvider mProvider;
        TileOverlay mOverlay;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-16.39889, -71.535),7));
        ArrayList<LatLng> list = new ArrayList<>();
        list.add(new LatLng(-16.39889, -71.535));
        list.add(new LatLng(-13.52264, -71.96734));
        list.add(new LatLng(-17.19832, -70.93567));
        list.add(new LatLng(-12.04318, -77.02824));
        list.add(new LatLng(-18.01465, -70.25362));
        list.add(new LatLng(-18.01465, -70.25362));
        list.add(new LatLng(-18.01465, -70.25362));

        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

    }

}