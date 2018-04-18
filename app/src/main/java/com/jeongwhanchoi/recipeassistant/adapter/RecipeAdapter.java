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

import com.jeongwhanchoi.recipeassistant.view.AspectRatioImageView;
import com.jeongwhanchoi.recipeassistant.Configurations;
import com.jeongwhanchoi.recipeassistant.R;
import com.jeongwhanchoi.recipeassistant.Recipe;
import com.mikepenz.iconics.view.IconicsTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

/**
 * Recipe Adapter to show recipe cards in list
 */
public class RecipeAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Recipe> recipes;
    Context context;
    private AdapterView.OnItemClickListener onItemClickListener;

    public RecipeAdapter(List<Recipe> recipes, AdapterView.OnItemClickListener onItemClickListener, Context context) {
        this.recipes = recipes;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public void addItems(List<Recipe> recipes) {
        this.recipes.addAll(recipes);
    }


    /**
     * Holds the recipe screen elements to avoid creating them multiple times
     */
    public class RecipeViewHolder extends ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView name;
        AspectRatioImageView image;
        IconicsTextView stats;

        RecipeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            image = (AspectRatioImageView) itemView.findViewById(R.id.recipe_image);
            name = (TextView) itemView.findViewById(R.id.recipe_name);
            if (Configurations.LIST_MENU_TYPE == Configurations.LIST_2COLUMNS)
                stats = (IconicsTextView) itemView.findViewById(R.id.stats);

            //set image on click listener
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
        return recipes.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;
        if (Configurations.LIST_MENU_TYPE == Configurations.LIST_FULLWIDTH)
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_card, viewGroup, false);
        else if (Configurations.LIST_MENU_TYPE == Configurations.LIST_2COLUMNS)
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_card_2column, viewGroup, false);
        RecyclerView.ViewHolder rvh = new RecipeViewHolder(v);
        return rvh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder recipeViewHolder, final int i) {
        //set recipe name
        ((RecipeViewHolder) recipeViewHolder).name.setText(recipes.get(i).name);

        if (Configurations.LIST_MENU_TYPE == Configurations.LIST_2COLUMNS) {
            ((RecipeViewHolder) recipeViewHolder).image.setBox(true);
        }
        //load recipe image with picasso
        RequestCreator r;
        if (recipes.get(i).imageUrl.length > 0)
            r = Picasso.with(context).load(recipes.get(i).imageUrl[0]).placeholder(R.drawable.loading);
        else
            r = Picasso.with(context).load(R.drawable.loading);

        if (Configurations.LIST_MENU_TYPE == Configurations.LIST_2COLUMNS) {
            r.fit().centerCrop();
        }
        r.into(((RecipeViewHolder) recipeViewHolder).image);

        //set stats
        if (Configurations.LIST_MENU_TYPE == Configurations.LIST_2COLUMNS) {
            ((RecipeViewHolder) recipeViewHolder).stats.setText("{faw-eye} " + recipes.get(i).viewed + "  {faw-star} " + recipes.get(i).favorited);
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}