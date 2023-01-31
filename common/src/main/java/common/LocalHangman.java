package common;

import java.util.ArrayList;
import java.util.List;

public class LocalHangman implements Hangman{

    private List<User> users = new ArrayList<>();

    private List<Lobby> lobbies = new ArrayList<>();

    @Override
    public void connectUser(User user) throws ConflictException{
        var copy = new User(user.nickName); //Defensive copy
        if (copy.getNickName() == null || copy.getNickName().isBlank()) {
            //TODO AGGIUNGERE ECCEZIONE: NICKNAME VUOTO
        }
        if(users.contains(copy)){
            throw new ConflictException("Nickname " + copy.nickName + " già in uso!");
        }
        System.out.println("Utente aggiunto: "+copy.nickName);
    }

    @Override
    public void createLobby(User user) {
        Lobby newLobby = new Lobby();
        newLobby.addUser(user);
        lobbies.add(newLobby);
        System.out.println("Lobby creata!");
    }
}
