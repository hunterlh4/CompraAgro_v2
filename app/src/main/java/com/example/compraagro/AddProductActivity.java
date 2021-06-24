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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.compraagro.adapter.ProductAdapter;
import com.example.compraagro.model.Product;
import com.example.compraagro.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageUri=null;
    String mUri;
    TextInputLayout nameProduct,descriptionProduct,quantityProduct,priceProduct;
    ImageButton imageButton;

    DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        nameProduct=findViewById(R.id.nameProduct);
        descriptionProduct=findViewById(R.id.descriptionProduct);
        quantityProduct=findViewById(R.id.quantityProduct);
        priceProduct=findViewById(R.id.priceProduct);
        imageButton=findViewById(R.id.ibAddProduct);
        Button btnAddProduct=findViewById(R.id.btnAddProduct);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });



        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFirebase();
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

            Toast.makeText(getApplication(),"Failed",Toast.LENGTH_SHORT).show();
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
                        String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


                        Product product= new Product();
                        product.setIdProducto(UUID.randomUUID().toString());
                        product.setUrlImagen(mUri);
                        product.setNombre(nameProduct.getEditText().getText().toString());
                        product.setDescripcion(descriptionProduct.getEditText().getText().toString());
                        product.setCantidad(quantityProduct.getEditText().getText().toString()+" Kg");
                        product.setPrecio("S/. "+priceProduct.getEditText().getText().toString());
                        product.setEstado("Activo");
                        product.setFecha(fecha);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    User user = snapshot.getValue(User.class);

                                    int i=0;
                                    if(user.getId().equals(firebaseUser.getUid())){
                                        product.setIdUsuario(user.getId());
                                        product.setDepartamento(user.getDepartamento());
                                        for(String departamento : getResources().getStringArray(R.array.departamentos)  ){


                                            if(departamento.equals(user.getDepartamento())){


                                                product.setLat(getResources().getStringArray(R.array.lat)[i]);
                                                product.setLng(getResources().getStringArray(R.array.lng)[i]);

                                            }

                                            i++;
                                        }

                                        mDatabase.child("Products").child(product.getIdProducto()).setValue(product);
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

//    private void searchProduct(String newText){
//        ArrayList<Product> filterProducts = new ArrayList<>();
//        for (Product obj: listaProductos){
//            if (obj.getNombre().toLowerCase().contains(newText.toLowerCase()) || obj.getDescripcion().toLowerCase().contains(newText.toLowerCase())){
//                filterProducts.add(obj);
//            }
//        }
//        ProductAdapter adapterProduct = new ProductAdapter(context, filterProducts);
//        recyclerView.setAdapter(adapterProduct);
//    }

}