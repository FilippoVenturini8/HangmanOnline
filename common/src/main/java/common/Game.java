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

    public void setWordToGuess(String wordToGuess){this.wordToGuess = wordToGuess.toLowerCase();}

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

    public boolean tryToGuess(String attempt){
        boolean guessedSomething = false;
        if(attempt.length() == 1){ //Only one letter
            char attemptLetter = attempt.toLowerCase().charAt(0);
            for(int i = 0; i < this.wordToGuess.length(); i++){
                if(attemptLetter == this.wordToGuess.charAt(i)){
                    if(i == this.encodedWordToGuess.length()-1){ //Last character
                        this.encodedWordToGuess = this.encodedWordToGuess.substring(0,i) + attemptLetter;
                    }else{
                        this.encodedWordToGuess = this.encodedWordToGuess.substring(0,i) + attemptLetter + this.encodedWordToGuess.substring(i+1);
                    }
                    guessedSomething = true;
                }
            }
            if(!guessedSomething){
                this.attempts--;
            }

        }else{ //The entire word
            if(!this.wordToGuess.equals(attempt.toLowerCase())){
                this.attempts--;
            }else {
                guessedSomething = true;
                this.encodedWordToGuess = this.wordToGuess;
            }
        }
        return guessedSomething;
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
