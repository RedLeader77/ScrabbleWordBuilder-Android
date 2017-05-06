package com.example.gregappdevelopment.scrabblewordbuilder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
//import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    ArrayList<LetterInfo> letterInfoArray = new ArrayList<LetterInfo>();
    ArrayList<WordInfo> wordInfoArray = new ArrayList<WordInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jsonString = readLettersJSONFile();
        processJSON(jsonString);
        readDictionaryFile();

        // Fill in letter tracker edit text
        EditText letterTrackerEditText = (EditText)findViewById(R.id.lettertracker_edittext);
        letterTrackerEditText.setText(getLetterInfoArrayString());

        // Add listener and call updateLetterTracking() method to rackEditText after text entered
        EditText rackEditText = (EditText)findViewById(R.id.rack_edittext);
        rackEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                //Log.d("INFO", s.toString());
                updateLetterTracking();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Add listener and call updateLetterTracking() method to wordEditText after text entered
        EditText wordEditText = (EditText)findViewById(R.id.word_edittext);
        wordEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                //Log.d("INFO", s.toString());
                updateLetterTracking();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }

    ////////////////////////////////////////////////////////////////////////////////
    // Name: processJSON()
    // Arguments:
    //     json: text in JSON format
    // Purpose: Takes in a JSON formatted String and stores it in a JSONArray
    //     object. Then iterates through the array and stores the letter elements
    //     (letter, count, score) into the letterInfoArray. Throws a
    //     RunTimeException if any issues occur.
    // Return Value: void
    // Dependencies: N?A
    ////////////////////////////////////////////////////////////////////////////////
    void processJSON(String json)
    {
        try
        {
            JSONObject objMain = new JSONObject(json);
            JSONArray jsonArray = objMain.getJSONArray("letters");

            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String letter = jsonObj.getString("letter");
                int count = jsonObj.getInt("count");
                int score = jsonObj.getInt("score");
                letterInfoArray.add(new LetterInfo(letter.charAt(0), count, 0, score));
            }
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////
    // Name: readLettersJSONFile()
    // Arguments: none
    // Purpose: Reads the letter info contained in the letters.json file from the
    //     app's assets and stores the data in the letterInfoArray, which is an
    //     array of LetterInfo objects. Throws a RunTimeException if any issues
    //     occur.
    // Return Value: String
    // Dependencies: Depends on letters.json existing in app assets.
    ////////////////////////////////////////////////////////////////////////////////
    String readLettersJSONFile()
    {
        String jsonText = "";

        try
        {
            InputStream is = getAssets().open("letters.json");
            int size = is.available();

            // Read the entire file contents into a local byte buffer
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string
            jsonText = new String(buffer);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e); // Should never happen
        }

        return jsonText;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Name: readDictionaryFile()
    // Arguments: none
    // Purpose: Reads the words contained in the dictionary file from the app's
    //     assets stores them in the wordInfoArray, which is an array of WordInfo
    //     objects. The wordScore for each word is also calculated and stored in
    //     the wordInfoArray. Throws a RunTimeException if any issues occur.
    // Return Value: void
    // Dependencies: Depends on dictionary.txt existing in app assets.
    ////////////////////////////////////////////////////////////////////////////////
    void readDictionaryFile()
    {
        try
        {
            InputStream is = getAssets().open("dictionary.txt");

            int size = is.available();

            // Read the entire file contents into a local byte buffer
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string
            String text = new String(buffer);
            String[] lines = text.split("\n");

            // Iterate through lines calculating wordScore and
            // add word and wordScore to wordInfoArray
            for(int i = 0; i < lines.length; i++)
            {
                String word = lines[i];
                int wordScore = 0;

                // Calculate wordScore for word
                for(int j = 0; j < word.length(); j++)
                {
                    for(int k = 0; k < letterInfoArray.size(); k++)
                    {
                        if(word.charAt(j) == letterInfoArray.get(k).getLetter())
                        {
                            wordScore += letterInfoArray.get(k).getScore();
                            break;
                        }
                    }
                }

                wordInfoArray.add(new WordInfo(word, wordScore));
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e); // Should never happen
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Name: getLetterInfoArrayString()
    // Arguments: none
    // Purpose: Creates and returns a string representation of the data contained
    //     in letterInfoArray
    // Return Value: String
    // Dependencies: Assumes the letterInfoArray has been populated
    ////////////////////////////////////////////////////////////////////////////////
    String getLetterInfoArrayString()
    {
        String returnString = "";

        for(int i = 0; i < letterInfoArray.size(); i++)
        {
            returnString += letterInfoArray.get(i).getLetter() + "(" +
                    (letterInfoArray.get(i).getNumPieces() - letterInfoArray.get(i).getNumPiecesInPlay()) +
                    "," + letterInfoArray.get(i).getScore() + ") ";
        }

        return returnString;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Name: updateLetterTracking()
    // Arguments: none
    // Purpose: Called on input made to both the 'Rack' and 'Word' text areas to
    //     dynamically update the number of letter pieces availble to the user
    //     and displayed in letterTrackerTextArea. Also used in
    //     resetButtonClicked().
    // Return Value: void
    // Dependencies: Assumes letterInfoArray has been populated
    ////////////////////////////////////////////////////////////////////////////////
    void updateLetterTracking()
    {
        EditText rackEditText = (EditText)findViewById(R.id.rack_edittext);
        EditText wordEditText = (EditText)findViewById(R.id.word_edittext);

        // Update numPiecesAvail in letterInfoArray
        String currentRackString = rackEditText.getText().toString();
        String currentWordString = wordEditText.getText().toString();

        // Reset all numPiecesInPlay values to 0
        for(int i = 0; i < letterInfoArray.size(); i++)
        {
            letterInfoArray.get(i).setNumPiecesInPlay(0);
        }

        // Iterate through currentRackString and update numPiecesInPlay
        for(int i = 0; i < currentRackString.length(); i++)
        {
            // Find correct index in letterInfoArray for character at
            // currentRackString[i] and update numPiecesInPlay
            for(int j = 0; j < letterInfoArray.size(); j++)
            {
                if(currentRackString.charAt(i) == letterInfoArray.get(j).getLetter())
                {
                    int num = letterInfoArray.get(j).getNumPiecesInPlay();
                    letterInfoArray.get(j).setNumPiecesInPlay(num + 1);
                    break;
                }
            }
        }

        // Iterate through currentWordString and update numPiecesInPlay
        for(int i = 0; i < currentWordString.length(); i++)
        {
            // Find correct index in letterInfoArray for character at
            // currentWordString[i] and update numPiecesInPlay
            for(int j = 0; j < letterInfoArray.size(); j++)
            {
                if(currentWordString.charAt(i) == letterInfoArray.get(j).getLetter())
                {
                    int num = letterInfoArray.get(j).getNumPiecesInPlay();
                    letterInfoArray.get(j).setNumPiecesInPlay(num + 1);
                    break;
                }
            }
        }

        // Update data in letterTrackerEditText
        EditText letterTrackerEditText = (EditText)findViewById(R.id.lettertracker_edittext);
        letterTrackerEditText.setText(getLetterInfoArrayString());
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Name: processButtonClicked()
    // Arguments: view
    // Purpose: Checks for input errors. If any found, updates the outputTextArea
    //     with the error message and returns. If no errors are found, a call to
    //     findHighestScoringWord() is made.
    // Return Value: void
    // Dependencies: Makes call to findHighestScoringWord()
    ////////////////////////////////////////////////////////////////////////////////
    public void processButtonClicked(View view)
    {
        EditText rackEditText = (EditText)findViewById(R.id.rack_edittext);
        EditText wordEditText = (EditText)findViewById(R.id.word_edittext);
        EditText outputEditText = (EditText)findViewById(R.id.output_edittext);
        EditText wordScoreEditText = (EditText)findViewById(R.id.wordscore_edittext);

        outputEditText.setText("");
        wordScoreEditText.setText("");

        // Check rack rules
        String rackString = rackEditText.getText().toString();
        if(rackString.length() < 1 || rackString.length() > 7)
        {
            String outputMsg = "Invalid number of letters on rack. Must be between 1-7";
            outputEditText.setText(outputMsg);
            return;
        }

        // If optional word provided by user, check word rules
        String wordString = wordEditText.getText().toString();
        if(wordString.length() > 0) // then we have something to validate
        {
            if(wordString.length() < 2 || wordString.length() > 15)
            {
                String outputMsg = "Invalid number of letters in word. Must be between 2-15";
                outputEditText.setText(outputMsg);
                return;
            }

            // Check that word exists in the supplied dictionary (aka wordInfoArray)
            boolean wordExists = false;
            for(int i = 0; i < wordInfoArray.size(); i++)
            {
                if(wordString.equals(wordInfoArray.get(i).getWord()))
                {
                    wordExists = true;
                    break;
                }
            }

            if(!wordExists)
            {
                outputEditText.setText("The word provided (" + wordString +
                        ") does not exist in dictionary");
                return;
            }
        }

        // Make sure the user hasn't entered too many of any letters
        String errorLetters = "";
        for(int i = 0; i < letterInfoArray.size(); i++)
        {
            if(letterInfoArray.get(i).getNumPiecesInPlay() > letterInfoArray.get(i).getNumPieces())
            {
                errorLetters += letterInfoArray.get(i).getLetter() + " ";
            }
        }

        if(errorLetters.length() > 0)
        {
            String outputMsg = "Error: letters with too many entries: " + errorLetters;
            outputEditText.setText(outputMsg);
            return;
        }

        findHighestScoringWord();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Name: findHighestScoringWord()
    // Arguments: none
    // Purpose: Looks for valid scoring words in the wordInfoArray and keeps track
    //     of the maxScoreIndex as maximum scoring words are found. See comments
    //     below in method for the word building scenarios that are handled. The
    //     'Output' and 'Word Score' text areas are updated with the results, the
    //     word and wordScore stored at wordInfoArray[maxScoreIndex].
    // Return Value: void
    // Dependencies: Assumes wordInfoArray has been populated. Makes calls to
    //     isScoringWord()
    ////////////////////////////////////////////////////////////////////////////////
    void findHighestScoringWord()
    {
        EditText rackEditText = (EditText)findViewById(R.id.rack_edittext);
        EditText wordEditText = (EditText)findViewById(R.id.word_edittext);
        EditText outputEditText = (EditText)findViewById(R.id.output_edittext);
        EditText wordScoreEditText = (EditText)findViewById(R.id.wordscore_edittext);
        int maxScoreIndex = 0;

        // Example: AIDOORW -> DRAW (8 points, 1st word alphabetically)

        // Case where no word is supplied: Find all words in wordInfoArray that can
        // be made from letters in rack. Keep track of index of max scoring word found.
        if(wordEditText.getText().toString().length() == 0)
        {
            for(int i = 0; i < wordInfoArray.size(); i++)
            {
                if(isScoringWord(wordInfoArray.get(i).getWord(), rackEditText.getText().toString()))
                {
                    //Log.d("INFO", "Scoring word: " + wordInfoArray.get(i).getWord());

                    // Keep track of index of max scoring word found
                    if(wordInfoArray.get(i).getWordScore() > wordInfoArray.get(maxScoreIndex).getWordScore())
                    {
                        maxScoreIndex = i;
                    }
                }
            }
        }

        // Case where word is supplied by user
        if(wordEditText.getText().toString().length() > 0)
        {
            String myWord = wordEditText.getText().toString();

            // Building off of word horizontally. Scoring word must include user supplied word.
            for(int i = 0; i < wordInfoArray.size(); i++)
            {
                if(wordInfoArray.get(i).getWord().contains(myWord) &&
                        isScoringWord(wordInfoArray.get(i).getWord(), rackEditText.getText().toString() + myWord))
                {
                    //Log.d("INFO", "Scoring word: " + wordInfoArray.get(i).getWord());

                    // Keep track of index of max scoring word found
                    if(wordInfoArray.get(i).getWordScore() > wordInfoArray.get(maxScoreIndex).getWordScore())
                    {
                        maxScoreIndex = i;
                    }
                }
            }

            // Building off of user supplied word vertically checking each letter
            for(int i = 0; i < myWord.length(); i++)
            {
                String myLetter = String.valueOf(myWord.charAt(i));
                for(int j = 0; j < wordInfoArray.size(); j++)
                {
                    if(wordInfoArray.get(j).getWord().contains(myLetter) &&
                            isScoringWord(wordInfoArray.get(j).getWord(),
                                    rackEditText.getText().toString() + myLetter))
                    {
                        //Log.d("INFO", "Scoring word: " + wordInfoArray.get(j).getWord());

                        // Keep track of index of max scoring word found
                        if(wordInfoArray.get(j).getWordScore() > wordInfoArray.get(maxScoreIndex).getWordScore())
                        {
                            maxScoreIndex = j;
                        }
                    }
                }
            }
        }

        // Update Output and Word Score text areas
        outputEditText.setText(wordInfoArray.get(maxScoreIndex).getWord());
        String scoreMsg = wordInfoArray.get(maxScoreIndex).getWordScore() + " points";
        wordScoreEditText.setText(scoreMsg);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Name: isScoringWord()
    // Arguments:
    //     word: passed in word from wordInfoArray (dictionary words)
    //     letters: string of letters from which to search for a valid scoring word
    // Purpose: Returns true if the passed in word if a valid scoring word and false
    //     otherwise.
    // Return Value: boolean
    // Dependencies: N/A
    ////////////////////////////////////////////////////////////////////////////////
    boolean isScoringWord(String word, String letters)
    {
        int index = 0;
        String myLetters = letters;

        //Log.d("INFO", "Inside isScoringWord: word = " + word + ", letters = " + letters);

        // Get out returning false if word has more characters than letters string
        if(word.length() > myLetters.length())
        {
            return false;
        }

        // Iterate through each letter in word to see if exists in letters.
        // If no, return false. If yes, remove letter from letters and continue
        // iterating.
        for(int i = 0; i < word.length(); i++)
        {
            index = myLetters.indexOf(word.charAt(i));
            if(index == -1)
            {
                return false;
            }
            else
            {
                if(index == 0)
                {
                    myLetters = myLetters.substring(1, myLetters.length());
                }
                else if(index == myLetters.length()-1)
                {
                    myLetters = myLetters.substring(0, myLetters.length()-1);
                }
                else
                {
                    myLetters = myLetters.substring(0, index) +
                            myLetters.substring(index+1, myLetters.length());
                }
            }
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Name: resetButtonClicked()
    // Arguments: view
    // Purpose: Clears out the ‘Rack’, ‘Word’, ‘Output’ and ‘Word Score’ text areas
    //     and resets all data in the ‘Letter Tracker’ text area to original values
    // Return Value: void
    // Dependencies: Calls updateLetterTracking()
    ////////////////////////////////////////////////////////////////////////////////
    public void resetButtonClicked(View view)
    {
        EditText rackEditText = (EditText)findViewById(R.id.rack_edittext);
        EditText wordEditText = (EditText)findViewById(R.id.word_edittext);
        EditText outputEditText = (EditText)findViewById(R.id.output_edittext);
        EditText wordScoreEditText = (EditText)findViewById(R.id.wordscore_edittext);

        // Clear out all text fields
        rackEditText.setText("");
        wordEditText.setText("");
        outputEditText.setText("");
        wordScoreEditText.setText("");

        updateLetterTracking();
    }

}
