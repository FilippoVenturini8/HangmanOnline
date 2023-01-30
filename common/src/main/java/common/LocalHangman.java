package common;

import java.util.ArrayList;
import java.util.List;

public class LocalHangman implements Hangman{

    private List<User> users = new ArrayList<>();

    @Override
    public void connectUser(User user) {
        var copy = new User(user.nickName); //Defensive copy
        if (copy.getNickName() == null || copy.getNickName().isBlank()) {
            //TODO AGGIUNGERE ECCEZIONE: NICKNAME VUOTO
        }
        if(users.contains(copy)){
            //TODO AGGIUNGERE ECCEZIONE: NICKNAME GIA IN USO
        }
        users.add(copy);
    }
}
