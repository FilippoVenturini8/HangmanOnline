package client;

import common.*;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class HangmanClient extends AbstractHttpClientStub implements Hangman {
    private static final int port = 10000;

    public HangmanClient(URI host) {
        super(host, "hangman", "0.1.0");
    }

    public HangmanClient(String host, int port){
        this(URI.create("http://" + host + ":" + port));
    }

    private CompletableFuture<?> connectUserAsync(User user) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users"))
                .header("Accept", "application/json")
                .POST(body(user))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }

    @Override
    public void connectUser(User user) throws ConflictException{
        try {
            connectUserAsync(user).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, ConflictException.class);
        }
    }

    private CompletableFuture<User> findUserAsync(String nicknameUser){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users/"+nicknameUser))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(User.class));
    }

    @Override
    public User findUser(String nicknameUser) throws MissingException {
        try {
            return findUserAsync(nicknameUser).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<Integer> createLobbyAsync(String nicknameUser) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .POST(body(nicknameUser))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Integer.class));
    }

    @Override
    public int createLobby(String nicknameUser) throws MissingException{
        try {
            return createLobbyAsync(nicknameUser).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<Lobby> getLobbyAsync(int idLobby) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/"+idLobby))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Lobby.class));
    }

    @Override
    public Lobby getLobby(int idLobby) throws MissingException{
        try {
            return getLobbyAsync(idLobby).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<List<Lobby>> getAllLobbiesAsync() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeMany(Lobby.class));
    }

    @Override
    public List<Lobby> getAllLobbies() {
        return getAllLobbiesAsync().join();
    }

    private CompletableFuture<?> startGameAsync(int idLobby, Game game){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/"+idLobby))
                .header("Accept", "application/json")
                .POST(body(game))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }

    @Override
    public void startGame(int idLobby, Game game) throws MissingException {
        try {
            startGameAsync(idLobby, game).join();
        }catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<String> setWordToGuessAsync(int idLobby, String toGuess){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/"+idLobby))
                .header("Accept", "application/json")
                .PUT(body(toGuess))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }

    @Override
    public String setWordToGuess(int idLobby, String toGuess) {
        try {
           return setWordToGuessAsync(idLobby, toGuess).join();
        }catch (CompletionException e) {
            //throw getCauseAs(e, MissingException.class);
            return "";
            //TODO ECCEZIONE?
        }
    }

    private CompletableFuture<?> joinLobbyAsync(int idLobby, User user) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/"+idLobby))
                .header("Accept", "application/json")
                .PUT(body(user))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }

    @Override
    public void joinLobby(int idLobby, User user) throws MissingException, ConflictException {
        try {
            joinLobbyAsync(idLobby, user).join();
        } catch (CompletionException e) {
            if(e.getCause() instanceof MissingException){
                throw getCauseAs(e, MissingException.class);
            }
            if(e.getCause() instanceof ConflictException){
                throw getCauseAs(e, ConflictException.class);
            }
        }
    }

    public static void main(String[] args) {
        HangmanClient client = new HangmanClient("localhost",port);
        Scanner scanner = new Scanner(System.in);

        boolean nickNameOk = false;

        User actualUser = null;

        while (!nickNameOk){
            System.out.print("Inserire un nickname: ");
            actualUser = new User(scanner.nextLine());
            try {
                client.connectUser(actualUser);
                nickNameOk = true;
            } catch (ConflictException e) {
                System.out.println("Il nickname inserito è già in uso!");
                nickNameOk = false;
            }
        }

        System.out.println("\n######### MENU ##################");
        System.out.println("[1] Crea una lobby");
        System.out.println("[2] Visualizza la lista delle lobby");
        System.out.println("###################################");

        String option = scanner.nextLine();
        int lobbyId;

        switch (option){
            case "1":
                try {
                    lobbyId = client.createLobby(actualUser.getNickName());
                } catch (MissingException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Lobby creata correttamente, codice lobby: "+lobbyId);
                try {
                    while (true){
                        Lobby myLobby = client.getLobby(lobbyId);
                        if(myLobby.isFull()){ //Another player is connected
                            break;
                        }
                        Thread.sleep(500);
                    }
                    client.inGame(actualUser, lobbyId); //Enter the game
                } catch (MissingException e) {
                    System.out.println("Lobby "+ lobbyId + " inesistente.");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "2":

                boolean lobbyOk = false;
                String lobbyIdToConnect = null;

                while (!lobbyOk){
                    List<Lobby> allLobbies = client.getAllLobbies();
                    for(Lobby lobby : allLobbies){
                        System.out.println("Lobby: " + lobby.getId() + "(" + lobby.getConnectedUserNumber() + "/2)");
                    }

                    System.out.print("Inserisci il codice della lobby a cui vuoi connetterti: ");
                    lobbyIdToConnect = scanner.nextLine();

                    try {
                        client.joinLobby(Integer.valueOf(lobbyIdToConnect), actualUser);
                        lobbyOk = true;
                    }catch (MissingException e){
                        System.out.println("La lobby selezionata non è presente.");
                        lobbyOk = false;
                    }catch (ConflictException e){
                        System.out.println("La lobby selezionata è piena.");
                        lobbyOk = false;
                    }
                }

                try {
                    client.startGame(Integer.valueOf(lobbyIdToConnect), new Game());
                } catch (MissingException e) {
                    throw new RuntimeException(e);
                }

                break;
        }
    }

    private void inGame(User actualUser, int idLobby) {
        Scanner scanner = new Scanner(System.in);
        try {
            GameRole myRole = this.findUser(actualUser.getNickName()).getGameRole();
            switch (myRole){
                case CHOOSER:
                    System.out.print("Parola da far indovinare: ");
                    String toGuess = scanner.nextLine();
                    String encodedToGuess = this.setWordToGuess(idLobby, toGuess);
                    System.out.println(encodedToGuess);
                    break;
                case GUESSER:

                    break;
            }

        } catch (MissingException e) {
            throw new RuntimeException(e);
        }
    }

}
