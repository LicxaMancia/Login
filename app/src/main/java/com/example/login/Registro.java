package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText txtUser;
    private EditText txtEmail;
    private EditText txtPass;


    //VARIABLES DE DATOS A REGISTRAR
    private String name = "";
    private String emailaddress = "";
    private String pass = "";


    Button btnRegistrarse;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference Database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUser = (EditText) findViewById(R.id.txtUser);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText)findViewById(R.id.txtPass);
        btnRegistrarse = (Button)findViewById(R.id.btnRecuperar);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                name = txtUser.getText().toString();
                emailaddress = txtEmail.getText().toString();
                pass = txtPass.getText().toString();

            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance().getReference(); //Hace referencia al nodo principal de la base de datos

        authStateListener = (FirebaseAuth.AuthStateListener)(firebaseAuth) -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    Toast.makeText(Registro.this, "El ususario fue creado", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Registro.this, "El usuario no se pudo crear", Toast.LENGTH_SHORT).show();
                }
        };

    }
    /*private void registerUser(){
        firebaseAuth.createUserWithEmailAndPassword(emailaddress, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification();

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", emailaddress);
                    map.put("password", pass);

                    String id = firebaseAuth.getCurrentUser().getUid();
                    Database.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Intent intent = new Intent(Registro.this, PerfilUsuario.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(Registro.this, "No se pudieron crear datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else {
                    Toast.makeText(Registro.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

   public void validate(){
        String email = txtEmail.getText().toString().trim();
        String password = txtPass.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Correo invalido");
            return;
        }
        else{
            txtEmail.setError(null);
        }
        if (password.isEmpty() || password.length() < 8 ){
            txtPass.setError("Se necesitan mas de 8 caracteres");
            return;
        }
        else {
            txtPass.setError(null);
        }
        signUp(email,password);
    }

    public void signUp(String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", emailaddress);
                    map.put("password", pass);

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification();

                    String id = firebaseAuth.getCurrentUser().getUid();
                    Database.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Intent intent = new Intent(Registro.this, PerfilUsuario.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(Registro.this, "No se pudieron crear datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {

                    Toast.makeText(Registro.this, "Hubo un error al crear usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}