package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;


public class TrieNode {
    private String TAG = "TrieNode";
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        // If current node does not have a child
        // corresponding to s[0], then add it.
        // Once it exists, change the current node
        // and proceed with the next character in s.
        TrieNode currentNode = this;
        for(int i = 0; i < s.length(); i++){
            if(!currentNode.children.containsKey(String.valueOf(s.charAt(i)))){
                currentNode.children.put(String.valueOf(s.charAt(i)), new TrieNode());
            }
            currentNode = currentNode.children.get(String.valueOf(s.charAt(i)));
        }
        currentNode.isWord = true;
    }

    public boolean isWord(String s) {
        // Go down the trie. If any character does
        // not exist, return false. At the end,
        // check if isWord is true.
        TrieNode currentNode = this;
        for(int i = 0; i < s.length(); i++){
            if(!currentNode.children.containsKey(String.valueOf(s.charAt(i)))){
                return false;
            }
            currentNode = currentNode.children.get(String.valueOf(s.charAt(i)));
        }
        return currentNode.isWord;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode currentNode = this;
        String newString = "";
        for(int i = 0; i < s.length(); i++){
            if(!currentNode.children.containsKey(String.valueOf(s.charAt(i)))){
                return null;
            }
            newString += s.charAt(i);
            currentNode = currentNode.children.get(String.valueOf(s.charAt(i)));
        }

        if(currentNode.children.size() == 0){
            return null;
        }
        // Try to find a valid child char by cycling through
        // the alphabet. If found, append to newString and
        // change the currentNode. Continue until isWord is true
        do {
            for (char i = 'a'; i <= 'z'; i++) {
                if (currentNode.children.containsKey(String.valueOf(i))) {
                    newString += i;
                    currentNode = currentNode.children.get(String.valueOf(i));
                    break;
                }
            }
        } while (!currentNode.isWord);

        Log.d(TAG, "Found word: " + newString);

        return newString;
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
