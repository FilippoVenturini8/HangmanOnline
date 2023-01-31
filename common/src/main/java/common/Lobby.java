package common;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    List<User> users = new ArrayList<>();

    public void addUser(User user){
        this.users.add(user);
    }

    public void removeUser(User user){
        this.users.remove(user);
    }

    public boolean isFull(){
        return this.users.size() == 2;
    }

    public List<User> getUsers(){
        return List.copyOf(users);
    }
}
