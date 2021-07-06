package com.example.bankapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.bankapp.Database.Room.User;
import com.example.bankapp.Database.SharedPreferences.LoginPreferences;
import com.example.bankapp.Database.ViewModel.UserViewModel;
import com.example.bankapp.R;
import com.example.bankapp.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends BaseActivity {

    private EditText etEmail, etAccount, etPhone, etPassword, etCPassword, etName;
    private Button btnSignUp;
    private String email, password, cPassword, account, phone, name;
    private MaterialToolbar toolbar;
    private UserViewModel userViewModel;
    private DatabaseReference databaseRef;
    private FirebaseDatabase firebaseDatabase;
    private static final String TAG = "SignUpActivity";
    private static  String  key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_sign_up);
        initViews();
        setSupportActionBar(toolbar);
        userViewModel = UserViewModel.of(this);

        firebaseDatabase = FirebaseDatabase.getInstance();

        btnSignUp.setOnClickListener(v -> {
            email = etEmail.getText().toString();
            name = etName.getText().toString();
            password = etPassword.getText().toString();
            cPassword = etCPassword.getText().toString();
            account = etAccount.getText().toString();
            phone = etPhone.getText().toString();

            if (name.isEmpty()) {
                showToast("Please fill Name");
                return;
            }

            if (email.isEmpty()) {
                showToast("Please fill Email Id");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Invalid Email Id");
                return;
            }
            if (password.isEmpty()) {
                showToast("Please fill password");
                return;
            }
            if (password.length() < 6) {
                showToast("Password should be minimum 6 characters");
                return;
            }
            if (!TextUtils.equals(password, cPassword)) {
                showToast("Password and confirm password doesn't match");
                return;
            }
            if (phone.isEmpty()) {
                showToast("Please fill phone number");
                return;
            }

            if (phone.length() != 10 || !phone.matches("[0-9]+")) {
                showToast("Invalid Phone Number");
                return;
            }

            if (account.isEmpty()) {
                showToast("Please enter account number");
                return;
            }

            //getUserDAO().findByEmail(email).observe(this, (Observer<User>) existing1 -> {
            userViewModel.userByEmail(this, email, (existing1, error, successful) -> {
                if (existing1 != null) {
                    showToast("Entered email id already exists");
                    return;
                }
                userViewModel.userByAccount(this, account, (existing, error0, successful0) -> {
                    if (existing != null) {
                        showToast("Entered account already exists");
                        return;
                    }

                    Users user = new Users();
                    user.setEmail(email);
                    user.setAccountNumber(account);
                    user.setPassword(password);
                    user.setPhoneNumber(phone);
                    user.setName(name);
                    user.setCreatedAt(new Date());



                    /*userViewModel.createOrUpdate(SignUpActivity.this, user, (result, error1, successful1) -> {
                        //LoginPreferences.getInstance().saveUserInfo(user);

                    });*/

                    if (email.contains("agent")) {
                        user.setAgent(true);
                        saveAgentDetails(user);
                    } else {
                        databaseRef = firebaseDatabase.getReference("Users");
                        user.setAgent(false);
                        Log.e(TAG, "user: "+user.getEmail());
                        key = databaseRef.push().getKey();
                        assert key != null;
                        databaseRef.child(key).setValue(user)
                                .addOnCompleteListener(task -> {
                                    Toast.makeText(SignUpActivity.this, "User Details Added successfully",
                                            Toast.LENGTH_SHORT).show();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                                            .setTitle("Alert")
                                            .setMessage("Sign Up Successful!!")
                                            .setPositiveButton("Okay", (dialogInterface, i) -> {
                                                Intent intent;
                                                if (email.contains("agent")) {

                                                    intent = new Intent(SignUpActivity.this, AgentHome.class);
                                                    intent.putExtra("name", name);
                                                    intent.putExtra("email", email);
                                                    intent.putExtra("account_number", account);

                                                } else {
                                                    intent = new Intent(SignUpActivity.this, UserHome.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                }
                                                startActivity(intent);
                                            });
                                    builder.create().show();
                                });
                     //   databaseRef.child("pq0FeZi5QRXH6jHC8IyQBdc9Zn43").removeValue();
                    }
                    Log.e(TAG, "onCreate: "+key );
                    SharedPreferences preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("key", key);
                    editor.putString("username", name);
                    editor.putString("email", email);
                    editor.putBoolean("isAdmin",false);
                    editor.putBoolean("isAgent",false);
                    editor.apply();

                });

            });
            
        });
    }

    private void saveAgentDetails(Users user) {
        Log.e("TAG", "user: "+user);
        databaseRef = firebaseDatabase.getReference("Agents");
        key = databaseRef.push().getKey();
        assert key != null;
        databaseRef.child(key).setValue(user)
                .addOnCompleteListener(task -> {



                    Toast.makeText(SignUpActivity.this, "User Details Added successfully",
                            Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle("Alert")
                            .setMessage("Sign Up Successful!!")
                            .setPositiveButton("Okay", (dialogInterface, i) -> {
                                Intent intent = new Intent(SignUpActivity.this, UserHome.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            });
                    builder.create().show();
                });
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etAccount = findViewById(R.id.etAccount);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnSignUp = findViewById(R.id.btnSignUp);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}