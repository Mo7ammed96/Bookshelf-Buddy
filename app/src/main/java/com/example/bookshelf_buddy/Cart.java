package com.example.bookshelf_buddy;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {
    ImageView backIcon;
    Button btnClearCart;
    FirebaseAuth auth;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ListView listView = findViewById(R.id.cart_items_list);
        btnClearCart = findViewById(R.id.btnClearCart);
        backIcon = findViewById(R.id.back);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Check if a user is logged in
        if (user == null) {
            // Handle the case where no user is logged in (e.g., redirect to login)
            Intent loginIntent = new Intent(this, Login.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Now you can safely get the user's ID
        String userId = user.getUid();

        // Create a Firebase database reference for the cart
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
                .child("cart");

        // Listen for changes in the cart data
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CartItem> cartItems = new ArrayList<>();

                backIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                // Iterate through the cart items in Firebase
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    CartItem item = itemSnapshot.getValue(CartItem.class);
                    cartItems.add(item);
                }

                // Create an adapter to display cart items
                ArrayAdapter<CartItem> adapter = new ArrayAdapter<>(Cart.this, android.R.layout.simple_list_item_1, cartItems);
                listView.setAdapter(adapter);

                // Calculate and display the total price
                double totalPrice = calculateTotalPrice(cartItems);
                TextView totalTextView = findViewById(R.id.cart_total);
                totalTextView.setText("Total: OMR " + String.format("%.2f", totalPrice));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database errors, if any
            }
        });

        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event to clear the cart in Firebase
                clearCartInFirebase(userId);
            }
        });
    }

    // Helper method to calculate the total price of cart items
    private double calculateTotalPrice(List<CartItem> items) {
        double totalPrice = 0.0;
        for (CartItem item : items) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }

    private void clearCartInFirebase(String userId) {
        // Create a Firebase database reference for the user's cart
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
                .child("cart");

        // Remove all cart items
        cartRef.removeValue();

        // Reset the total price to zero
        DatabaseReference totalRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
                .child("total_price");

        totalRef.setValue(0.0);

        // Display a message to indicate that the cart has been cleared
        Toast.makeText(Cart.this, "Cart cleared", Toast.LENGTH_SHORT).show();
    }
}
