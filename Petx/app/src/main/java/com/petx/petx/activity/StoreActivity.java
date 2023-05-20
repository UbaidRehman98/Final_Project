package com.petx.petx.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.petx.petx.R;
import com.petx.petx.databinding.ActivityStoreBinding;
import com.petx.petx.fragment.AccessoriesFragment;
import com.petx.petx.fragment.AdoptPetsFragment;
import com.petx.petx.fragment.LoginFragment;
import com.petx.petx.fragment.OrderRequestFragment;
import com.petx.petx.fragment.SellsPetsFragment;

public class StoreActivity extends AppCompatActivity {




    Fragment fragment;
    private  ActivityStoreBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.adopt.setSelected(true);
        fragment=new AdoptPetsFragment();
        openFragment();

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.adopt.setSelected(true);
                binding.sells.setSelected(false);
                binding.accessories.setSelected(false);
                binding.request.setSelected(false);
                fragment=new AdoptPetsFragment();
                openFragment();
            }
        });

        binding.sells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.adopt.setSelected(false);
                binding.sells.setSelected(true);
                binding.accessories.setSelected(false);
                binding.request.setSelected(false);
                fragment=new SellsPetsFragment();
                openFragment();
            }
        });

        binding.accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.adopt.setSelected(false);
                binding.sells.setSelected(false);
                binding.accessories.setSelected(true);
                binding.request.setSelected(false);
                fragment=new AccessoriesFragment();
                openFragment();
            }
        });
        binding.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.adopt.setSelected(false);
                binding.sells.setSelected(false);
                binding.accessories.setSelected(false);
                binding.request.setSelected(true);
                fragment=new OrderRequestFragment();
                openFragment();
            }
        });


    }

    public void openFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.store_container, fragment).commit();
    }


}