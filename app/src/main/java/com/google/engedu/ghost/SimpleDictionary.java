package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private String TAG = "SimpleDictionary";
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Log.d(TAG, "getAnyWordStartingWith() called.");
        Random mRandom = new Random();
        if(prefix.equals("")){
            Log.d(TAG,"Empty prefix.");
            int mRandomIndex = Math.abs(mRandom.nextInt()) % words.size();
            Log.d(TAG,"Random index "+mRandomIndex+" with word "+words.get(mRandomIndex));
            return words.get(mRandomIndex);
        } else {
            String mReturnWord = null;
            int mLowIndex = 0;
            int mHighIndex = words.size()-1;
            int mMidIndex = (mLowIndex + mHighIndex) / 2;
            while (mHighIndex - mLowIndex > 1){
                Log.d(TAG,"Trying word: "+words.get(mMidIndex));
                if(words.get(mMidIndex).startsWith(prefix)){
                    //prefix is equal to words[mMidIndex]
                    mReturnWord = words.get(mMidIndex);
                    break;
                } else if(prefix.compareTo(words.get(mMidIndex)) > 0){
                    //prefix is greater than words[mMidIndex]
                    mLowIndex = mMidIndex;
                    mMidIndex = (mLowIndex + mHighIndex) / 2;
                } else if(prefix.compareTo(words.get(mMidIndex)) < 0){
                    //prefix is less than words[mMidIndex]
                    mHighIndex = mMidIndex;
                    mMidIndex = (mLowIndex + mHighIndex) / 2;
                }
                Log.d(TAG,"mLowIndex = "+mLowIndex);
                Log.d(TAG,"mMidIndex = "+mMidIndex);
                Log.d(TAG,"mHighIndex = "+mHighIndex);
            }
            return mReturnWord;
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
