package com.example.chav.poker.controller;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.chav.poker.R;
import com.example.chav.poker.managers.CramCardsManager;

import java.util.ArrayList;
import java.util.Random;

import model.CramCard;

public class CramModeActivity extends AppCompatActivity implements CramModeResultFragment.CramModeMessageCallback {

    private FrameLayout mCardLayout;
    private Button mCorrectButton;
    private Button mWrongButton;
    private Random mRandomGenerator = new Random();
    private CramCard mSelectedCard;
    private ArrayList<CramCard> mCards = new ArrayList<>();
    private ArrayList<CramCard> mUsedCards = new ArrayList<>();
    private boolean mPressed = false;
    private CramCardFrontEndFragment mBackFragment;
    private CramCardFrontEndFragment mFrontFragment;
    private FragmentManager manager;
    private int mCorrectAnswers;
    private int mAllCards;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cram_mode);

        manager = getFragmentManager();

        long deckId = getIntent().getExtras().getLong("deck_id");
        mCards.addAll(CramCardsManager.getInstance(this).getDeckCards(deckId));
//        CramCard card1 = new CramCard("Who are u?", "Player.");
//        CramCard card2 = new CramCard("Are u ok?", "Im ok.");
//        CramCard card3 = new CramCard("Nice a?", "nice.");
//        CramCard card4 = new CramCard("Fuck u?", "No no.");
//        mCards.add(card1);
//        mCards.add(card2);
//        mCards.add(card3);
//        mCards.add(card4);


        mCardLayout = (FrameLayout) findViewById(R.id.cram_mode_frame_layout);
        mCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });

        mCorrectButton = (Button) findViewById(R.id.cram_mode_button_correct);
        mCorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCards.size() == 0) {
                    mCorrectAnswers++;
                    startResultsFragment();
                } else {
                    mPressed = false;
                    mCorrectAnswers++;
                    cleanOldFragments();
                    int nextCard = mRandomGenerator.nextInt(mCards.size());
                    mSelectedCard = mCards.remove(nextCard);
                    mUsedCards.add(mSelectedCard);
                    addFrontFragment();

                }
            }
        });
        mWrongButton = (Button) findViewById(R.id.cram_mode_button_wrong);
        mWrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCards.size() == 0) {
                    startResultsFragment();
                } else {
                    mPressed = false;
                    cleanOldFragments();
                    int nextCard = mRandomGenerator.nextInt(mCards.size());
                    mSelectedCard = mCards.remove(nextCard);
                    mUsedCards.add(mSelectedCard);
                    addFrontFragment();
                }
            }
        });

        prepareNewGame();



    }

    private void prepareNewGame() {
        if(mUsedCards.size() != 0){
            mCards.addAll(mUsedCards);
            mUsedCards.clear();
        }
        int nextCard = mRandomGenerator.nextInt(mCards.size());
        mAllCards = mCards.size();
        mSelectedCard = mCards.remove(nextCard);
        mUsedCards.add(mSelectedCard);
        mCorrectAnswers = 0;
        mBackFragment = new CramCardFrontEndFragment();
        addFrontFragment();
    }


    private void cleanOldFragments(){
        manager .beginTransaction()
                .remove(mFrontFragment)
                .commit();
        manager .beginTransaction()
                .remove(mBackFragment)
                .commit();
    }

    private void addFrontFragment(){
        mFrontFragment = new CramCardFrontEndFragment();
        Bundle bundle = new Bundle();
        bundle.putString("textOfCard", mSelectedCard.getQuestion());
        mFrontFragment.setArguments(bundle);

        manager
                .beginTransaction()
                .add(R.id.cram_mode_frame_layout, mFrontFragment)
                .commit();

    }


    private void flipCard() {

        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.
        if(!mPressed) {
            mPressed = true;
            mBackFragment = new CramCardFrontEndFragment();
            Bundle bundle = new Bundle();
            bundle.putString("textOfCard", mSelectedCard.getAnswer());
            mBackFragment.setArguments(bundle);

            manager
                    .beginTransaction()

                            // Replace the default fragment animations with animator resources
                            // representing rotations when switching to the back of the card, as
                            // well as animator resources representing rotations when flipping
                            // back to the front (e.g. when the system Back button is pressed).
                    .setCustomAnimations(
                            R.anim.fragment_card_flip_rigth_in,
                            R.anim.fragment_card_flip_rigth_out,
                            R.anim.fragment_card_flip_left_in,
                            R.anim.fragment_card_flip_left_out)

                            // Replace any fragments currently in the container view with a
                            // fragment representing the next page (indicated by the
                            // just-incremented currentPage variable).
                    .replace(R.id.cram_mode_frame_layout, mBackFragment)

                            // Add this transaction to the back stack, allowing users to press
                            // Back to get to the front of the card.
                    .addToBackStack(null)

                            // Commit the transaction.
                    .commit();
        }
        else {
            mPressed = false;
            mFrontFragment = new CramCardFrontEndFragment();
            Bundle bundle = new Bundle();
            bundle.putString("textOfCard", mSelectedCard.getQuestion());
            mFrontFragment.setArguments(bundle);
            manager
                    .beginTransaction()

                            // Replace the default fragment animations with animator resources
                            // representing rotations when switching to the back of the card, as
                            // well as animator resources representing rotations when flipping
                            // back to the front (e.g. when the system Back button is pressed).
                    .setCustomAnimations(
                            R.anim.fragment_card_flip_left_in,
                            R.anim.fragment_card_flip_left_out,
                            R.anim.fragment_card_flip_rigth_in,
                            R.anim.fragment_card_flip_rigth_out)

                            // Replace any fragments currently in the container view with a
                            // fragment representing the next page (indicated by the
                            // just-incremented currentPage variable).
                    .replace(R.id.cram_mode_frame_layout, mFrontFragment)

                            // Add this transaction to the back stack, allowing users to press
                            // Back to get to the front of the card.
                    .addToBackStack(null)

                            // Commit the transaction.
                    .commit();
        }
    }

    private void startResultsFragment(){
        CramModeResultFragment cramModeResultFragment = new CramModeResultFragment();
        Bundle args = new Bundle();
        args.putString("title", "Results");
        args.putString("message", "Correct: " + mCorrectAnswers);
        args.putString("score", "Total: "  + mAllCards);
        cramModeResultFragment.setArguments(args);
        cramModeResultFragment.show(manager, "tag");
    }


    @Override
    public void onPrepareNewGame() {
        prepareNewGame();
    }

    @Override
    public void onCancelCramMode() {
        finish();
    }
}
