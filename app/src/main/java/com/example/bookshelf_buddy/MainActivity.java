package com.example.bookshelf_buddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView mail;
    Button logout,btnPrice1,btnPrice2,btnPrice3;

    ImageView cartIcone,backIcon;
    private View cartIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        mail = findViewById(R.id.textView);
        cartIcon = findViewById(R.id.cart);
        btnPrice1 = findViewById(R.id.btnPrice1);
        btnPrice2 = findViewById(R.id.btnPrice2);
        btnPrice3 = findViewById(R.id.btnPrice3);
        user = auth.getCurrentUser();

        // Click listener for the price button of the first item
        btnPrice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current user's unique ID
                String userId = user.getUid();

                // Create a Firebase database reference for the user's cart
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(userId)
                        .child("cart");

                // Create a new cart item
                CartItem item = new CartItem("Book Name: The secret", 20.00);

                // Push the item to Firebase with a unique key
                DatabaseReference newItemRef = cartRef.push();
                newItemRef.setValue(item.toMap());

                // Fetch the current total price from Firebase
                DatabaseReference totalRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(userId)
                        .child("total_price");

                totalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            double currentTotalPrice = dataSnapshot.getValue(Double.class); // Assuming it's stored as a Double
                            double newTotalPrice = currentTotalPrice + item.getPrice();

                            // Update the total price in Firebase
                            totalRef.setValue(newTotalPrice);
                        } else {
                            // If the "total_price" node doesn't exist, create it and set the initial value
                            totalRef.setValue(item.getPrice());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database errors, if any
                    }
                });

                // Show a message indicating that the item has been added to the cart
                Toast.makeText(MainActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for the price button of the second item
        btnPrice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current user's unique ID
                String userId = user.getUid();

                // Create a Firebase database reference for the user's cart
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(userId)
                        .child("cart");

                // Create a new cart item
                CartItem item = new CartItem("Book Name: The miracle morning", 15.00);

                // Push the item to Firebase with a unique key
                DatabaseReference newItemRef = cartRef.push();
                newItemRef.setValue(item.toMap());

                // Fetch the current total price from Firebase
                DatabaseReference totalRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(userId)
                        .child("total_price");

                totalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            double currentTotalPrice = dataSnapshot.getValue(Double.class); // Assuming it's stored as a Double
                            double newTotalPrice = currentTotalPrice + item.getPrice();

                            // Update the total price in Firebase
                            totalRef.setValue(newTotalPrice);
                        } else {
                            // If the "total_price" node doesn't exist, create it and set the initial value
                            totalRef.setValue(item.getPrice());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database errors, if any
                    }
                });

                // Show a message indicating that the item has been added to the cart
                Toast.makeText(MainActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for the price button of the first item
        btnPrice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current user's unique ID
                String userId = user.getUid();

                // Create a Firebase database reference for the user's cart
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(userId)
                        .child("cart");

                // Create a new cart item
                CartItem item = new CartItem("Book Name: Rich Dad Poor Dad", 10.00);

                // Push the item to Firebase with a unique key
                DatabaseReference newItemRef = cartRef.push();
                newItemRef.setValue(item.toMap());

                // Fetch the current total price from Firebase
                DatabaseReference totalRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(userId)
                        .child("total_price");

                totalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            double currentTotalPrice = dataSnapshot.getValue(Double.class); // Assuming it's stored as a Double
                            double newTotalPrice = currentTotalPrice + item.getPrice();

                            // Update the total price in Firebase
                            totalRef.setValue(newTotalPrice);
                        } else {
                            // If the "total_price" node doesn't exist, create it and set the initial value
                            totalRef.setValue(item.getPrice());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database errors, if any
                    }
                });

                // Show a message indicating that the item has been added to the cart
                Toast.makeText(MainActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        if(user == null){
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }
        else{
            mail.setText(user.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the cart activity
                Intent intent = new Intent(getApplicationContext(), Cart.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
