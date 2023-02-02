package common;

import java.util.Objects;

public class User {
    private String nickName;

    private GameRole gameRole = GameRole.NOT_IN_GAME;

    public User (String nickName){
        this.nickName = nickName;
    }

    public User(String nickName, GameRole gameRole){this.nickName = nickName; this.gameRole = gameRole;}

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public GameRole getGameRole(){
        return this.gameRole;
    }

    public void setAsChooser(){
        this.gameRole = GameRole.CHOOSER;
    }

    public void setAsGuesser(){
        this.gameRole = GameRole.GUESSER;
    }

    public void setAsNotInGame(){
        this.gameRole = GameRole.NOT_IN_GAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(nickName, user.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickName);
    }

    @Override
    public String toString() {
        return "User{" +
                "nickName='" + nickName + '\'' +
                '}';
    }
}
