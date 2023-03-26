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

public class Entrar extends AppCompatActivity {

    EditText txtemail;
    EditText txtpass;
    TextView olvistecontraseña;

    Button btnIngresar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);

        txtemail = (EditText) findViewById(R.id.txtEmail);
        txtpass = (EditText) findViewById(R.id.txtPass);
        olvistecontraseña = (TextView)findViewById(R.id.olvidastecontraseña);
        btnIngresar = (Button)findViewById(R.id.btnRecuperar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                Intent intent = new Intent(Entrar.this, PerfilUsuario.class);
                startActivity(intent);
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = (FirebaseAuth.AuthStateListener)(firebaseAuth) ->{

                FirebaseUser  user = firebaseAuth.getCurrentUser();
                if (user != null){
                    if (!user.isEmailVerified()){
                        Toast.makeText(Entrar.this, "Correo electronico no verificado", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(Entrar.this, "Inicia sesion", Toast.LENGTH_SHORT).show();
                    }

                }
            };

        olvistecontraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Entrar.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });


    }

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
        String email = txtemail.getText().toString().trim();
        String password = txtpass.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtemail.setError("Correo invalido");
            return;
        }
        else{
            txtemail.setError(null);
        }
        if (password.isEmpty() || password.length() < 8 ){
            txtpass.setError("Se necesitan mas de 8 caracteres");
            return;
        }
        else{
            txtpass.setError(null);
        }
        login(email,password);
    }


    public void login(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Entrar.this, "Inicio Sesion!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Entrar.this, "Credenciales equivocadas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}