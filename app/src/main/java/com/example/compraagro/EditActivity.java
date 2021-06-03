package com.example.compraagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.compraagro.model.Product;
import com.example.compraagro.model.User;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class EditActivity extends AppCompatActivity {

    TextView tvNombre,tvDescripcion,tvCantidad,tvPrecio,tvPublicador;
    FloatingActionButton fabProfile,fabWhatsApp,fabPhone;
    ImageView productImage;
    String idProduct="",idPublication="",idPublicador="";
    Button btnEdit,btnDelete;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        idProduct=getIntent().getExtras().getString("id");
        idPublication=getIntent().getExtras().getString("idUser");

        tvNombre=findViewById(R.id.tvNombre);
        tvDescripcion=findViewById(R.id.tvDescripcion);
        tvCantidad=findViewById(R.id.tvCantidad);
        tvPrecio=findViewById(R.id.tvPrecio);
        tvPublicador=findViewById(R.id.tvPublicador);
        productImage=findViewById(R.id.ivProduct);
        fabProfile=findViewById(R.id.fabProfile);
        fabWhatsApp=findViewById(R.id.fabWhatsApp);
        fabPhone=findViewById(R.id.fabPhone);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(EditActivity.this, EditProductActivity.class);
                intent.putExtra("idProduct", idProduct);
                startActivity(intent);

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");
                reference.child(idProduct).removeValue();
                Toast.makeText(getApplicationContext(),"Eliminado",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        readProduct();

        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, ProfileActivity.class);
                intent.putExtra("idPublicador", idPublicador);
                startActivity(intent);
            }
        });

        fabWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:123456789")));
            }
        });

        fabPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:123456789")));
            }
        });


    }




    private void readProduct() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);

                    if(product.getIdProducto().equals(idProduct)){
                        tvNombre.setText(product.getNombre());
                        tvDescripcion.setText(product.getDescripcion());
                        tvCantidad.setText(product.getCantidad());
                        tvPrecio.setText(product.getPrecio());
                        Glide.with(getApplication()).load(product.getUrlImagen()).into(productImage);

                        idPublicador = product.getIdUsuario();


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User user = snapshot.getValue(User.class);

                                    if(user.getId().equals(idPublicador)){
                                        tvPublicador.setText(user.getNombres());


                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}