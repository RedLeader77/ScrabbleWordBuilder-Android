The ScrabbleWordBuilder application is an Android app made up of the following files:

dictionary.txt
letters.json
activity_main.xml
LetterInfo.java
WordInfo.java
MainActivity.java

The app includes a read only EditText field labeled ‘Letter Tracker’ which dynamically keeps track of the number of game 
pieces available. The number available of each letter is the first value in parenthesis. This value changes as the user 
enters characters in either the ‘Rack’ or ‘Word’ EditText areas. The second value is the number of points for the letter, 
a static value. I included the Letter Tracker data for both debugging purposes as well as facilitating the user 
experience. Below the ‘Word’ text area are the ‘Process’ and ‘Reset’ buttons. Clicking the ‘Process’ button causes the 
processButtonClicked() method to be called. This is the entry point for determining what output should be displayed 
(error messages, valid scoring word, word score) in the ‘Output’ and ‘Word Score’ EditText areas. Clicking the ‘Reset’ 
button clears out the ‘Rack’, ‘Word’, ‘Output’ and ‘Word Score’ EditText areas and resets all data in the ‘Letter Tracker’ 
EditText area to original values allowing the user to enter an new word and letters without having to clear the fields 
manually. 

To use this app the user would enter 2-7 letters in the Rack EditText area and a word in the Word EditText area. The Word 
field is optional as the user may wish to build a word from scratch using just the letters in their rack. If more than 
one word is present on a Scrabble board the user is left with the challenge of finding the best word to play off of. 
However, once a word is chosen, the highest scoring word will be determined for them.

