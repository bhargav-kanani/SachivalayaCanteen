package com.example.food;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food.models.CityModel;
import com.example.food.models.CountryModel;
import com.example.food.models.RegistrationResponseModel;
import com.example.food.models.StateModel;
import com.example.food.network.NetworkClient;
import com.example.food.network.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    Spinner countrySpinner, stateSpinner, citySpinner;
    EditText inputFirstName, inputLastName, inputMobile, inputEmail, inputPassword, inputDesignation, inputIdentityNo;
    RadioButton radioMale, radioFemale;
    Button buttonRegister;
    boolean isGenderSelected;
    String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        inputFirstName = findViewById(R.id.input_first_name);
        inputLastName = findViewById(R.id.input_last_name);
        inputMobile = findViewById(R.id.input_mobile);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputDesignation = findViewById(R.id.input_designation);
        inputIdentityNo = findViewById(R.id.input_identity_no);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        buttonRegister = findViewById(R.id.button_register);



        radioMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isGenderSelected = true;
                    selectedGender = "Male";
                }
            }
        });
        radioFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isGenderSelected = true;
                    selectedGender = "Female";
                }
            }
        });



        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputFirstName.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter first name",Toast.LENGTH_SHORT).show();
                } else if(inputLastName.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter last name",Toast.LENGTH_SHORT).show();
                } else if(!isGenderSelected) {
                    Toast.makeText(RegistrationActivity.this, "Select gender",Toast.LENGTH_SHORT).show();
                } else if(inputEmail.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter email",Toast.LENGTH_SHORT).show();
                } else if(!emailValidator(inputEmail.getText().toString())) {
                    Toast.makeText(RegistrationActivity.this, "Enter valid email",Toast.LENGTH_SHORT).show();
                } else if(inputMobile.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Enter mobile no.",Toast.LENGTH_SHORT).show();
                } else if(inputMobile.getText().toString().length() < 10) {
                    Toast.makeText(RegistrationActivity.this, "Enter valid mobile no.",Toast.LENGTH_SHORT).show();
                } else if(inputDesignation.getText().toString().equals("")){
                    Toast.makeText(RegistrationActivity.this, "Enter Designation",Toast.LENGTH_SHORT).show();
                } else if(inputIdentityNo.getText().toString().equals("")){
                    Toast.makeText(RegistrationActivity.this, "Enter Identity no.",Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("first_name", inputFirstName.getText().toString());
                    params.put("last_name", inputLastName.getText().toString());
                    params.put("gender", selectedGender);
                    params.put("email", inputEmail.getText().toString());
                    params.put("phone", inputMobile.getText().toString());
                    ;params.put("emp_desg", inputDesignation.getText().toString());
                    params.put("emp_id_card_no", inputIdentityNo.getText().toString());

                    register(params);
                }
            }
        });
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void register(HashMap<String, String> params) {

        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        NetworkService networkService = NetworkClient.getClient().create(NetworkService.class);
        Call<RegistrationResponseModel> registerCall = networkService.register(params);
        registerCall.enqueue(new Callback<RegistrationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponseModel> call, @NonNull Response<RegistrationResponseModel> response) {
                    RegistrationResponseModel responseBody = response.body();
                    if (responseBody != null) {
                        if (responseBody.getSuccess().equals("1")) {
                            Toast.makeText(RegistrationActivity.this, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this, responseBody.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponseModel> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

}