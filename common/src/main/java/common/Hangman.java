package common;

public interface Hangman {
    void connectUser(User user) throws ConflictException;

    int createLobby(User user);
}
