package com.petx.petx.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.petx.petx.MainActivity;
import com.petx.petx.Model.User;
import com.petx.petx.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        initLayout();
        return view;
    }

    EditText mNameEditText, mEmailEditText, mPasswordEditText;
    private CheckBox mCheckBox;
    private Button mSignupButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    boolean show = true;

    private void initLayout() {


        view.findViewById(R.id.close).setOnClickListener(v -> {
            MainActivity.getInstance().closeFragment();
            //hide keyboard
            View view = getView();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        view.findViewById(R.id.logIn).setOnClickListener(v -> {
            MainActivity.getInstance().openLoginFragment();
        });


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mNameEditText = view.findViewById(R.id.name);
        mEmailEditText = view.findViewById(R.id.email);
        mPasswordEditText = view.findViewById(R.id.pass);
        mCheckBox = view.findViewById(R.id.checkBox);
        mSignupButton = view.findViewById(R.id.singUp_user);


        TextView showPass = view.findViewById(R.id.showpass);
        view.findViewById(R.id.showpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show) {
                    mPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass.setText("Hide");
                    show = false;
                } else {
                    mPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass.setText("Show");
                    show = true;
                }
            }
        });


        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEditText.getText().toString().trim();
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                boolean isSubscribed = mCheckBox.isChecked();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isValid = isValidEmail(email);
                if (isValid) {
                    // Email is valid, perform desired actions

                } else {
                    // Email is invalid, show an error message or handle the invalid case
                    Toast.makeText(getActivity(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            // Authentication successful
                            // Do something
                        })
                        .addOnFailureListener(e -> {
                            // Authentication failed
                            String errorMessage = "Authentication failed: " + e.getLocalizedMessage();
                            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                        });


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userId = user.getUid();

                                    // Save user information to database
                                    User newUser = new User(userId, name, email, isSubscribed);
                                    mDatabase.child(userId).setValue(newUser);

                                    // Redirect user to main activity
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    // Registration failed, display the error message
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(getActivity(), "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

            }
        });


    }

    public boolean isValidEmail(String email) {
        // Define the regular expression pattern for email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Compare the input email with the pattern using regular expressions
        return email.matches(emailPattern);
    }


}