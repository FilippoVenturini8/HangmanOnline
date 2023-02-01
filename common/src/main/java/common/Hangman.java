package common;

import java.util.List;

public interface Hangman {
    void connectUser(User user) throws ConflictException;

    int createLobby(User user);

    List<Lobby> getAllLobbies();

    void joinLobby(int idLobby, User user) throws MissingException;
}
