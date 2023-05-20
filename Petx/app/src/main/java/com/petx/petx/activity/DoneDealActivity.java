package com.petx.petx.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.petx.petx.Model.Buy;
import com.petx.petx.Model.Pet;
import com.petx.petx.R;

public class DoneDealActivity extends AppCompatActivity {


    private TextView titleTextView,product_price;

    ImageView image;
    private TextView descriptionTextView;
    Buy pet;

    Pet pet1;

    String id;

    private DatabaseReference databaseReference_buy,databaseReference_adopt,databaseReference_seals,databaseReference_accessories;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_deal);


        // Initialize the database reference
        databaseReference_buy = FirebaseDatabase.getInstance().getReference("Buy");
        databaseReference_seals = FirebaseDatabase.getInstance().getReference("sales_pets");
        databaseReference_adopt = FirebaseDatabase.getInstance().getReference("adopt_pets");
        databaseReference_accessories = FirebaseDatabase.getInstance().getReference("buy_accessories");


        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get references to the TextViews or views in the layout
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        image=findViewById(R.id.imageView1);
        product_price=findViewById(R.id.product_price);
        // Get references to other TextViews or views

        // Retrieve the pet details from the intent
        Intent intent = getIntent();
        if (intent != null) {
            pet = (Buy) intent.getSerializableExtra("pet");
            pet1 = (Pet) intent.getSerializableExtra("pet1");
            if (pet != null) {
                // Use the pet object and its properties as needed
                id=pet.getId();
                String title = pet.getTitle();
                String description = pet.getDescription();
                titleTextView.setText(title);
                descriptionTextView.setText(description);
                if(!pet.getPrice().isEmpty())
                   product_price.setText("Total Bill: $"+pet.getPrice());
                Glide.with(this).load(pet.getImageURL())
                        .placeholder(R.drawable.image_1)
                        .into(image);

                // ...
            }
            if (pet1 != null) {
                // Use the pet object and its properties as needed
                id=pet1.getId();
                String title = pet1.getTitle();
                String description = pet1.getDescription();
                titleTextView.setText(title);
                descriptionTextView.setText(description);
                Glide.with(this).load(pet1.getImageURL())
                        .placeholder(R.drawable.image_1)
                        .into(image);

                // ...
            }
        }

        findViewById(R.id.canceled).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference_buy.child(id).setValue(null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Pet details uploaded successfully
                                finish();
                                Toast.makeText(DoneDealActivity.this, "Pet details uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to upload pet details
                                Toast.makeText(DoneDealActivity.this, "Failed to upload pet details", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        findViewById(R.id.completed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCompleted();
            }
        });
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCompleted();
            }
        });



    }


    void DeleteCompleted() {
        databaseReference_buy.child(id).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Pet details uploaded successfully
                        finish();
                        Toast.makeText(DoneDealActivity.this, "Pet details uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload pet details
                        Toast.makeText(DoneDealActivity.this, "Failed to upload pet details", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference_seals.child(id).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Pet details uploaded successfully
                        finish();
                        Toast.makeText(DoneDealActivity.this, "Pet details uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload pet details
                        Toast.makeText(DoneDealActivity.this, "Failed to upload pet details", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference_adopt.child(id).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Pet details uploaded successfully
                        finish();
                        Toast.makeText(DoneDealActivity.this, "Pet details uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload pet details
                        Toast.makeText(DoneDealActivity.this, "Failed to upload pet details", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference_accessories.child(id).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Pet details uploaded successfully
                        finish();
                        Toast.makeText(DoneDealActivity.this, "Pet details uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload pet details
                        Toast.makeText(DoneDealActivity.this, "Failed to upload pet details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}