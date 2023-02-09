package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private int round = 1;

    private boolean guesserRoundWon = false;

    private int attempts = 5;

    private List<User> players;

    private List<Integer> results;

    private String wordToGuess;

    private String encodedWordToGuess = "";

    public Game (){}

    public Game(int round, int attempts, String toGuess, String encodedToGuess, List<User> players, List<Integer> results, boolean guesserRoundWon) {
        this.round = round;
        this.attempts = attempts;
        this.wordToGuess = toGuess;
        this.encodedWordToGuess = encodedToGuess;
        this.players = players;
        this.results = results;
        this.guesserRoundWon = guesserRoundWon;
    }

    public void setPlayers(List<User> players){
        this.players = players;
        this.results = new ArrayList<>(List.of(0,0)); //Set initial state of the results
    }

    public int getRoundWon(User player){
        return this.players.get(0).getNickName().equals(player.getNickName()) ? this.results.get(0) : this.results.get(1);
    }

    public List<Integer> getResults(){
        return this.results;
    }

    public void incWon(GameRole role){
        for(User player : this.players){
            if(player.getGameRole().equals(role)){ //Search the right role
                if(player.equals(this.players.get(0))){
                    this.results.set(0, this.results.get(0)+1); //Increment round won by the first player
                }else {
                    this.results.set(1, this.results.get(1)+1); //Increment round won by the second player
                }
            }
        }
    }

    public int getRound(){
        return this.round;
    }

    public void newRound(){
        this.wordToGuess = null;
        this.guesserRoundWon = false;
        this.encodedWordToGuess = "";
        this.attempts = 5;
        this.round++;
    }

    public List<User> getPlayers(){return this.players;}

    public String getWordToGuess(){ return this.wordToGuess;}

    public void setWordToGuess(String wordToGuess){this.wordToGuess = wordToGuess.toLowerCase();}

    public String getEncodedWordToGuess() {
        return encodedWordToGuess;
    }

    public int getAttempts(){return this.attempts;}

    public boolean getGuesserRoundWon(){return this.guesserRoundWon;}

    public boolean isNeededExtraRound(){
        return this.results.get(0) == 1 && this.results.get(1) == 1;
    }

    public boolean isGameFinished(){
        return this.results.get(0) == 2 || this.results.get(1) == 2;
    }

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
                if(attemptLetter == this.wordToGuess.charAt(i) && attemptLetter != this.encodedWordToGuess.charAt(i)){ //The char is correct and not yet guessed
                    if(i == this.encodedWordToGuess.length()-1){ //Last character
                        this.encodedWordToGuess = this.encodedWordToGuess.substring(0,i) + attemptLetter;
                    }else{
                        this.encodedWordToGuess = this.encodedWordToGuess.substring(0,i) + attemptLetter + this.encodedWordToGuess.substring(i+1);
                    }
                    guessedSomething = true;
                }
            }
            if(this.wordToGuess.equals(this.encodedWordToGuess)){
                this.guesserRoundWon = true;
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
                this.guesserRoundWon = true;
            }
        }
        return guessedSomething;
    }

    public void setRndGameRoles(){
        long rnd = Math.round(Math.random());

        if(rnd == 0){
            this.players.get(0).setAsChooser();
            this.players.get(1).setAsGuesser();
        }else{
            this.players.get(0).setAsGuesser();
            this.players.get(1).setAsChooser();
        }
    }

    public void switchGameRoles(){
        if(this.players.get(0).getGameRole().equals(GameRole.CHOOSER)){
            this.players.get(0).setAsGuesser();
            this.players.get(1).setAsChooser();
        }else{
            this.players.get(0).setAsChooser();
            this.players.get(1).setAsGuesser();
        }
    }
}
