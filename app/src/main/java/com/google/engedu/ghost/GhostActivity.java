package com.google.engedu.ghost;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private final String TAG = "GhostActivity";
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private FastDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    private String mWordFragment;
    private TextView mGameStatus;
    private TextView mGhostText;
    private TextView mValidWordText;
    private Button mChallengeButton;
    private Button mRestartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        try {
            dictionary = new FastDictionary(getAssets().open("words.txt"));
        } catch (Exception e){
            Log.d(TAG,"Exception thrown: "+e);
        }
        onStart(null);

        //mGameStatus = (TextView)findViewById(R.id.gameStatus);
        //mGhostText = (TextView)findViewById(R.id.ghostText);

        mChallengeButton = (Button)findViewById(R.id.challenge_button);
        mChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWordFragment.length() >= 4 && dictionary.isWord(mWordFragment)){
                    mGameStatus.setText("You won!");
                } else if(dictionary.getAnyWordStartingWith(mWordFragment) != null){
                    String mPossibleLongerWord = dictionary.getAnyWordStartingWith(mWordFragment);
                    mGameStatus.setText("You lose!");
                    mValidWordText.setText("Possible word: "+mPossibleLongerWord);
                    //mWordFragment = mPossibleLongerWord.substring(0,mWordFragment.length()+1);
                    //mGhostText.setText(mWordFragment);
                } else {
                    mGameStatus.setText("You won!");
                }
            }
        });

        mRestartButton = (Button) findViewById(R.id.restart_button);
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart(view);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        Log.d(TAG,"computerTurn() called.");
        //TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        if(dictionary.isWord(mWordFragment)){
            mValidWordText.setText("This is a valid word.");
            Log.d(TAG,"is a valid word.");
        }
        if(mWordFragment.length() >= 4 && dictionary.isWord(mWordFragment)){
            mGameStatus.setText("Computer won!");
            Log.d(TAG, "length>=4 and isWord, computer won.");
            return;
        }
        else if(dictionary.getAnyWordStartingWith(mWordFragment) != null) {
            Log.d(TAG, "possible longer word found.");
            String mPossibleLongerWord = dictionary.getAnyWordStartingWith(mWordFragment);
            mWordFragment += mPossibleLongerWord.charAt(mWordFragment.length());
            mGhostText.setText(mWordFragment);
        } else{
            Log.d(TAG,"no possible longer word - computer won.");
            mGameStatus.setText("No longer word exists - Computer won!");
        }
        Log.d(TAG,"end of computerTurn()");

        if(dictionary.isWord(mWordFragment)){
            mValidWordText.setText("This is a valid word. - You won!");
            Log.d(TAG,"is a valid word.");
        }

        userTurn = true;
        mGameStatus.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        mWordFragment = "";

        mGhostText = (TextView) findViewById(R.id.ghostText);
        mGhostText.setText(mWordFragment);

        mValidWordText = (TextView)findViewById(R.id.valid_word_text);
        mValidWordText.setText("No word yet!");

        mGameStatus = (TextView) findViewById(R.id.gameStatus);

        if (userTurn) {
            mGameStatus.setText(USER_TURN);
        } else {
            mGameStatus.setText(COMPUTER_TURN);
            computerTurn();
        }

        return true;
    }

    @Override
    public boolean onKeyUp(int mKeyCode, KeyEvent mEvent){
        Log.d(TAG,"Button pressed.");
        Character mUnicodeChar = Character.valueOf( (char)mEvent.getUnicodeChar() );
        if( Character.isLetter( (char)mEvent.getUnicodeChar() ) ){
            mWordFragment += (char)mEvent.getUnicodeChar();
            mGhostText.setText(mWordFragment);
            if( dictionary.isWord(mWordFragment) ){
                mGameStatus.setText("This is a valid word.");
            } else {
                mGameStatus.setText("This is not a valid word.");
            }
            mGameStatus.setText(COMPUTER_TURN);
            computerTurn();
        }
        return super.onKeyUp(mKeyCode, mEvent);
    }
}
