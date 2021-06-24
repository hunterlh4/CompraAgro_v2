package com.example.compraagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compraagro.model.Product;
import com.example.compraagro.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {


    FirebaseAuth auth;

    DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        MaterialEditText nameUser= findViewById(R.id.nameUser);
        MaterialEditText surnameUser= findViewById(R.id.surnameUser);
        MaterialEditText phoneUser= findViewById(R.id.phoneUser);
        MaterialEditText emailUser= findViewById(R.id.emailUser);
        MaterialEditText passwordUser= findViewById(R.id.passwordUser);
        Spinner departmentUser = (Spinner) findViewById(R.id.departmentUser);


        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(emailUser.getText().toString(),passwordUser.getText().toString(),nameUser.getText().toString(),surnameUser.getText().toString(),phoneUser.getText().toString(),departmentUser.getSelectedItem().toString());

                //Toast.makeText(com.example.compraagro.RegisterActivity.this, departmentUser.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.departamentos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentUser.setAdapter(adapter);



    }

    private void register(String emailUser, String passwordUser,String nameUser,String surnameUser,String phoneUser,String departmentUser){
        auth.createUserWithEmailAndPassword(emailUser, passwordUser)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){


                            String userid = task.getResult().getUser().getUid();


                            Toast.makeText(com.example.compraagro.RegisterActivity.this, userid, Toast.LENGTH_SHORT).show();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            User user= new User();
                            user.setId(userid);
                            user.setNombres(nameUser);
                            user.setApellidos(surnameUser);
                            user.setTelefono(phoneUser);
                            user.setDepartamento(departmentUser);
                            user.setTipoUsuario("Vendedor");
                            user.setEmail(emailUser);
                            user.setUrlImagen("https://firebasestorage.googleapis.com/v0/b/compraagro-a6c29.appspot.com/o/uploads%2Fprofile.png?alt=media&token=5409a72f-8ecb-4fa9-af62-7bfd0682c26c");

                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(com.example.compraagro.RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(com.example.compraagro.RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}