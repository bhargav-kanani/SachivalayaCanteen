package com.example.food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.database.DatabaseHandler;
import com.example.food.models.FoodModel;
import com.example.food.models.PlaceOrderResponse;
import com.example.food.network.NetworkClient;
import com.example.food.network.NetworkService;
import com.example.food.util.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView foodRecyclerView;
    ImageView imageBack;
    TextView textTotalAmount;
    List<FoodModel> cartItems;
    Button butonPlaceOrder;
    int totalAmount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        textTotalAmount = findViewById(R.id.text_total_amount);
        imageBack = findViewById(R.id.image_back);
        imageBack.setOnClickListener(v -> onBackPressed());

        foodRecyclerView = findViewById(R.id.cart_recycler_view);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        foodRecyclerView.setHasFixedSize(true);

        cartItems = new DatabaseHandler(getApplicationContext()).getCartItems();
        foodRecyclerView.setAdapter(new BooksAdapter(cartItems));

        butonPlaceOrder = findViewById(R.id.button_place_order);
        butonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
                boolean isLoggedIn = preferences.getBoolean(Constants.KEY_ISE_LOGGED_IN, false);

                if (!isLoggedIn) {
                    Toast.makeText(CartActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_email", preferences.getString(Constants.KEY_EMAIL, "N/A"));
                    params.put("total_amount", String.valueOf(totalAmount));
                    params.put("products", new Gson().toJson(cartItems));
                    params.put("order_date", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                    placeOrder(params);
                }
            }
        });

        calculateTotal();
    }
        private void placeOrder(HashMap<String, String> params) {

            final ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Placing order...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            NetworkService networkService = NetworkClient.getClient().create(NetworkService.class);
            Call<PlaceOrderResponse> placeOrderCall = networkService.placeOrder(params);
            placeOrderCall.enqueue(new Callback<PlaceOrderResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlaceOrderResponse> call, @NonNull Response<PlaceOrderResponse> response) {
                    PlaceOrderResponse responseBody = response.body();
                    if (responseBody != null) {
                        if (responseBody.getSuccess().equals("1")) {
                            Toast.makeText(CartActivity.this, responseBody.getMessage(), Toast.LENGTH_LONG).show();
                            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                            databaseHandler.deleteCart();
                            finish();
                        } else {
                            Toast.makeText(CartActivity.this, responseBody.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<PlaceOrderResponse> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });



    }

    private  void calculateTotal() {

        totalAmount = 0;
        for(int i = 0; i <= cartItems.size()-1; i++) {
            totalAmount = totalAmount + Integer.parseInt(cartItems.get(i).getPrice());
        }

        textTotalAmount.setText("Total Amt. \u20B9 " + String.valueOf(totalAmount));
    }

    private class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

        List<FoodModel> food;

        BooksAdapter(List<FoodModel> food) {
            this.food = food;
        }

        @Override
        public int getItemCount() {
            return food.size();
        }


        @Override
        public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.cart_item_container, parent, false
            ));
        }

        @Override
        public void onBindViewHolder(final BookViewHolder holder, int position) {

            if (food.get(position).getName() != null && !food.get(position).getName().equals("")) {
                holder.textName.setText(food.get(position).getName());

            } else {
                holder.textName.setVisibility(View.GONE);
            }

            if (food.get(position).getPrice() != null && !food.get(position).getPrice().equals("")) {
                holder.textPrice.setText("\u20B9" + food.get(position).getPrice());

            } else {
                holder.textPrice.setVisibility(View.GONE);
            }




            Picasso.with(getApplicationContext()).load(
                    food.get(position).getImage()
            ).into(holder.imageBook);

            if (food.get(position).getImage() != null && !food.get(position).getImage().equals("")) {
                Picasso.with(getApplicationContext()).load(
                        food.get(position).getImage()
                ).into(holder.imageBook);
            } else {
                holder.imageBook.setVisibility(View.GONE);
            }

            holder.textRemoveFromCart.setPaintFlags(holder.textRemoveFromCart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.textRemoveFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatabaseHandler(getApplicationContext()).removeItem(food.get(holder.getAdapterPosition()).getId());
                    cartItems.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(),cartItems.size());
                    calculateTotal();
                }
            });

        }

        class BookViewHolder extends RecyclerView.ViewHolder {


            ImageView imageBook;
            TextView textName, textPrice, textRemoveFromCart;

            BookViewHolder(View view) {
                super(view);

                imageBook = (ImageView) view.findViewById(R.id.image_food);
                textName = (TextView) view.findViewById(R.id.text_food_name);
                textPrice = (TextView) view.findViewById(R.id.text_food_price);
                textRemoveFromCart = (TextView) view.findViewById(R.id.text_remove_item);

            }
        }
    }

}