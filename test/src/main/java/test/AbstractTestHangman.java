package test;

import common.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @AfterEach
    public final void teardown() throws InterruptedException {
        shutdownHangman(hangman);
        afterShuttingAuthenticatorDown();
    }

    protected abstract void shutdownHangman(Hangman hangman);

    protected abstract void afterShuttingAuthenticatorDown() throws InterruptedException;

    public void testUserConnections() throws ConflictException, MissingException {
        hangman.connectUser(nicolo);
        hangman.connectUser(martin);
        hangman.connectUser(marco);

        assertEquals(nicolo, hangman.findUser(nicolo.getNickName()));
        assertEquals(martin, hangman.findUser(martin.getNickName()));
        assertEquals(marco, hangman.findUser(marco.getNickName()));
    }

    public void testUserDisconnections() throws ConflictException, MissingException {
        hangman.connectUser(nicolo);
        hangman.connectUser(martin);
        hangman.connectUser(marco);

        hangman.disconnectUser(nicolo.getNickName());
        assertThrows(MissingException.class, () -> hangman.findUser(nicolo.getNickName()));
        hangman.disconnectUser(martin.getNickName());
        assertThrows(MissingException.class, () -> hangman.findUser(martin.getNickName()));
        hangman.disconnectUser(marco.getNickName());
        assertThrows(MissingException.class, () -> hangman.findUser(marco.getNickName()));
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

    public void testLobbyElimination() throws MissingException {
        int idLobbyFilippo = hangman.createLobby(filippo.getNickName());
        int idLobbyAlberto = hangman.createLobby(alberto.getNickName());
        int idLobbyPietro = hangman.createLobby(pietro.getNickName());

        hangman.deleteLobby(idLobbyFilippo);
        assertThrows(MissingException.class, () -> hangman.getLobby(idLobbyFilippo));
        assertThrows(MissingException.class, () -> hangman.getGame(idLobbyFilippo));

        hangman.deleteLobby(idLobbyAlberto);
        assertThrows(MissingException.class, () -> hangman.getLobby(idLobbyAlberto));
        assertThrows(MissingException.class, () -> hangman.getGame(idLobbyAlberto));

        hangman.deleteLobby(idLobbyPietro);
        assertThrows(MissingException.class, () -> hangman.getLobby(idLobbyPietro));
        assertThrows(MissingException.class, () -> hangman.getGame(idLobbyPietro));
    }

    public void testLobbyCreationError() {
        assertThrows(MissingException.class, () -> hangman.createLobby(nicolo.getNickName()));
        assertThrows(MissingException.class, () -> hangman.createLobby(marco.getNickName()));
        assertThrows(MissingException.class, () -> hangman.createLobby(martin.getNickName()));
    }

    public void testLobbyConnection() throws MissingException, ConflictException {
        int idLobbyFilippo = hangman.createLobby(filippo.getNickName());

        hangman.joinLobby(idLobbyFilippo, alberto.getNickName());

        assertThrows(ConflictException.class, () -> hangman.joinLobby(idLobbyFilippo, nicolo.getNickName()));
        assertThrows(ConflictException.class, () -> hangman.joinLobby(idLobbyFilippo, marco.getNickName()));
        assertThrows(ConflictException.class, () -> hangman.joinLobby(idLobbyFilippo, martin.getNickName()));
    }

    public void testStartGame() throws MissingException, ConflictException {
        int idLobbyFilippo = hangman.createLobby(filippo.getNickName());
        hangman.joinLobby(idLobbyFilippo, alberto.getNickName());

        hangman.startGame(idLobbyFilippo, new Game());

        assertEquals(5, hangman.getGame(idLobbyFilippo).getAttempts());
        assertEquals(filippo.getNickName(), hangman.getGame(idLobbyFilippo).getPlayers().get(0).getNickName());
        assertEquals(alberto.getNickName(), hangman.getGame(idLobbyFilippo).getPlayers().get(1).getNickName());
        assertEquals(List.of(0,0), hangman.getGame(idLobbyFilippo).getResults());
        assertEquals("", hangman.getGame(idLobbyFilippo).getEncodedWordToGuess());

        assertNotEquals(GameRole.NOT_IN_GAME, hangman.getGame(idLobbyFilippo).getPlayers().get(0).getGameRole());
        assertNotEquals(GameRole.NOT_IN_GAME, hangman.getGame(idLobbyFilippo).getPlayers().get(1).getGameRole());
    }

    public void testChooseAndEncodeWord() throws MissingException, ConflictException {
        int idLobbyFilippo = hangman.createLobby(filippo.getNickName());
        hangman.joinLobby(idLobbyFilippo, alberto.getNickName());
        hangman.startGame(idLobbyFilippo, new Game());

        String testWord = "testparola";
        String testEncodedWord = "-+---+-+-+";

        hangman.setWordToGuess(idLobbyFilippo, testWord);

        assertEquals(hangman.getGame(idLobbyFilippo).getWordToGuess(), testWord);
        assertEquals(hangman.getGame(idLobbyFilippo).getEncodedWordToGuess(), testEncodedWord);
    }

    public void testFailedAttempt() throws MissingException, ConflictException {
        int idLobbyFilippo = hangman.createLobby(filippo.getNickName());
        hangman.joinLobby(idLobbyFilippo, alberto.getNickName());
        hangman.startGame(idLobbyFilippo, new Game());

        String testWord = "daindovinare";
        String testEncodedWord = "-++--+-+-+-+";

        hangman.setWordToGuess(idLobbyFilippo, testWord);

        for(int i = 5; i>2; i--){
            hangman.tryToGuess(idLobbyFilippo, "q");
            assertEquals(hangman.getGame(idLobbyFilippo).getWordToGuess(), testWord);
            assertEquals(hangman.getGame(idLobbyFilippo).getEncodedWordToGuess(), testEncodedWord);
            assertEquals(hangman.getGame(idLobbyFilippo).getAttempts(), i-1);
        }

        hangman.tryToGuess(idLobbyFilippo, "parolasbagliata1");
        assertEquals(hangman.getGame(idLobbyFilippo).getEncodedWordToGuess(), testEncodedWord);
        assertEquals(hangman.getGame(idLobbyFilippo).getAttempts(), 1);

        hangman.tryToGuess(idLobbyFilippo, "parolasbagliata2");
        assertEquals(false, hangman.getGame(idLobbyFilippo).getGuesserRoundWon());
    }

    public void testCorrectAttempt() throws MissingException, ConflictException {
        int idLobbyFilippo = hangman.createLobby(filippo.getNickName());
        hangman.joinLobby(idLobbyFilippo, alberto.getNickName());
        hangman.startGame(idLobbyFilippo, new Game());

        String testWord = "daindovinare";

        hangman.setWordToGuess(idLobbyFilippo, testWord);

        hangman.tryToGuess(idLobbyFilippo, "a");
        assertEquals("-a+--+-+-a-+", hangman.getGame(idLobbyFilippo).getEncodedWordToGuess());
        assertEquals(5, hangman.getGame(idLobbyFilippo).getAttempts());
        hangman.tryToGuess(idLobbyFilippo, "i");
        assertEquals("-ai--+-i-a-+", hangman.getGame(idLobbyFilippo).getEncodedWordToGuess());
        assertEquals(5, hangman.getGame(idLobbyFilippo).getAttempts());
        hangman.tryToGuess(idLobbyFilippo, "n");
        assertEquals("-ain-+-ina-+", hangman.getGame(idLobbyFilippo).getEncodedWordToGuess());
        assertEquals(5, hangman.getGame(idLobbyFilippo).getAttempts());

        hangman.tryToGuess(idLobbyFilippo, "daindovinare");
        assertEquals(5, hangman.getGame(idLobbyFilippo).getAttempts());
    }

}
