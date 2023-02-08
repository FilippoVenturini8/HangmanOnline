package common;

import java.util.ArrayList;
import java.util.List;

public class LocalHangman implements Hangman{

    private List<User> users = new ArrayList<>();

    private List<Lobby> lobbies = new ArrayList<>();

    private int lobbiesCounter = 0;

    @Override
    public void connectUser(User user) throws ConflictException{
        var copy = new User(user.getNickName()); //Defensive copy
        if (copy.getNickName() == null || copy.getNickName().isBlank()) {
            //TODO AGGIUNGERE ECCEZIONE: NICKNAME VUOTO
        }
        if(users.contains(copy)){
            throw new ConflictException("Nickname " + copy.getNickName() + " già in uso!");
        }
        users.add(copy);
    }

    @Override
    public void disconnectUser(String nicknameUser) throws MissingException {
        User toRemove = this.findUser(nicknameUser);
        this.users.remove(toRemove);
    }

    @Override
    public User findUser(String nicknameUser) throws MissingException {
        boolean foundUser = false;
        int userIndex = -1;
        for(int i = 0; i < this.users.size(); i++){
            if(users.get(i).getNickName().equals(nicknameUser)){
                foundUser = true;
                userIndex = i;
                break;
            }
        }
        if(!foundUser){
            throw new MissingException("L'utente con nickname " + nicknameUser + " è inesistente.");
        }
        return this.users.get(userIndex);
    }

    @Override
    public int createLobby(String nicknameUser) throws MissingException{
        Lobby newLobby = new Lobby(++lobbiesCounter);
        User user = this.findUser(nicknameUser);
        newLobby.addUser(user);
        lobbies.add(newLobby);
        return newLobby.getId();
    }

    @Override
    public void deleteLobby(int idLobby) throws MissingException {
        Lobby toDelete = this.getLobby(idLobby);
        toDelete.getUsers().get(0).setAsNotInGame();
        toDelete.getUsers().get(1).setAsNotInGame();
        this.lobbies.remove(toDelete);
    }

    @Override
    public void joinLobby(int idLobby, String nicknameUser) throws MissingException, ConflictException{
        Lobby lobby = this.getLobby(idLobby); //Se non è presente la lobby viene generata una MissingException qui

        if(lobby.isFull()){
            throw new ConflictException("Lobby " + idLobby + " piena.");
        }
        User user = this.findUser(nicknameUser);
        lobby.addUser(user);
    }

    @Override
    public Lobby getLobby(int idLobby) throws MissingException{
        boolean foundLobby = false;
        int lobbyIndex = -1;
        for(int i = 0; i < this.lobbies.size(); i++){
            if(lobbies.get(i).getId() == idLobby){
                foundLobby = true;
                lobbyIndex = i;
                break;
            }
        }
        if(!foundLobby){
            throw new MissingException("La lobby con codice " + idLobby + " è inesistente.");
        }
        return this.lobbies.get(lobbyIndex);
    }

    @Override
    public List<Lobby> getAllLobbies() {
        return List.copyOf(this.lobbies);
    }

    @Override
    public void startGame(int idLobby, Game game) throws MissingException {
        Lobby lobby = this.getLobby(idLobby);
        game.setPlayers(lobby.getUsers());
        game.setRndGameRoles();
        lobby.setGame(game);
    }

    @Override
    public String setWordToGuess(int idLobby, String toGuess) throws MissingException {
        Game game = this.getGame(idLobby);
        game.setWordToGuess(toGuess);
        game.encodeWordToGuess();
        return game.getEncodedWordToGuess();
    }

    @Override
    public Game getGame(int idLobby) throws MissingException {
        Lobby lobby = this.getLobby(idLobby);
        return lobby.getGame();
    }

    @Override
    public Boolean tryToGuess(int idLobby, String attempt) throws MissingException {
        Game game = this.getGame(idLobby);

        boolean guessed = game.tryToGuess(attempt);
        if(game.getAttempts() == 0 && !game.getGuesserRoundWon()){
            game.incWon(GameRole.CHOOSER);
            if(game.getRound() == 1){ //Done only one round, the second is mandatory
                game.newRound();
                game.switchGameRoles();
            } else if (game.getRound() == 2 && game.isNeededExtraRound()) { //For the third round rnd game roles
                game.newRound();
                game.setRndGameRoles();
            }
        }
        else if(game.getGuesserRoundWon()){
            game.incWon(GameRole.GUESSER);
            if(game.getRound() == 1){ //Done only one round, the second is mandatory
                game.newRound();
                game.switchGameRoles();
            } else if (game.getRound() == 2 && game.isNeededExtraRound()) { //For the third round rnd game roles
                game.newRound();
                game.setRndGameRoles();
            }
        }
        return guessed;
    }


}
