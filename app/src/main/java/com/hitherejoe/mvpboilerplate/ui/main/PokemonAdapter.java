package com.hitherejoe.mvpboilerplate.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hitherejoe.mvpboilerplate.R;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<String> mPokemon;
    private ClickListener mClickListener;

    @Inject
    public PokemonAdapter() {
        mPokemon = Collections.emptyList();
    }

    public void setPokemon(List<String> pokemon) {
        mPokemon = pokemon;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        String pokemon = mPokemon.get(position);
        holder.mPokemon = pokemon;
        holder.nameText.setText(pokemon.substring(0, 1).toUpperCase() + pokemon.substring(1));
    }

    @Override
    public int getItemCount() {
        return mPokemon.size();
    }

    class PokemonViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_name) TextView nameText;

        public String mPokemon;

        public PokemonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) mClickListener.onPokemonClick(mPokemon);
                }
            });
        }
    }

    public interface ClickListener {
        void onPokemonClick(String pokemon);
    }

}
