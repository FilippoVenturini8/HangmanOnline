package common;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private int id;

    private List<User> users = new ArrayList<>();

    private Game game;

    public Lobby(int id){
        this.id = id;
    }

    public void addUser(User user){
        this.users.add(user);
    }

    public void removeUser(User user){
        if(this.users.contains(user)){
            this.users.remove(user);
        }
        if(this.game != null){
            this.game.removePlayer(user);
        }
    }

    public boolean isFull(){
        return this.users.size() == 2;
    }

    public List<User> getUsers(){
        return this.users;
    }

    public int getId(){
        return this.id;
    }

    public int getConnectedUserNumber(){ return this.users.size();}

    public void setGame(Game game){
        this.game = game;
    }

    public Game getGame(){return this.game;}
}
