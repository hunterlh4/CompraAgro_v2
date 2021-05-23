package com.example.compraagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        MaterialEditText departmentUser= findViewById(R.id.departmentUser);
        MaterialEditText emailUser= findViewById(R.id.emailUser);
        MaterialEditText passwordUser= findViewById(R.id.passwordUser);


        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(emailUser.getText().toString(),passwordUser.getText().toString(),nameUser.getText().toString(),surnameUser.getText().toString(),phoneUser.getText().toString(),departmentUser.getText().toString());

                //Toast.makeText(com.example.compraagro.RegisterActivity.this, email.getText().toString()+password.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void register(String emailUser, String passwordUser,String nameUser,String surnameUser,String phoneUser,String departmentUser){
        auth.createUserWithEmailAndPassword(emailUser, passwordUser)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){


                            String userid = UUID.randomUUID().toString();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            User user= new User();
                            user.setId(userid);
                            user.setNombres(nameUser);
                            user.setApellidos(surnameUser);
                            user.setTelefono(phoneUser);
                            user.setDepartamento(departmentUser);
                            user.setTipoUsuario("Vendedor");
                            user.setEmail(emailUser);

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