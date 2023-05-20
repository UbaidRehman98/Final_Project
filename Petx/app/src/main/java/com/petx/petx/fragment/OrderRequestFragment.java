package com.petx.petx.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.petx.petx.Adapter.PetAdapter;
import com.petx.petx.Adapter.PetOrderRequestAdapter;
import com.petx.petx.Model.Buy;
import com.petx.petx.Model.Pet;
import com.petx.petx.R;
import com.petx.petx.activity.BuyAccessoriesActivity;
import com.petx.petx.activity.DoneDealActivity;
import com.petx.petx.activity.ProductBuyActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderRequestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OederRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderRequestFragment newInstance(String param1, String param2) {
        OrderRequestFragment fragment = new OrderRequestFragment();
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
        view= inflater.inflate(R.layout.fragment_oeder_request, container, false);
        initLayout();
        return view;
    }


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private void initLayout() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();





        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_adopt_pets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

// Create a list of Pet objects
        List<Buy> petList = new ArrayList<>();

// Create the item click listener
        PetOrderRequestAdapter.OnItemClickListener itemClickListener = new PetOrderRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Buy pet) {
                // Handle the item click event, e.g., start a new activity and pass pet details
                Intent intent = new Intent(getActivity(), DoneDealActivity.class);
                intent.putExtra("pet", pet);
                startActivity(intent);
                Log.d("Pet=",pet.getTitle());
            }
        };

// Create the PetAdapter and set the item click listener
        PetOrderRequestAdapter adapter = new PetOrderRequestAdapter(petList, itemClickListener,getActivity());
        recyclerView.setAdapter(adapter);

// Retrieve pet data from Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Buy");
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
}