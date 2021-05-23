package com.example.compraagro.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compraagro.R;
import com.example.compraagro.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    TextView tvNombre,tvApellidos,tvEmail,tvDepartamento,tvTelefono;
    ImageView profileImage;
    DatabaseReference reference;
    FirebaseUser fuser;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View root =inflater.inflate(R.layout.fragment_profile, container, false);


        tvNombre= root.findViewById(R.id.tvNombre);
        tvApellidos= root.findViewById(R.id.tvApellidos);
        tvEmail= root.findViewById(R.id.tvEmail);
        tvDepartamento= root.findViewById(R.id.tvDepartamento);
        tvTelefono= root.findViewById(R.id.tvTelefono);
        profileImage = root.findViewById(R.id.profile_image);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                //Toast.makeText(getContext(),user.getNombres(),Toast.LENGTH_SHORT).show();
                tvNombre.setText(user.getNombres());
                tvApellidos.setText(user.getApellidos());
                tvEmail.setText(user.getEmail());
                tvDepartamento.setText(user.getDepartamento());
                tvTelefono.setText(user.getTelefono());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return root;
    }
}