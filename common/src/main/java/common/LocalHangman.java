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
        System.out.println("Utente aggiunto: "+copy.getNickName());
    }

    @Override
    public int createLobby(User user) {
        Lobby newLobby = new Lobby(lobbiesCounter++);
        newLobby.addUser(user);
        lobbies.add(newLobby);
        System.out.println("Lobby creata!");
        return newLobby.getId();
    }

    @Override
    public List<Lobby> getAllLobbies() {
        return List.copyOf(this.lobbies);
    }
}
