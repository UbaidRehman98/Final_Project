package com.petx.petx.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.petx.petx.R;
import com.petx.petx.databinding.ActivityHomeBinding;
import com.petx.petx.databinding.ActivityMainBinding;
import com.petx.petx.databinding.ActivityMarketPlaseBinding;

public class MarketPlaseActivity extends AppCompatActivity {


    private ActivityMarketPlaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMarketPlaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.adoptAPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketPlaseActivity.this,AdoptAPetActivity.class));
            }
        });

        binding.buyAPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketPlaseActivity.this,BuyAPetsActivity.class));
            }
        });

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketPlaseActivity.this,ProfileActivity.class));
            }
        });

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}