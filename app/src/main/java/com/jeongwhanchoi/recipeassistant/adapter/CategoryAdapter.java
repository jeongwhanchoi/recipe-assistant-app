package com.jeongwhanchoi.recipeassistant.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jeongwhanchoi.recipeassistant.AspectRatioImageView;
import com.jeongwhanchoi.recipeassistant.Category;
import com.jeongwhanchoi.recipeassistant.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Category adapter to create a list with Text and Images
 */

public class CategoryAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<Category> categories;
    Context context;
    private AdapterView.OnItemClickListener onItemClickListener;

    public CategoryAdapter(List<Category> categories, AdapterView.OnItemClickListener onItemClickListener, Context context) {
        this.categories = categories;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }


    /**
     * Holds the Category elements so that they don't have to be re-created each time
     */
    public class CategoryViewHolder extends ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView name;
        AspectRatioImageView image;

        CategoryViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            image = (AspectRatioImageView) itemView.findViewById(R.id.recipe_image);
            name = (TextView) itemView.findViewById(R.id.recipe_name);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //passing the clicked position to the parent class
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        }
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_card, viewGroup, false);
        ViewHolder cvh = new CategoryViewHolder(v);
        return cvh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder recipeViewHolder, final int i) {
        //set category name
        ((CategoryViewHolder) recipeViewHolder).name.setText(categories.get(i).name);

        //load images using Picasso
        Picasso.with(context)
                .load(categories.get(i).imageUrl).placeholder(R.drawable.loading)
                .into(((CategoryViewHolder) recipeViewHolder).image);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}