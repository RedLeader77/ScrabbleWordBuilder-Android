package com.example.gregappdevelopment.scrabblewordbuilder;

public class WordInfo
{
    private String word;
    private int wordScore;

    public WordInfo(String word, int wordScore)
    {
        this.word = word;
        this.wordScore = wordScore;
    }

    public String getWord()
    {
        return word;
    }

    public int getWordScore()
    {
        return wordScore;
    }

    public String toString()
    {
        return word + "(" + wordScore + ")";
    }
}
