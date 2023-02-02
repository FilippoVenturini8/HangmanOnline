package common;

import java.util.List;

public class Game {
    private List<User> users;

    private int round = 1;

    public Game (){}
    public Game(List<User> users){
        this.users = users;
    }

    public void setUsers(List<User> users){this.users = users;}

    public int getRound(){
        return this.round;
    }

    public void setRndGameRoles(){
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
