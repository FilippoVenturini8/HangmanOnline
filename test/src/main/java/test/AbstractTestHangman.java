package test;

import common.ConflictException;
import common.Hangman;
import common.MissingException;
import common.User;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractTestHangman {
    private final User filippo = new User("ventu00");

    private final User alberto = new User("albyDigi");

    private final User pietro = new User("lello0");

    private final User nicolo = new User("$maluNico$");

    private final User martin = new User("tutron");

    private final User marco = new User("automa123");

    private Hangman hangman;

    @BeforeEach
    public final void setup() throws ConflictException, IOException {
        beforeCreatingHangman();
        hangman = createHangman();

        hangman.connectUser(filippo);
        hangman.connectUser(alberto);
        hangman.connectUser(pietro);
    }

    protected abstract void beforeCreatingHangman() throws IOException;

    protected abstract Hangman createHangman() throws ConflictException;

    public void testUserConnections() throws ConflictException, MissingException {
        hangman.connectUser(nicolo);
        hangman.connectUser(martin);
        hangman.connectUser(marco);

        assertEquals(nicolo, hangman.findUser(nicolo.getNickName()));
        assertEquals(martin, hangman.findUser(martin.getNickName()));
        assertEquals(marco, hangman.findUser(marco.getNickName()));
    }

    public void testConnectionError(){
        assertThrows(ConflictException.class, () -> hangman.connectUser(filippo));
        assertThrows(ConflictException.class, () -> hangman.connectUser(alberto));
        assertThrows(ConflictException.class, () -> hangman.connectUser(pietro));
    }

    public void testLobbyCreation() throws MissingException {
        int idLobbyFilippo = hangman.createLobby(filippo.getNickName());
        int idLobbyAlberto = hangman.createLobby(alberto.getNickName());
        int idLobbyPietro = hangman.createLobby(pietro.getNickName());

        assertEquals(idLobbyFilippo, hangman.getAllLobbies().get(0).getId());
        assertEquals(idLobbyAlberto, hangman.getAllLobbies().get(1).getId());
        assertEquals(idLobbyPietro, hangman.getAllLobbies().get(2).getId());
    }

    public void testLobbyCreationError(){
        assertThrows(MissingException.class, () -> hangman.createLobby(nicolo.getNickName()));
        assertThrows(MissingException.class, () -> hangman.createLobby(marco.getNickName()));
        assertThrows(MissingException.class, () -> hangman.createLobby(martin.getNickName()));
    }

    

}
