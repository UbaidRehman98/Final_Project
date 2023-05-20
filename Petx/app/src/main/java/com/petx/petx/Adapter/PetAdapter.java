package com.petx.petx.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.petx.petx.Model.Pet;
import com.petx.petx.R;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    private List<Pet> petList;
    private OnItemClickListener onItemClickListener;

    Context context;

    public PetAdapter(List<Pet> petList, OnItemClickListener onItemClickListener,Context context) {
        this.petList = petList;
        this.onItemClickListener = onItemClickListener;
        this.context=context;
    }

    public interface OnItemClickListener {
        void onItemClick(Pet pet);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = petList.get(position);

        // Bind the pet data to the ViewHolder views
        holder.titleTextView.setText(pet.getTitle());
        holder.descriptionTextView.setText(pet.getDescription());
        Glide.with(context).load(pet.getImageURL())
                .placeholder(R.drawable.image_1)
                .into(holder.image);
        // Bind other pet details to respective views
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleTextView;
        private TextView descriptionTextView;

        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            descriptionTextView = itemView.findViewById(R.id.description);
            image=itemView.findViewById(R.id.imageView1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Pet pet = petList.get(position);
                onItemClickListener.onItemClick(pet);
            }
        }
    }
}