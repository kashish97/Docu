package com.appsnipp.modernlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Date;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText name, email, password, cnfpassword;
    Button mRegisterbtn;
   // TextView mLoginPageBack;
    FirebaseAuth mAuth;
    DatabaseReference mdatabase;
    String Name, Email, Password;
    ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Dermatologist", "Gynae", "Skin", "Surgery");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        changeStatusBarColor();
        name = (EditText) findViewById(R.id.ed1);
        email = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText4);
        cnfpassword = (EditText) findViewById(R.id.editText5);

        mRegisterbtn = (Button) findViewById(R.id.button3);
     //   mLoginPageBack = (TextView) findViewById(R.id.cirLoginButton);
        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        mRegisterbtn.setOnClickListener((View.OnClickListener) this);
   //     mLoginPageBack.setOnClickListener((View.OnClickListener) this);
        mDialog = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    }
    @Override
    public void onClick(View v) {
        if (v==mRegisterbtn){
            if(!password.getText().toString().trim().equals(cnfpassword.getText().toString().trim()))
                Toast.makeText(getApplicationContext(), "Password and Confirm Password do not match", Toast.LENGTH_LONG).show();
            else
            UserRegister();
        }//else if (v== mLoginPageBack){
           // startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        //}
    }

    private void UserRegister() {
        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Name)){
            Toast.makeText(RegisterActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Email)){
            Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Password)){
            Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }else if (Password.length()<6){
            Toast.makeText(RegisterActivity.this,"Passwor must be greater then 6 digit",Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //sendEmailVerification();
                    mDialog.dismiss();
                    //OnAuth(task.getResult().getUser());
                  //  mAuth.signOut();
                    Intent intent = new Intent(RegisterActivity.this, CertiActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(RegisterActivity.this,"error on creating user",Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }
        });
    }
    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);
    }


    private User BuildNewuser(){
        return new User(
                getDisplayName(),
                getUserEmail(),
                new Date().getTime()
        );
    }

    public String getDisplayName() {
        return Name;
    }

    public String getUserEmail() {
        return Email;
    }






    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }




}
