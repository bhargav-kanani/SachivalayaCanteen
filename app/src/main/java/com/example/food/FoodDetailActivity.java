package com.example.food;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.slider.library.SliderLayout;

import com.example.food.database.DatabaseHandler;
import com.example.food.models.FoodModel;
import com.squareup.picasso.Picasso;


public class FoodDetailActivity extends AppCompatActivity {

    ImageView imageAddToCart, imageFood;
    TextView textAuthor, textPublishedYear, textDescription, textPrice, textToolbarTitle;
    SliderLayout sliderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        sliderLayout = findViewById(R.id.slider);

        imageFood = findViewById(R.id.image_food_detail);
        imageAddToCart = findViewById(R.id.add_to_cart);
//        textAuthor = findViewById(R.id.text_author);
//        textPublishedYear = findViewById(R.id.text_published_year);
        textDescription = findViewById(R.id.text_description);
        textPrice = findViewById(R.id.text_price);
        textToolbarTitle = findViewById(R.id.text_toolbar_title);



        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("image")).into(imageFood);

        //textAuthor.setText("By :" + getIntent().getStringExtra("author"));
        //textPublishedYear.setText("Published :" + getIntent().getStringExtra("publishedYear"));
        textDescription.setText(getIntent().getStringExtra("description"));
        textToolbarTitle.setText(getIntent().getStringExtra("name"));
        textPrice.setText("\u20B9" + getIntent().getStringExtra("price"));


        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        imageAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodModel foodModel = new FoodModel();
                foodModel.setId(getIntent().getStringExtra("id"));
                foodModel.setCategory(getIntent().getStringExtra("category"));
                foodModel.setName(getIntent().getStringExtra("name"));
                foodModel.setDescription(getIntent().getStringExtra("description"));

                foodModel.setImage(getIntent().getStringExtra("image"));

                foodModel.setPrice(getIntent().getStringExtra("price"));

                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                databaseHandler.addToCart(foodModel);
                Toast.makeText(FoodDetailActivity.this, "Added to cart!", Toast.LENGTH_SHORT).show();


            }
        });


    }
}