package com.petx.petx.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.io.Serializable;

public class PetDetailsActivity extends AppCompatActivity  {


    private TextView titleTextView;
    private TextView descriptionTextView;
    ImageView image;
    Pet pet;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Buy");


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

        // Get references to other TextViews or views

        // Retrieve the pet details from the intent
        Intent intent = getIntent();
        if (intent != null) {
             pet = (Pet) intent.getSerializableExtra("pet");
            if (pet != null) {
                // Use the pet object and its properties as needed
                String title = pet.getTitle();
                String description = pet.getDescription();
                titleTextView.setText(title);
                descriptionTextView.setText(description);
                Glide.with(this).load(pet.getImageURL())
                        .placeholder(R.drawable.image_1)
                        .into(image);
                // ...
            }
        }

        findViewById(R.id.upload_adopt_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = ((EditText) findViewById(R.id.address)).getText().toString();
                String contactDetails = ((EditText) findViewById(R.id.contact_details)).getText().toString();
                Buy buy=new Buy(pet.getId(),pet.getTitle(),pet.getDescription(),pet.getPetType(),pet.getImageURL(),pet.getUserId(),contactDetails,address, "");


                findViewById(R.id.upload_adopt_info).setEnabled(false);

                databaseReference.child(pet.getId()).setValue(buy)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Pet details uploaded successfully
                                finish();
                                Toast.makeText(PetDetailsActivity.this, "Pet details uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to upload pet details
                                findViewById(R.id.upload_adopt_info).setEnabled(true);
                                Toast.makeText(PetDetailsActivity.this, "Failed to upload pet details", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });



    }
}