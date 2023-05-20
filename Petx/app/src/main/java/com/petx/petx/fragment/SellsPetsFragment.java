package com.petx.petx.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.petx.petx.Adapter.PetAdapter;
import com.petx.petx.Adapter.PetBuyAdapter;
import com.petx.petx.Model.Buy;
import com.petx.petx.Model.Pet;
import com.petx.petx.Model.PetSales;
import com.petx.petx.R;
import com.petx.petx.activity.DoneDealActivity;
import com.petx.petx.activity.ProductEditActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellsPetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellsPetsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellsPetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellsPetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellsPetsFragment newInstance(String param1, String param2) {
        SellsPetsFragment fragment = new SellsPetsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_sells_pets, container, false);
        initLayout();
        return view;
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private Uri imageUri;
    int PICK_IMAGE_REQUEST=101;
    private void initLayout() {


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();


        view.findViewById(R.id.floating_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petDetailsDialog();
            }
        });



        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_adopt_pets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

// Create a list of Pet objects
        List<Buy> petList = new ArrayList<>();

// Create the item click listener
        PetBuyAdapter.OnItemClickListener itemClickListener = new PetBuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Buy pet) {
                // Handle the item click event, e.g., start a new activity and pass pet details
                Intent intent = new Intent(getActivity(), ProductEditActivity.class);
                intent.putExtra("pet", pet);
                intent.putExtra("updateOption",1);
                startActivity(intent);
                Log.d("Pet=",pet.getTitle());
                Log.d("Pet=",pet.getTitle());
            }
        };

// Create the PetAdapter and set the item click listener
        PetBuyAdapter adapter = new PetBuyAdapter(petList, itemClickListener,getActivity());
        recyclerView.setAdapter(adapter);

// Retrieve pet data from Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sales_pets");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                petList.clear();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Buy pet = snapshot.getValue(Buy.class);
                    if(currentUser.getUid().equals(pet.getUserId()))
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



    private EditText itemTitleEditText;
    private EditText itemDescriptionEditText;
    private EditText priceEditText;
    private ImageView imageView;
    private RadioGroup petTypeRadioGroup;
    private Button uploadPetButton;
    @SuppressLint("MissingInflatedId")
    public void petDetailsDialog() {
        // Inflate the custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_seals_pets_add, null);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Get the dialog views

        itemTitleEditText =dialogView.findViewById(R.id.item_title);
        itemDescriptionEditText = dialogView.findViewById(R.id.item_description);
        imageView = dialogView.findViewById(R.id.imageView1);
        priceEditText = dialogView.findViewById(R.id.item_price);
        petTypeRadioGroup = dialogView.findViewById(R.id.pet_type_radio_group);
        uploadPetButton = dialogView.findViewById(R.id.upload_pt);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        uploadPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = itemTitleEditText.getText().toString().trim();
                String description = itemDescriptionEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();
                // String petType = getSelectedPetType();
                String petType = null; //= getSelectedPetType();

                int selectedId = petTypeRadioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton radioButton =dialogView.findViewById(selectedId);
                    petType= radioButton.getText().toString();
                }


                if (imageUri != null && !title.isEmpty() && !description.isEmpty() && petType != null) {
                    uploadPet(title, description, petType, price);
                } else {
                    Toast.makeText(getActivity(), "Please fill in all the fields and select an image", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private void uploadPet(final String title, final String description, final String petType, final String price) {
        // Get the current user ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        final String userId = currentUser.getUid();

        // Create a unique key for the new pet
        final String petId = mDatabase.child("sales_pets").push().getKey();

        // Define the storage path for the pet image
        final StorageReference imageRef = mStorage.child("pet_images").child(petId + ".jpg");

        // Convert the image URI to a byte array
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
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
                                PetSales newPet = new PetSales(petId, title, description, petType, downloadUri.toString(), userId, price);

                                // Save the new pet to the Firebase Realtime Database
                                mDatabase.child("sales_pets").child(petId).setValue(newPet);

                                Toast.makeText(getActivity(), "Pet uploaded successfully", Toast.LENGTH_SHORT).show();
//                                finish();
                            } else {
                                Toast.makeText(getActivity(), "Failed to get image URL", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}