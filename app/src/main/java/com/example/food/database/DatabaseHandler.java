package com.example.food.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.food.models.FoodModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 21;
    private  static final String DATABASE_NAME = "CanteenDb";



    private static  final String TABLE_CART = "cart";
    private static final  String KEY_ID = "id";
    private static final  String KEY_CATEGORY = "category";
    private static final  String KEY_NAME = "name";
  //  private static final  String KEY_AUTHOR = "author";
    private static final  String KEY_DESCRIPTION = "description";
    private static final  String KEY_IMAGE = "image";
   // private static final  String KEY_PUBLISHED_YEAR = "published_year";
    private static final  String KEY_PRICE = "price";
    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART +
                "(" + KEY_ID + " TEXT PRIMARY KEY, " +
                KEY_CATEGORY + " TEXT, " +
                KEY_NAME + " TEXT, " +
          //      KEY_AUTHOR + " TEXT, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_IMAGE + " TEXT, " +
         //       KEY_PUBLISHED_YEAR + " TEXT, " +
                KEY_PRICE + " TEXT)";

        db.execSQL(CREATE_CART_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    public void addToCart(FoodModel foodModel) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, foodModel.getId());
        contentValues.put(KEY_CATEGORY, foodModel.getCategory());
        contentValues.put(KEY_NAME, foodModel.getName());

        contentValues.put(KEY_IMAGE, foodModel.getImage());
        contentValues.put(KEY_DESCRIPTION, foodModel.getDescription());
        contentValues.put(KEY_PRICE, foodModel.getPrice());


        sqLiteDatabase.insert(TABLE_CART, null, contentValues);
        sqLiteDatabase.close();

    }

    public List<FoodModel> getCartItems() {
        String selectQuery = "SELECT * FROM " + TABLE_CART;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        List<FoodModel> cartItems = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {

            do {
                FoodModel foodModel = new FoodModel();

                foodModel.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                foodModel.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                foodModel.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));

                foodModel.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                foodModel.setImage(cursor.getString(cursor.getColumnIndex(KEY_IMAGE)));

                foodModel.setPrice(cursor.getString(cursor.getColumnIndex(KEY_PRICE)));

                cartItems.add(foodModel);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    public void deleteCart() {
        String deleteQuery = "DELETE FROM " + TABLE_CART;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(deleteQuery);
    }

    public void removeItem(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CART, KEY_ID + " = ?", new String[]{id});
        sqLiteDatabase.close();
    }

}
