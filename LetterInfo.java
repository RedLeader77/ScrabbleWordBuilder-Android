package com.example.gregappdevelopment.scrabblewordbuilder;

public class LetterInfo
{
    private char letter;
    private int numPieces;
    private int numPiecesInPlay;
    private int score;

    public LetterInfo(char letter, int numPieces, int numPiecesInPlay, int score)
    {
        this.letter = letter;
        this.numPieces = numPieces;
        this.numPiecesInPlay = numPiecesInPlay;
        this.score = score;
    }

    public char getLetter()
    {
        return letter;
    }

    public int getNumPieces()
    {
        return numPieces;
    }

    public int getNumPiecesInPlay()
    {
        return numPiecesInPlay;
    }

    public int getScore()
    {
        return score;
    }

    public void setNumPiecesInPlay(int num)
    {
        this.numPiecesInPlay = num;
    }

    public String toString()
    {
        return letter + "(" + numPieces + "," + score + ")";
    }
}
