package com.petx.petx.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.petx.petx.R;

public class ProfileActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    String userId = firebaseUser.getUid();

    private EditText nameProfileEditText,emailProfileEditText,passProfileEditText;
    boolean show=true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        nameProfileEditText=findViewById(R.id.name_profile);
        emailProfileEditText=findViewById(R.id.email_profile);
        passProfileEditText=findViewById(R.id.pass_pro);



        findViewById(R.id.store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,StoreActivity.class));
            }
        });


        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView showPass=findViewById(R.id.showpass_profile);
        findViewById(R.id.showpass_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show) {
                    passProfileEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass.setText("Hide");
                    show=false;
                } else {
                    passProfileEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass.setText("Show");
                    show=true;
                }
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    // Set the retrieved data to the respective views
                    nameProfileEditText.setText(name);
                    emailProfileEditText.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });


        findViewById(R.id.save_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameProfileEditText.getText().toString();
                String newEmail = emailProfileEditText.getText().toString();
                String newPassword = passProfileEditText.getText().toString();

                // Update the data in the database
                userRef.child("name").setValue(newName);
                userRef.child("email").setValue(newEmail);

                // Show a success message or perform any additional actions
                Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();


                if (!newPassword.isEmpty())
                    firebaseUser.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, "Password update failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

            }
        });


    }
}