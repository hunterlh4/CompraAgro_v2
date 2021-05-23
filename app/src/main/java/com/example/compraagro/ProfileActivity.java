package com.example.compraagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.compraagro.adapter.CommentaryAdapter;
import com.example.compraagro.model.Commentary;
import com.example.compraagro.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    TextView tvNombres,tvApellidos,tvEmail,tvDepartamento,tvTelefono;
    Button btnAddCommentary;
    ImageView profileImage;
    DatabaseReference reference;
    FirebaseUser fuser;
    StorageReference storageReference;
    String idPublicador;
    private RecyclerView rvCommentary;
    private ArrayList<Commentary> listCommentaries;
    private CommentaryAdapter commentaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        rvCommentary = findViewById(R.id.rvCommentary);
        rvCommentary.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listCommentaries = new ArrayList<>();

        idPublicador=getIntent().getExtras().getString("idPublicador");

        tvNombres= findViewById(R.id.tvNombres);
        tvApellidos= findViewById(R.id.tvApellidos);
        tvEmail= findViewById(R.id.tvEmail);
        tvDepartamento= findViewById(R.id.tvDepartamento);
        tvTelefono= findViewById(R.id.tvTelefono);
        profileImage = findViewById(R.id.profile_image);
        btnAddCommentary = findViewById(R.id.btnAddCommentary);


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        loadCommentaries();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                //Toast.makeText(getContext(),user.getNombres(),Toast.LENGTH_SHORT).show();
                tvNombres.setText(user.getNombres());
                tvApellidos.setText(user.getApellidos());
                tvEmail.setText(user.getEmail());
                tvDepartamento.setText(user.getDepartamento());
                tvTelefono.setText(user.getTelefono());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnAddCommentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,CommentarActivity.class);
                intent.putExtra("idPublicador",idPublicador);
                startActivity(intent);
            }
        });


    }

    private void loadCommentaries(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commentaries");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCommentaries.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Commentary commentaries = snapshot.getValue(Commentary.class);
                    if(commentaries.getIdProfile().equals(idPublicador)){
                        listCommentaries.add(commentaries);
                    }

                }
                commentaryAdapter = new CommentaryAdapter(getApplicationContext(), listCommentaries);
                rvCommentary.setAdapter(commentaryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(this,"Cargando ???",Toast.LENGTH_SHORT).show();


    }
}