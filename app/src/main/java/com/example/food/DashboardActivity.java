package com.example.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.food.models.CategoryModel;
import com.example.food.models.CategoryResponseModel;
import com.example.food.network.NetworkClient;
import com.example.food.network.NetworkService;
import com.example.food.util.Constants;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    String url1 = "data:image/jpeg;base64,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";
    String url2 = "https://www.silverkris.com/wp-content/uploads/2018/01/Rajwadu3.jpg";
    String url3 = "https://i.ndtvimg.com/i/2017-07/gujarati-thali-620x350_620x350_81500012551.jpg";

    DrawerLayout drawerLayout;
    ImageView imageMenu;
    TextView textUsername, textEmail;
    LinearLayout layoutLogin, layoutLogout, layout_books, layoutorderhistory, layoutcart, layoutwallet;
    View viewLogin, viewLogout;
    RecyclerView categoriesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        imageMenu = findViewById(R.id.image_menu);
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view);
        viewLogin = findViewById(R.id.view_login);
        viewLogout = findViewById(R.id.view_logout);
        layoutLogin = findViewById(R.id.layout_login);
        layoutLogout = findViewById(R.id.layout_logout);

        layoutcart = findViewById(R.id.layout_cart);
        layoutwallet = findViewById(R.id.layout_wallet);
        layoutorderhistory = findViewById(R.id.layout_orderhistory);
        textUsername = findViewById(R.id.text_username);
        textEmail = findViewById(R.id.text_email);

        GridLayoutManager layoutManager = new GridLayoutManager(
                this,2
        );
        categoriesRecyclerView.setLayoutManager(layoutManager);


       // categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        categoriesRecyclerView.setHasFixedSize(true);


        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(Constants.KEY_ISE_LOGGED_IN, false);

        if(!isLoggedIn) {
            textUsername.setText(R.string.welcome_guest);
            textUsername.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.VISIBLE);
            viewLogin.setVisibility(View.VISIBLE);
            layoutLogout.setVisibility(View.GONE);
            viewLogout.setVisibility(View.GONE);
        } else {
            textUsername.setText(preferences.getString(Constants.KEY_USERNAME, "N/A"));
            textEmail.setText(preferences.getString(Constants.KEY_EMAIL, "N/A"));
            textUsername.setVisibility(View.VISIBLE);
            textEmail.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
            viewLogin.setVisibility(View.GONE);
            layoutLogout.setVisibility(View.VISIBLE);
            viewLogout.setVisibility(View.VISIBLE);

        }

        // we are creating array list for storing our image urls.
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

        imageMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

//        findViewById(R.id.card_books).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
//            }
//        });
//        findViewById(R.id.card_cart).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), CartActivity.class));
//            }
//        });
//        findViewById(R.id.card_basket).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), MyOrdersActivity.class));
//            }
//        });
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Do you want to logout?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(DashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });

        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });


//        layout_books.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
//            }
//        });

        layoutcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });

        layoutwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WalletActivity.class));
            }
        });
        layoutorderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyOrdersActivity.class));
            }
        });
        getCategories();
    }

    private void getCategories() {

        final ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Getting categories");
        progressDialog.setCancelable(false);
        progressDialog.show();

        NetworkService networkService = NetworkClient.getClient().create(NetworkService.class);

        Call<CategoryResponseModel> categoryResponseModelCall = networkService.getCategoriesFromServer();
        categoryResponseModelCall.enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponseModel> call, @NonNull Response<CategoryResponseModel> response) {

                CategoryResponseModel categoryResponseModel = response.body();
                CategoriesAdpter categoriesAdpter = new CategoriesAdpter(
                        categoryResponseModel.getCategories()
                );

                categoriesRecyclerView.setAdapter(categoriesAdpter);
                progressDialog.cancel();

            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });

    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {

        CardView categoryItemLayout;
        TextView textCategory;
        ImageView imgFood;

        CategoryViewHolder(View view)
        {
            super(view);

            categoryItemLayout = (CardView)view.findViewById(R.id.category_card_view);
            textCategory = (TextView)view.findViewById(R.id.text_category);
            imgFood = (ImageView)view.findViewById(R.id.img_Food);
        }
    }
    private class  CategoriesAdpter extends RecyclerView.Adapter<CategoryViewHolder> {

        List<CategoryModel> categories;

        CategoriesAdpter(List<CategoryModel> categories) {
            this.categories = categories;
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.category_item_container, parent, false));
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {




            if(categories.get(holder.getAdapterPosition()).getCategory() != null) {
                holder.textCategory.setText(categories.get(holder.getAdapterPosition()).getCategory());
                holder.categoryItemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(), FoodActivity.class);
                        intent.putExtra("category", categories.get(holder.getAdapterPosition()).getCategory());
                        startActivity(intent);
                    }
                });
            }



            if (categories.get(holder.getAdapterPosition()).getImage() != null) {
                Picasso.with(getApplicationContext()).load(
                        categories.get(position).getImage()
                ).into(holder.imgFood);
            } else {
                holder.imgFood.setVisibility(View.GONE);
            }
        }
    }


}