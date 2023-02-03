package common;

import java.util.List;

public class Game {

    private int round = 1;

    private int attempts = 5;

    private String wordToGuess;

    private String encodedWordToGuess = "";

    public Game (){}

    public Game (int round, int attempts, String wordToGuess, String encodedWordToGuess){this.round = round; this.attempts = attempts; this.wordToGuess = wordToGuess; this.encodedWordToGuess = encodedWordToGuess;}

    public int getRound(){
        return this.round;
    }

    public String getWordToGuess(){ return this.wordToGuess;}

    public void setWordToGuess(String wordToGuess){this.wordToGuess = wordToGuess;}

    public String getEncodedWordToGuess() {
        return encodedWordToGuess;
    }

    public int getAttempts(){return this.attempts;}

    public void encodeWordToGuess(){
        for(int i = 0; i < this.wordToGuess.length(); i++){
            if(this.wordToGuess.charAt(i) == 'a' || this.wordToGuess.charAt(i) == 'e' || this.wordToGuess.charAt(i) == 'i' || this.wordToGuess.charAt(i) == 'o' || this.wordToGuess.charAt(i) == 'u'){
                this.encodedWordToGuess += '+';
            }else{
                this.encodedWordToGuess += '-';
            }
        }
    }

    public void setRndGameRoles(List<User>users){
        long rnd = Math.round(Math.random());

        if(rnd == 0){
            users.get(0).setAsChooser();
            users.get(1).setAsGuesser();
        }else{
            users.get(0).setAsGuesser();
            users.get(1).setAsChooser();
        }
    }
}
