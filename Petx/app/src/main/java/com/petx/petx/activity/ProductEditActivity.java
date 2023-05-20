package com.petx.petx.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.petx.petx.Model.Buy;
import com.petx.petx.Model.Pet;
import com.petx.petx.Model.PetSales;
import com.petx.petx.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProductEditActivity extends AppCompatActivity {



    private EditText titleTextView,product_price;

    ImageView image;
    private EditText descriptionTextView;
    Buy pet;

    Pet pet1;

    String id;

    private RadioGroup petTypeRadioGroup;
    private Uri imageUri=null;
    String img;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    ImageView imageView;

    int updateOption;

    Button update;
    int PICK_IMAGE_REQUEST=101;
    private DatabaseReference databaseReference_buy,databaseReference_adopt,databaseReference_seals,databaseReference_accessories;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        // Initialize the database reference
        databaseReference_seals = FirebaseDatabase.getInstance().getReference("sales_pets");
        databaseReference_adopt = FirebaseDatabase.getInstance().getReference("adopt_pets");
        databaseReference_accessories = FirebaseDatabase.getInstance().getReference("buy_accessories");


        petTypeRadioGroup = findViewById(R.id.pet_type_radio_group);


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
            updateOption = intent.getIntExtra("updateOption",0);
            pet = (Buy) intent.getSerializableExtra("pet");
            pet1 = (Pet) intent.getSerializableExtra("pet1");
            if (pet != null) {
                // Use the pet object and its properties as needed
                id=pet.getId();
                String title = pet.getTitle();
                String description = pet.getDescription();
                titleTextView.setText(title);
                descriptionTextView.setText(description);
                if(!pet.getPrice().isEmpty()) {
                    product_price.setText( pet.getPrice());
                    product_price.setVisibility(View.VISIBLE);
                }
                Glide.with(this).load(pet.getImageURL())
                        .placeholder(R.drawable.image_1)
                        .into(image);
                img= pet.getImageURL();

                switch (pet.getPetType()) {
                    case "Cat":
                        RadioButton cat = findViewById(R.id.cat);
                        cat.setChecked(true);
                        break;
                    case "Dog":
                        RadioButton dog = findViewById(R.id.dog);
                        dog.setChecked(true);
                        break;
                    case "Bird":
                        RadioButton bird = findViewById(R.id.bird);
                        bird.setChecked(true);
                        break;
                }
                // ...
            }
            if (pet1 != null) {
                // Use the pet object and its properties as needed
                id=pet1.getId();
                titleTextView.setText(pet1.getTitle());
                descriptionTextView.setText(pet1.getDescription());
                Glide.with(this).load(pet1.getImageURL())
                        .placeholder(R.drawable.image_1)
                        .into(image);

                img= pet1.getImageURL();

                switch (pet1.getPetType()){
                    case "Cat":
                      RadioButton cat=  findViewById(R.id.cat);
                        cat.setChecked(true);
                        break;
                    case "Dog":
                        RadioButton dog=  findViewById(R.id.dog);
                        dog.setChecked(true);
                        break;
                    case "Bard":
                        RadioButton bird=  findViewById(R.id.bird);
                        bird.setChecked(true);
                        break;
                }

                // ...
            }
        }




        update=findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update();
                update.setEnabled(false);
            }
        });
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();
            }
        });

        imageView=findViewById(R.id.imageView1);
        findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });


    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    void Delete() {
        databaseReference_buy.child(id).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Pet details uploaded successfully
                        finish();
                        Toast.makeText(ProductEditActivity.this, "Deleted The Item", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload pet details
                        Toast.makeText(ProductEditActivity.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference_seals.child(id).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Pet details uploaded successfully
                        finish();
//                        Toast.makeText(ProductEditActivity.this, "Pet details uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload pet details
//                        Toast.makeText(ProductEditActivity.this, "Failed to upload pet details", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference_adopt.child(id).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Pet details uploaded successfully
                        finish();
//                        Toast.makeText(ProductEditActivity.this, "Pet details uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload pet details
//                        Toast.makeText(ProductEditActivity.this, "Failed to upload pet details", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference_accessories.child(id).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Pet details uploaded successfully
                        finish();
//                        Toast.makeText(ProductEditActivity.this, "Pet details uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to upload pet details
//                        Toast.makeText(ProductEditActivity.this, "Failed to upload pet details", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    void Update() {
        String price;
        String title = titleTextView.getText().toString().trim();
        String description = descriptionTextView.getText().toString().trim();
        if(pet!=null) {
             price = product_price.getText().toString().trim();
        }else{
             price="";
        }
        // String petType = getSelectedPetType();
        String petType = null; //= getSelectedPetType();

        int selectedId = petTypeRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton radioButton =findViewById(selectedId);
            petType= radioButton.getText().toString();
        }


        if ( !title.isEmpty() && !description.isEmpty() && petType != null) {
            uploadPet(title, description, petType, price);
        } else {
            Toast.makeText(this, "Please fill in all the fields and select an image", Toast.LENGTH_SHORT).show();
        }


    }

    private void uploadPet(final String title, final String description, final String petType, final String price) {
        // Get the current user ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        final String userId = currentUser.getUid();

        // Create a unique key for the new pet
//        final String petId = mDatabase.child("buy_accessories").push().getKey();

        // Define the storage path for the pet image
        final StorageReference imageRef = mStorage.child("pet_images").child(id + ".jpg");

        // Convert the image URI to a byte array
        if(imageUri!=null) {
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                return;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();


            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Get the download URL of the uploaded image
                        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();

                                    // Create a new pet object with the information
                                    PetSales newPet = new PetSales(id, title, description, petType, downloadUri.toString(), userId, price);
                                    switch (updateOption){
                                        case 1:
                                            databaseReference_seals.child(id).setValue(newPet);
                                            break;
                                        case 2:
                                            Pet uPet = new Pet(id, title, description, petType, downloadUri.toString(), userId);
                                            databaseReference_adopt.child(id).setValue(uPet);
                                            break;

                                        case 3:
                                            databaseReference_accessories.child(id).setValue(newPet);
                                            break;
                                    }


                                    Toast.makeText(ProductEditActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    update.setEnabled(true);
                                    Toast.makeText(ProductEditActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
//
                    }
                }
            });
        }else{
            PetSales newPet = new PetSales(id, title, description, petType, img, userId, price);
            switch (updateOption){
                case 1:
                    databaseReference_seals.child(id).setValue(newPet);
                    break;
                case 2:
                    Pet uPet = new Pet(id, title, description, petType, img, userId);
                    databaseReference_adopt.child(id).setValue(uPet);
                    break;

                case 3:
                    databaseReference_accessories.child(id).setValue(newPet);
                    break;
            }
            finish();
        }
    }
}