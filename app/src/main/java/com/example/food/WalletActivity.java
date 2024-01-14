package com.example.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.models.OrderModel;
import com.example.food.models.OrderResponseModel;
import com.example.food.network.NetworkClient;
import com.example.food.network.NetworkService;
import com.example.food.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {


    TextView mybalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(Constants.KEY_ISE_LOGGED_IN, false);
        mybalance = (TextView) findViewById(R.id.my_balance);
        mybalance.setText("\u20B9" + 0);

        if (!isLoggedIn) {
            Toast.makeText(WalletActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            getUserWallet();
        }


       // getUserWallet();


    }

    private void getUserWallet(){
        final ProgressDialog progressDialog = new ProgressDialog(WalletActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Getting detail...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);

        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        Call<OrderResponseModel> getOrdersCall = service.getOrders(preferences.getString(Constants.KEY_EMAIL, "N/A"));
        getOrdersCall.enqueue(new Callback<OrderResponseModel>() {
            @Override
            public void onResponse(Call<OrderResponseModel> call, Response<OrderResponseModel> response) {
                OrderResponseModel orderResponse = response.body();
              //  Toast.makeText(WalletActivity.this,orderResponse.getOrders().get(0).getAmount(),Toast.LENGTH_SHORT).show();
                mybalance.setText("\u20B9" + orderResponse.getOrders().get(0).getWalletAmount());
//                if (orderResponse != null) {
//                    orderRecyclerView.setAdapter(new MyOrdersActivity.OrderAdapter(orderResponse.getOrders()));
//                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<OrderResponseModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

}