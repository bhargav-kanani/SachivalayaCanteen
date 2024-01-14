package com.example.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.food.models.FoodModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    RecyclerView orderRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderRecyclerView = findViewById(R.id.books_recycler_view);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        List<FoodModel> books = new ArrayList<>();

        String products = getIntent().getStringExtra("products");
        Gson gson = new Gson();
        FoodModel[] bookItems = gson.fromJson(products, FoodModel[].class);
        books = Arrays.asList(bookItems);
        books = new ArrayList<>(books);

        orderRecyclerView.setAdapter(new BooksAdapter(books));

        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

        List<FoodModel> books;

        BooksAdapter(List<FoodModel> books) {
            this.books = books;
        }

        @Override
        public int getItemCount() {
            return books.size();
        }

        @NonNull
        @Override
        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.food_item_container, parent, false
            ));
        }


        public void onBindViewHolder(final BookViewHolder holder, int position) {

            if (books.get(position).getName() != null && !books.get(position).getName().equals("")) {
                holder.textName.setText(books.get(position).getName());

            } else {
                holder.textName.setVisibility(View.GONE);
            }

            if (books.get(position).getPrice() != null && !books.get(position).getPrice().equals("")) {
                holder.textPrice.setText("\u20B9" + books.get(position).getPrice());

            } else {
                holder.textPrice.setVisibility(View.GONE);
            }



            if (books.get(position).getDescription() != null && !books.get(position).getDescription().equals("")) {
                holder.textDescription.setText(books.get(position).getDescription());

            } else {
                holder.textDescription.setVisibility(View.GONE);
            }


            if (books.get(position).getImage() != null && !books.get(position).getImage().equals("")) {
                Picasso.with(getApplicationContext()).load(
                        books.get(position).getImage()
                ).into(holder.imageBook);
            } else {
                holder.imageBook.setVisibility(View.GONE);
            }

//            holder.cardBook.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), BookDetailActivity.class);
//                    intent.putExtra("image", books.get(holder.getAdapterPosition()).getImage());
//                    intent.putExtra("description", books.get(holder.getAdapterPosition()).getDescription());
//                    intent.putExtra("price", books.get(holder.getAdapterPosition()).getPrice());
//                    intent.putExtra("name", books.get(holder.getAdapterPosition()).getName());
//                    intent.putExtra("id", books.get(holder.getAdapterPosition()).getId());
//                    intent.putExtra("category", books.get(holder.getAdapterPosition()).getCategory());
//
//                    startActivity(intent);
//
//
//                }
//            });


        }

        class BookViewHolder extends RecyclerView.ViewHolder {

            CardView cardBook;
            ImageView imageBook;
            TextView textName, textAuthor, textPrice, textDescription;

            BookViewHolder(View view) {
                super(view);

                cardBook = (CardView) view.findViewById(R.id.book_card_view);
                imageBook = (ImageView) view.findViewById(R.id.image_food);
                textName = (TextView) view.findViewById(R.id.text_book_name);
                textPrice = (TextView) view.findViewById(R.id.text_book_price);
                textDescription = (TextView) view.findViewById(R.id.text_description);

            }
        }
    }

}