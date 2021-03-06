package com.example.chav.poker.adapters;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chav.poker.R;
import com.example.chav.poker.controller.cram.CramCardsViewActivity;

import java.util.List;

import model.CramDeck;

/**
 * Created by Mitakaa on 05-Apr-16.
 */
public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder>{

    private List<CramDeck> decks;

    public DeckAdapter(List<CramDeck> decks) {
        this.decks = decks;
    }


    public static class DeckViewHolder extends RecyclerView.ViewHolder {
        TextView mDeckTitle;

        public DeckViewHolder(View itemView) {
            super(itemView);
            Typeface myTypeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "HelveticaRoman.ttf");
            mDeckTitle = (TextView) itemView.findViewById(R.id.view_holder_deck_text_view);
            mDeckTitle.setTypeface(myTypeface);
        }
    }

    public static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }

    }

    @Override
    public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_holder_deck, parent, false);
        DeckViewHolder dvh = new DeckViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DeckViewHolder holder, final int position) {
        holder.mDeckTitle.setText(decks.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CramCardsViewActivity.class);
                intent.putExtra("deck_id", decks.get(position).getId());
                intent.putExtra("title", decks.get(position).getTitle());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }


}
