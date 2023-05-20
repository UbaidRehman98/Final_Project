package com.petx.petx.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petx.petx.Adapter.PetAdapter;
import com.petx.petx.Adapter.PetBuyAdapter;
import com.petx.petx.Model.Buy;
import com.petx.petx.Model.Pet;
import com.petx.petx.R;

import java.util.ArrayList;
import java.util.List;

public class BuyAPetsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_apets);



        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyAPetsActivity.this,ProfileActivity.class));
            }
        });

        // Assuming you have a RecyclerView with the id "recyclerView" in your layout file

        RecyclerView recyclerView = findViewById(R.id.recycler_view_adoption_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

// Create a list of Pet objects
        List<Buy> petList = new ArrayList<>();

// Create the item click listener
        PetBuyAdapter.OnItemClickListener itemClickListener = new PetBuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Buy pet) {
                // Handle the item click event, e.g., start a new activity and pass pet details
                Intent intent = new Intent(BuyAPetsActivity.this, ProductBuyActivity.class);
                intent.putExtra("pet", pet);
                startActivity(intent);
                Log.d("Pet=", pet.getTitle());
            }
        };

// Create the PetAdapter and set the item click listener
        PetBuyAdapter adapter = new PetBuyAdapter(petList, itemClickListener, getApplicationContext());
        recyclerView.setAdapter(adapter);

// Retrieve pet data from Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sales_pets");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                petList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Buy pet = snapshot.getValue(Buy.class);
                    petList.add(pet);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });


    }

}
