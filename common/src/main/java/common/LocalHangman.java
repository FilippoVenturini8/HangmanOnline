package common;

import java.util.ArrayList;
import java.util.List;

public class LocalHangman implements Hangman{

    private List<User> users = new ArrayList<>();

    @Override
    public void connectUser(User user) throws ConflictException{
        var copy = new User(user.nickName); //Defensive copy
        if (copy.getNickName() == null || copy.getNickName().isBlank()) {
            //TODO AGGIUNGERE ECCEZIONE: NICKNAME VUOTO
        }
        if(users.contains(copy)){
            throw new ConflictException("Nickname " + copy.nickName + " già in uso!");
        }
        users.add(copy);
        System.out.println("Utente aggiunto !"+users);
    }
}
