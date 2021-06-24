package com.example.compraagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.compraagro.model.Commentary;
import com.example.compraagro.model.Product;
import com.example.compraagro.model.Transaction;
import com.example.compraagro.model.User;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity {

    TextView tvNombre,tvDescripcion,tvCantidad,tvPrecio,tvPublicador;
    FloatingActionButton fabProfile,fabWhatsApp,fabPhone;
    ImageView productImage;
    String id="",idPublication="",idPublicador="";
    Button btnTransaction;

    DatabaseReference mDatabase;

    String phoneUser="123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        id=getIntent().getExtras().getString("id");
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
        btnTransaction = findViewById(R.id.btnTransaction);


        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFirebase();
            }
        });

        readProduct();

        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ProfileActivity.class);
                intent.putExtra("idPublicador", idPublicador);
                startActivity(intent);
            }
        });

        fabWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWhatsApp();
            }
        });

        fabPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+phoneUser)));
            }
        });


    }


    private void sendWhatsApp() {
        Intent intent= new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String msg= "Hola me interesa su producto de CompraAgro.";
        String uri = "whatsapp://send?phone="+phoneUser+"&text="+msg;
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void readProduct() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);

                    if(product.getIdProducto().equals(id)){
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
                                        phoneUser = user.getTelefono();

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

    private void uploadFirebase(){

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        Transaction transaction= new Transaction();
        transaction.setIdTransaction(UUID.randomUUID().toString());
        transaction.setIdProduct(id);
        transaction.setIdBuyer(fuser.getUid());
        transaction.setIdSeller(idPublication);
        transaction.setDate(date);
        transaction.setPrice(tvPrecio.getText().toString());
        transaction.setWeight(tvCantidad.getText().toString());
        transaction.setState("Reservado");
        transaction.setNameProduct(tvNombre.getText().toString());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transactions");


        reference.addValueEventListener(new ValueEventListener() {

            boolean agregar=true;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Transaction transactions = snapshot.getValue(Transaction.class);
                    if(transactions.getIdBuyer().equals(transaction.getIdBuyer()) && transactions.getIdProduct().equals(transaction.getIdProduct())){
                        agregar=false;
                        break;
                    }
                }

                if(agregar){
                    mDatabase.child("Transactions").child(transaction.getIdTransaction()).setValue(transaction);
                    Toast.makeText(getApplication(),"Reservado",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(),"Ya lo reservó",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplication(),"Cancelado",Toast.LENGTH_SHORT).show();
            }
        });


        //Toast.makeText(getApplication(),"Subido",Toast.LENGTH_SHORT).show();


    }

}