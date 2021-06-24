package com.example.compraagro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.compraagro.model.Product;
import com.example.compraagro.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class EditProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageUri=null;
    String mUri;
    TextInputLayout nameProduct,descriptionProduct,quantityProduct,priceProduct;
    ImageButton imageButton;

    DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    StorageTask uploadTask;

    String idProduct="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);


        idProduct= getIntent().getExtras().getString("idProduct");
        Toast.makeText(getApplicationContext(),idProduct,Toast.LENGTH_SHORT).show();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        nameProduct=findViewById(R.id.nameProduct);
        descriptionProduct=findViewById(R.id.descriptionProduct);
        quantityProduct=findViewById(R.id.quantityProduct);
        priceProduct=findViewById(R.id.priceProduct);
        imageButton=findViewById(R.id.ibAddProduct);
        Button btnEditProduct=findViewById(R.id.btnEditProduct);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });



        btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFirebase();
            }
        });


        loadProduct();

    }

    private void loadProduct() {

        mDatabase = FirebaseDatabase.getInstance().getReference("Products");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = new Product();
                    product = dataSnapshot.getValue(Product.class);
                    if(product.getIdProducto().equals(idProduct)){
                        nameProduct.getEditText().setText(product.getNombre());
                        descriptionProduct.getEditText().setText(product.getDescripcion());
                        quantityProduct.getEditText().setText(product.getCantidad());
                        priceProduct.getEditText().setText(product.getPrecio());
                        Glide.with(getApplicationContext()).load(product.getUrlImagen()).into(imageButton);
                        mUri=product.getUrlImagen();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error al cargar",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageButton.setImageURI(imageUri);
        }

    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }




    private void uploadFirebase(){
        if(imageUri==null){

            Product product= new Product();
            product.setIdProducto(idProduct);
            product.setUrlImagen(mUri);
            product.setNombre(nameProduct.getEditText().getText().toString());
            product.setDescripcion(descriptionProduct.getEditText().getText().toString());
            product.setCantidad(quantityProduct.getEditText().getText().toString());
            product.setPrecio(priceProduct.getEditText().getText().toString());
            product.setIdUsuario(firebaseUser.getUid());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        if(user.getId().equals(firebaseUser.getUid())){
                            product.setIdUsuario(user.getId());
                            mDatabase.child(product.getIdProducto()).setValue(product);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(getApplication(),"Cancelado",Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(getApplication(),"Editado",Toast.LENGTH_SHORT).show();
            finish();

        } else {

            //Toast.makeText(getApplication(),imageUri.toString(),Toast.LENGTH_SHORT).show();
            //uploadTask = storageReference.putFile(imageUri);

            String nameimage =System.currentTimeMillis()+".jpg";


            uploadTask= storageReference.child(nameimage).putFile(imageUri);


            Toast.makeText(getApplication(),"Cargando",Toast.LENGTH_SHORT).show();
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference.child(nameimage).getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mUri=downloadUri.toString();
                        Toast.makeText(getApplication(),"Carga completa de la imagen",Toast.LENGTH_SHORT).show();
                        String date = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));

                        Product product= new Product();
                        product.setIdProducto(idProduct);
                        product.setUrlImagen(mUri);
                        product.setNombre(nameProduct.getEditText().getText().toString());
                        product.setDescripcion(descriptionProduct.getEditText().getText().toString());
                        product.setCantidad(quantityProduct.getEditText().getText().toString());
                        product.setPrecio(priceProduct.getEditText().getText().toString());
                        product.setIdUsuario(firebaseUser.getUid());

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    User user = snapshot.getValue(User.class);
                                    if(user.getId().equals(firebaseUser.getUid())){
                                        product.setIdUsuario(user.getId());
                                        mDatabase.child(product.getIdProducto()).setValue(product);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(getApplication(),"Cancelado",Toast.LENGTH_SHORT).show();
                            }
                        });


                        Toast.makeText(getApplication(),"Subido",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {

                        Toast.makeText(getApplication(),"Carga completa?????",Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

}