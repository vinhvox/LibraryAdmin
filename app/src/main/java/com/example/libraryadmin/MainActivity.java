package com.example.libraryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText edtEmailLogin, edtPasswordLogin;
    Button btnLogin;
    String emailLogin, passwordLogin, emailForgot;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    private void setupViews() {
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
        getInformationLogin();
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgotPassword();
            }
        });
    }
    private  void  getInformationLogin(){

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailLogin = edtEmailLogin.getText().toString().trim();
                passwordLogin = edtPasswordLogin.getText().toString().trim();
                if (emailLogin.isEmpty() || passwordLogin.isEmpty()){
                    Toast.makeText(MainActivity.this, getString(R.string.enter_full_information), Toast.LENGTH_LONG).show();
                }
                else {
                    if (!emailLogin.contains("@gmail.com")){
                        edtEmailLogin.setError(getString(R.string.request_email_format));
                    }
                    if (passwordLogin.length()<6){
                        edtPasswordLogin.setError(getString(R.string.request_password_format));
                    }
                    else {
                        requestLogin(emailLogin, passwordLogin);
                    }
                }
            }
        });
    }
    private  void requestLogin(String email, String password){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.mess_login));
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(MainActivity.this, getString(R.string.login_failed)+ " "+task.getException(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
    private  void dialogForgotPassword(){
        final Dialog dialog  = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_forgot_password);
        final EditText edtEmailForgotPassword = dialog.findViewById(R.id.edtEmailForgotPassword);
        Button btnRequestPassword = dialog.findViewById(R.id.btnRequestPassword);
        Button btnCancelForgot = dialog.findViewById(R.id.btnCancelForgot);
        btnRequestPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailForgot = edtEmailForgotPassword.getText().toString().trim();
                sendRequest(emailForgot);
                dialog.dismiss();

            }
        });
        btnCancelForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private  void sendRequest(String email){
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage(getString(R.string.mess_forgot));
        dialog.show();
        auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, getString(R.string.forgot_failed)+ " "+task.getException(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}