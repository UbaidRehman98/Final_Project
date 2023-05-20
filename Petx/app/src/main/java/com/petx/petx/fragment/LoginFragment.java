package com.petx.petx.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.petx.petx.MainActivity;
import com.petx.petx.R;
import com.petx.petx.activity.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        view= inflater.inflate(R.layout.fragment_login, container, false);


        initLayout();

        return view;

    }

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    boolean show=true;
    private void initLayout() {
        mAuth = FirebaseAuth.getInstance();
        view.findViewById(R.id.close).setOnClickListener(v->{
            MainActivity.getInstance().closeFragment();
            //hide keyboard
            View view = getView();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        view.findViewById(R.id.signUp).setOnClickListener(v->{
            MainActivity.getInstance().openSignUpFragment();
        });



        passwordEditText = view.findViewById(R.id.pass_login);
        loginButton = view.findViewById(R.id.login_fragment);
        emailEditText = view.findViewById(R.id.email_login);


        TextView showPass=view.findViewById(R.id.showpass);
        view.findViewById(R.id.showpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass.setText("Hide");
                    show=false;
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass.setText("Show");
                    show=true;
                }
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Toast.makeText(getActivity(),email+" "+password,Toast.LENGTH_SHORT).show();
                loginUser(email, password);
            }
        });


    }


    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful, redirect to main activity
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            // Login failed, display the error message
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getActivity(), "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}