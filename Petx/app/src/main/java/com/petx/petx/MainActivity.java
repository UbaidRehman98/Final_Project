package com.petx.petx;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.petx.petx.databinding.ActivityMainBinding;
import com.petx.petx.fragment.LoginFragment;
import com.petx.petx.fragment.SignupFragment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {


    public static MainActivity instance;
    public static  MainActivity getInstance(){
        return instance;
    }

    private ActivityMainBinding binding;

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        instance=this;


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openLoginFragment();
            }
        });


        binding.singup.setOnClickListener(v->{
            openSignUpFragment();
        });


    }


   public void openLoginFragment(){
        fragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment).commit();
        binding.layoutContainer.setVisibility(View.VISIBLE);
    }

   public void openSignUpFragment(){
        fragment = new SignupFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment).commit();
        binding.layoutContainer.setVisibility(View.VISIBLE);
    }

    public void closeFragment(){
        binding.layoutContainer.setVisibility(View.GONE);
    }


}