package com.example.bankapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.bankapp.Database.Room.User;
import com.example.bankapp.Database.SharedPreferences.LoginPreferences;
import com.example.bankapp.Database.ViewModel.UserViewModel;
import com.example.bankapp.R;
import com.example.bankapp.model.Users;
import com.example.bankapp.model.UsersBean;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    private TextView signUp;
    private EditText etEmail, etPassword;
    private Button login;
    private String email, password;
    private MaterialToolbar toolbar;
    private UserViewModel userViewModel;

    private ProgressBar progressbar;

    DatabaseReference databaseReference;
    SharedPreferences preferences;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        Log.e(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }

        userViewModel = UserViewModel.of(this);

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();

            if (email.contains("Agents")) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Agents");
            } else if (email.contains("admin")) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
            } else {
                databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            }

            progressbar.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter Email ID", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Boolean flag = false;
                    for (DataSnapshot shot : snapshot.getChildren()) {
                        Users users = shot.getValue(Users.class);
                        Log.e(TAG, "users: " + users.getEmail() + " isagent " + users.getAgent() + " is admin " + users.getAdmin());

                        String email = users.getEmail();
                        String password = users.getPassword();
                       if (email != null || password != null) {
                            if (email.equals(etEmail.getText().toString().trim()) &&
                                    password.equals(etPassword.getText().toString().trim())) {

                                if (users.getAdmin()) {
                                    Log.e(TAG, "onDataChange: contains admin");
                                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                    intent.putExtra("name", users.getName());
                                    intent.putExtra("email", users.getEmail());
                                    intent.putExtra("account_number", users.getAccountNumber());
                                    startActivity(intent);
                                    finish();
                                } else if (users.getAgent()) {
                                    Log.e(TAG, "onDataChange: contains agent");
                                    Intent intent = new Intent(MainActivity.this, AgentHome.class);
                                    intent.putExtra("name", users.getName());
                                    intent.putExtra("email", users.getEmail());
                                    intent.putExtra("account_number", users.getAccountNumber());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.e(TAG, "onDataChange: contains no agent");
                                    Intent intent = new Intent(MainActivity.this, UserHome.class);
                                    intent.putExtra("name", users.getName());
                                    intent.putExtra("email", users.getEmail());
                                    intent.putExtra("account_number", users.getAccountNumber());
                                    startActivity(intent);
                                    finish();
                                }
                                flag = true;
                            }
                        }


                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("key", shot.getKey());
                        editor.putString("username", users.getName());
                        editor.putString("email", users.getEmail());
                        editor.putBoolean("isAdmin",users.getAdmin());
                        editor.putBoolean("isAgent",users.getAgent());
                        editor.apply();
                        if(flag)
                            break;
                    }
                    progressbar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    progressbar.setVisibility(View.INVISIBLE);
                }
            });

        });


//        login.setOnLongClickListener(view -> {
////            userViewModel.mockDate(this, (result, error, successful) -> {
////                if(successful){
////                    Toast.makeText(MainActivity.this, "Mocking completed", Toast.LENGTH_LONG).show();
////                    login.setOnClickListener(v -> {
////                        email = etEmail.getText().toString();
////                        password = etPassword.getText().toString();
////
////                        if (email.contains("Agents")) {
////                            databaseReference = FirebaseDatabase.getInstance().getReference("Agents");
////                        } else if (email.contains("admin")) {
////                            databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
////                        } else  {
////                            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
////                        }
////
////                        progressbar.setVisibility(View.VISIBLE);
////
////                        if (TextUtils.isEmpty(email)) {
////                            Toast.makeText(this, "Enter Email ID", Toast.LENGTH_SHORT).show();
////                            return;
////                        }
////
////                        if (TextUtils.isEmpty(password)) {
////                            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
////                            return;
////                        }
////
////                        /*if (email.equals("pksv@admin") && password.equals("pksv")) {
////
////                            Query query = databaseReference.orderByChild("email").equalTo(email);
////                            query.addListenerForSingleValueEvent(new ValueEventListener() {
////                                @Override
////                                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
////
////                                    User user1 = dataSnapshot.getValue(User.class);
////                                    Log.d(TAG, "user: "+user1);
////
////                                    if (dataSnapshot.exists()) {
////                                        // dataSnapshot is the "issue" node with all children with id 0
////
////                                        for (DataSnapshot user : dataSnapshot.getChildren()) {
////                                            // do something with the individual "issues"
////                                            Log.d(TAG, "onDataChange: "+user);
////                                            UsersBean usersBean = user.getValue(UsersBean.class);
////
////                                            if (usersBean.getPassword().equals(password)) {
////                                                progressbar.setVisibility(View.GONE);
////                                                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
////                                                startActivity(intent);
////                                            } else {
////                                                progressbar.setVisibility(View.GONE);
////                                                Toast.makeText(MainActivity.this, "Password is wrong", Toast.LENGTH_LONG).show();
////                                            }
////                                        }
////                                    } else {
////                                        progressbar.setVisibility(View.GONE);
////                                        Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_LONG).show();
////                                    }
////                                }
////
////                                @Override
////                                public void onCancelled(DatabaseError databaseError) {
////
////                                }
////                            });
////                        }
////
////                        else {
////                            Query query = databaseReference.child("user1").child("Agents")
////                                    .orderByChild("email").equalTo(etEmail.getText().toString().trim());
////                            Log.e("TAG", "query: "+query);
////                            query.addListenerForSingleValueEvent(new ValueEventListener() {
////                                @Override
////                                public void onDataChange(DataSnapshot dataSnapshot) {
////                                    Log.d(TAG, "onDataChange: "+dataSnapshot.getRef().getParent());
////                                    if (dataSnapshot.exists()) {
////                                        // dataSnapshot is the "issue" node with all children with id 0
////                                        for (DataSnapshot user : dataSnapshot.getChildren()) {
////                                            // do something with the individual "issues"
////                                            Log.d(TAG, "onDataChange: "+user);
////                                            UsersBean usersBean = user.getValue(UsersBean.class);
////
////                                            if (usersBean.getPassword().equals(password)) {
////                                                progressbar.setVisibility(View.GONE);
////                                                Intent intent = new Intent(MainActivity.this, AgentHome.class);
////                                                intent.putExtra("name",usersBean.getName());
////                                                intent.putExtra("email",usersBean.getEmail());
////                                                intent.putExtra("account_number",usersBean.getAccount_no());
////                                                intent.putExtra("balance",usersBean.getBalance());
////                                                Log.d(TAG, "onDataChange: "+usersBean.getAccount_no()+"\n"+usersBean.getBalance()+"\n"+usersBean.getName());
////                                                startActivity(intent);
////                                            } else {
////                                                progressbar.setVisibility(View.GONE);
////                                                Toast.makeText(MainActivity.this, "Password is wrong", Toast.LENGTH_LONG).show();
////                                            }
////                                        }
////                                    } else {
////                                        progressbar.setVisibility(View.GONE);
////                                        Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_LONG).show();
////                                    }
////                                }
////
////                                @Override
////                                public void onCancelled(DatabaseError databaseError) {
////                                    progressbar.setVisibility(View.GONE);
////                                }
////                            });
////
////                            databaseReference.addValueEventListener(new ValueEventListener() {
////                                @Override
////                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
////                                    Log.e("TAG", "email: "+snapshot.child("email").getValue());
////                                    String email = snapshot.child("email").getValue().toString();
////                                    String password = snapshot.child("password").getValue().toString();
////                                    if (email.equals(etEmail.getText().toString().trim()) &&
////                                    password.equals(etPassword.getText().toString().trim())) {
////                                        startActivity(new Intent(MainActivity.this, UserHome.class));
////                                        finish();
////                                    }
////                                }
////
////                                @Override
////                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
////
////                                }
////                            });
////                        }*/
////
////                        databaseReference.addValueEventListener(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
////
////                                for (DataSnapshot shot : snapshot.getChildren()) {
////                                    Users users = shot.getValue(Users.class);
////                                    Log.e("TAG", "users: " + users.getEmail());
////
////                                    String email = users.getEmail();
////                                    String password = users.getPassword();
////
////                                    if (email.contains("agents")) {
////
////                                        if (email.equals(etEmail.getText().toString().trim()) &&
////                                                password.equals(etPassword.getText().toString().trim())) {
////                                            Intent intent = new Intent(MainActivity.this, AgentHome.class);
////                                            intent.putExtra("name", users.getName());
////                                            intent.putExtra("email", users.getEmail());
////                                            intent.putExtra("account_number", users.getAccountNumber());
////                                            startActivity(intent);
////                                            finish();
////                                        }
////
////                                    }
////                                    else {
////
////                                        if (email.equals(etEmail.getText().toString().trim()) &&
////                                                password.equals(etPassword.getText().toString().trim())) {
////                                            startActivity(new Intent(MainActivity.this, UserHome.class));
////                                            finish();
////                                        }
////
////                                        if (email.equals("pksv@admin") &&
////                                                password.equals("pksv")) {
////                                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
////                                            finish();
////                                        }
////
////                                    }
////
////                                    SharedPreferences.Editor editor = preferences.edit();
////                                    editor.putString("key", shot.getKey());
////                                    editor.putString("username", users.getName());
////                                    editor.putString("email", users.getEmail());
////                                    editor.apply();
////                                }
////                                progressbar.setVisibility(View.INVISIBLE);
////                            }
////
////                            @Override
////                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
////                                progressbar.setVisibility(View.INVISIBLE);
////                            }
////                        });
////
////                    });
////                }else {
////                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
////                }
////            });
//            return true;
//        });

    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        login = findViewById(R.id.btnSignUp);
        signUp = findViewById(R.id.tvSignUp);
        toolbar = findViewById(R.id.toolbar);

        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);
    }


}
