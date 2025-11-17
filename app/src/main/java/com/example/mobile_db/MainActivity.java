package com.example.mobile_db;

import static java.security.AccessController.getContext;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    ArrayList<HashMap<String, String>> categories;
    ArrayList<HashMap<String, String>> products;

    String selectedCategoryId;

    private SQLiteDatabase db;
    private ListView catList;
    private ListView prodList;
    public DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        helper = new DBHelper(this.getApplicationContext());
        try {
            db = helper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        catList = findViewById(R.id.CategoryListView);
        prodList = findViewById(R.id.ItemListView);

        catList.setOnItemClickListener(
                (parent, view, position, id) -> {

                    HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);
                    selectedCategoryId = item.get("id");
                    Log.d("ITEM SELECT PROD", "Selected category ID: " + selectedCategoryId);

                    loadProducts();
                }
        );
        loadCategories();


    }

    public void loadProducts() {
        try {
            HashMap<String, String> product;
            products = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT id, name, description, price FROM product where category_id=?", new String[]{selectedCategoryId});

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                product = new HashMap<>();

//                product.put("id", cursor.getString(1));
                product.put("name", cursor.getString(1));
                product.put("description", cursor.getString(2));
                product.put("price", cursor.getString(3));

                products.add(product);
                cursor.moveToNext();
            }
            cursor.close();
            SimpleAdapter adapter = new SimpleAdapter(this.getApplicationContext(), products, R.layout.product_item,
                    new String[]{"name", "description", "price"},
                    new int[]{R.id.ProductItemName, R.id.ProductItemDescription, R.id.ProductItemPrice}

            );
            prodList.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadCategories() {
        try {
            HashMap<String, String> category;
            categories = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT id, name FROM category", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                category = new HashMap<>();

                category.put("id", cursor.getString(0));
                category.put("name", cursor.getString(1));

                categories.add(category);
                cursor.moveToNext();
            }
            cursor.close();
            SimpleAdapter adapter = new SimpleAdapter(this.getApplicationContext(), categories, R.layout.scrollview_item,
                    new String[]{"name"},
                    new int[]{R.id.item_name}

            );
            catList.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}