package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private Button btnRecuperar;
    private EditText txtEmail;
    private String email = "";
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnRecuperar = (Button) findViewById(R.id.btnRecuperar);
        txtEmail = (EditText) findViewById(R.id.txtEmail);

        mAuth = FirebaseAuth.getInstance();

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate();
                email = txtEmail.getText().toString();

                if (!email.isEmpty()){
                    resetPassword();
                }
                else {
                    Toast.makeText(ForgotPassword.this, "Debe ingresar el email", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    /*public void validate(){
        String email = txtEmail.getText().toString().trim();
        if (email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Correo invalido");
            return;
        }
        sendEmail(email);
    }*/

    public void resetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Correo Exitoso", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ForgotPassword.this, "No se pudo enviar correo para restablecer contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgotPassword.this, Entrar.class);
        startActivity(intent);
        finish();
    }

    /*public void sendEmail(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "Correo enviado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPassword.this, Entrar.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(ForgotPassword.this, "Correo invalido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/
}