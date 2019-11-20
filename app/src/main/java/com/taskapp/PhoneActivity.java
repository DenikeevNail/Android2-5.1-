package com.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private EditText editPhone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneAuthCredential phoneAuthCredential;
    private EditText editPhone2;
    private Button btnAccept;
    private Button btnContinue;
    private String smsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        editPhone = findViewById(R.id.editPhone);
        btnAccept = findViewById(R.id.btn_Accept);
        editPhone2 = findViewById(R.id.editPhone2);
        btnContinue = findViewById(R.id.btnContinue);
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("TAG","onVerificationsCompleted");

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("TAG","onVerificationsFailed: " + e.getMessage() );
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                smsCode = s;
            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PhoneActivity.this,"Успешно", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PhoneActivity.this,MainActivity.class));
                    finish();

                }else {
                    Log.e("TAG","Error: " + task.getException().getMessage());
                    Toast.makeText(PhoneActivity.this,"Ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClick(View view) {

        editPhone.setVisibility(View.GONE);
        editPhone2.setVisibility(View.VISIBLE);
        String phone = editPhone.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks);
                btnContinue.setVisibility(View.GONE);
                btnAccept.setVisibility(View.VISIBLE);
    }

    public void onClickAccept(View view) {
        String phone2 = editPhone2.getText().toString();
        phoneAuthCredential = PhoneAuthProvider.getCredential(smsCode,phone2);
        signIn(phoneAuthCredential);
    }
}
