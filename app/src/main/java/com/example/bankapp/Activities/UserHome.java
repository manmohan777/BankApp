package com.example.bankapp.Activities;



//TODO: have to replace User with Users

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.bankapp.Database.Room.User;
import com.example.bankapp.Database.SharedPreferences.AdminPreferences;
import com.example.bankapp.Database.ViewModel.UserViewModel;
import com.example.bankapp.R;
import com.example.bankapp.model.Users;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class UserHome extends BaseActivity {

    private MaterialToolbar toolbar;
    private ImageView logout;
    private TextView tvBsrBal, tvAccount, tvBal, tvConversion;
    private BottomNavigationView bottomNavigationView;
    private Spinner currencySpinner;
    private Button send;
    private EditText receiverAcc, sendingAmount;
    UserViewModel userViewModel;
    private double bal;
    User users;
    SharedPreferences preferences;

    DatabaseReference databaseReference;
    private static final String TAG = "UserHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        setContentView(R.layout.activity_user);
        initViews();
        setSupportActionBar(toolbar);
        initBottomNavMenu(bottomNavigationView, R.id.home);
        initLogout(logout);
        userViewModel = UserViewModel.of(this);

        preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        /*User.Info user = getLoggedUserInfo();
        userViewModel.userByEmail(this, user.getEmail(), (user1, error, successful) -> {
            //bal = user1.getBsrBal();
        });*/

        String[] currency = getResources().getStringArray(R.array.currency);


        ArrayAdapter adapter = new ArrayAdapter(UserHome.this, android.R.layout.simple_spinner_item, currency);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(adapter);

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.currencySpinner) {
                    double BSRRate = AdminPreferences.getInstance().getBSRRate();
                    String value = parent.getItemAtPosition(position).toString();
                    switch (value) {
                        case "INR":
                            tvBal.setText(String.format("Balance : ₹%s", (bal * BSRRate * 75.11)));
                            tvConversion.setText("1 BSR = " + (int)(BSRRate * 75.11) + " " + value);
                            break;
                        case "JPY":
                            tvBal.setText(String.format("Balance : ¥%s", (bal * BSRRate * 108.15)));
                            tvConversion.setText("1 BSR = " + (int)(BSRRate * 108.15) + " " + value);
                            break;
                        case "GBP":
                            tvBal.setText(String.format("Balance : £%s", (bal * BSRRate * 0.72)));
                            tvConversion.setText("1 BSR = " + (int)(BSRRate * 0.72) + " " + value);
                            break;
                        case "AED":
                            tvBal.setText(String.format("Balance : د.إ%s", (bal * BSRRate * 3.67)));
                            tvConversion.setText("1 BSR = " + (int)(BSRRate * 3.67) + " " + value);
                            break;
                        case "AUD":
                            tvBal.setText(String.format("Balance : A$%s", (bal * BSRRate * 1.29)));
                            tvConversion.setText("1 BSR = " + (int)(BSRRate * 1.29) + " " + value);
                            break;
                        default:
                            tvBal.setText(String.format("Balance : $%s", (bal * BSRRate)));
                            tvConversion.setText("1 BSR = " + (int)(BSRRate) + " " + value);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvBal.setText(String.format("Balance : $%s", (bal * 100)));
            }
        });

        getUserDetails();

        send.setOnClickListener(v -> {
            String amt = sendingAmount.getText().toString();
            String acc = receiverAcc.getText().toString();
            if (amt.isEmpty()) {
                showToast("Please enter the amount to be sent");
                return;
            }
            if (bal < Double.parseDouble(amt)) {
                showToast("Insufficient Balance");
                return;
            }
            if (acc.isEmpty()) {
                showToast("Please enter receiver's account number");
                return;
            }
            if (acc.equals(users.getAccountNo())) {
                showToast("Invalid receiver");
                return;
            }
            /*userViewModel.sendMoney(UserHome.this, users, acc, amt, (result, error1, successful1) -> {
                if(successful1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this)
                            .setTitle("Alert")
                            .setMessage("Money Sent")
                            .setPositiveButton("Okay", (dialogInterface, i) -> {
                                receiverAcc.setText("");
                                sendingAmount.setText("");
                                bal = result.getBsrBal();
                                tvBsrBal.setText(String.format("BSR Balance : %s", result.getBsrBal()));
                            });
                    builder.create().show();
                }else {
                    showToast(error1.getMessage());
                }
            });*/

        });
    }

    private void getUserDetails() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot shot: snapshot.getChildren()) {
                    SharedPreferences preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
                    String userKey =preferences.getString("key","");
//                  Log.e(TAG, "onDataChange: "+userKey +" shotkey"+ shot.getKey() );
                    Log.e("TAG", "shot: " + shot);
                    if(shot.getKey().equals("IflvhLDZb7V8ShJsRVQq8N3uJ593")||shot.getKey().equals("Q0EDPpVuywcSkBcSvDSItUSuTok1")||shot.getKey().equals("Q0EDPpVuywcSkBcSvDSItUSuTok1"))
                    shot.getRef().removeValue();



                    if(userKey!="" && userKey.equals(shot.getKey()) ) {
                        users = shot.getValue(User.class);
                        toolbar.setTitle("Welcome, " + users.getName());
                        tvAccount.setText(String.format("Account Number : %s", users.getAccountNo()));
                        tvBsrBal.setText(String.format("BSR Balance : %s", users.getBsrBal()));
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logout);
        tvAccount = findViewById(R.id.tvAccount);
        tvBsrBal = findViewById(R.id.tvBsrBal);
        tvBal = findViewById(R.id.tvBal);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        currencySpinner = findViewById(R.id.currencySpinner);
        send = findViewById(R.id.send);
        sendingAmount = findViewById(R.id.sendingAmount);
        receiverAcc = findViewById(R.id.receiverAcc);
        tvConversion = findViewById(R.id.tvConversionRate);
    }

    @Override
    public void onBackPressed() {
        showToast("Please Logout");
    }
}