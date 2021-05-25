package com.example.compraagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compraagro.model.Commentary;
import com.example.compraagro.model.Product;
import com.example.compraagro.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.UUID;

public class CommentaryActivity extends AppCompatActivity {

    MaterialEditText titleCommentary,descriptionCommentary;
    RatingBar starsCommentary;
    Button btnAddCommentary;
    String idPublicador;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentary);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        idPublicador=getIntent().getExtras().getString("idPublicador");

        titleCommentary=findViewById(R.id.titleCommentary);
        descriptionCommentary=findViewById(R.id.descriptionCommentary);
        starsCommentary=findViewById(R.id.starsCommentary);
        btnAddCommentary=findViewById(R.id.btnAddCommentary);

        btnAddCommentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFirebase();
            }
        });

    }

    private void uploadFirebase(){

        String stars="";
        int numStars=(int)starsCommentary.getRating();

        switch (numStars){
            case 1: stars="⭐";
                    break;
            case 2: stars="⭐⭐";
                break;
            case 3: stars="⭐⭐⭐";
                break;
            case 4: stars="⭐⭐⭐⭐";
                break;
            case 5: stars="⭐⭐⭐⭐⭐";
                break;
            default: stars="⭐";
                break;
        }


        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        Commentary commentary= new Commentary();
        commentary.setIdCommentary(UUID.randomUUID().toString());
        commentary.setTitle(titleCommentary.getText().toString());
        commentary.setDescription(descriptionCommentary.getText().toString());
        commentary.setStars(stars);
        commentary.setIdCommentator(fuser.getUid());
        commentary.setIdProfile(idPublicador);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getId().equals(fuser.getUid())){
                        commentary.setNameCommentator(user.getNombres());
                        mDatabase.child("Commentaries").child(commentary.getIdCommentary()).setValue(commentary);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplication(),"Cancelado",Toast.LENGTH_SHORT).show();
            }
        });


        Toast.makeText(getApplication(),"Subido",Toast.LENGTH_SHORT).show();


    }

}