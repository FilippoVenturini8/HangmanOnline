package client;

import common.ConflictException;
import common.Hangman;
import common.Lobby;
import common.User;

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

    private CompletableFuture<Integer> createLobbyAsync(User user) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .POST(body(user))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Integer.class));
    }

    @Override
    public int createLobby(User user) {
        try {
            return createLobbyAsync(user).join();
        } catch (CompletionException e) {
            //throw getCauseAs(e, ConflictException.class);
            //TODO AGGIUNGERE ECCEZIONE
            return -1;
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

    public static void main(String[] args) {
        HangmanClient client = new HangmanClient("localhost",port);
        Scanner scanner = new Scanner(System.in);

        boolean nickNameOk = false;

        User user = null;

        while (!nickNameOk){
            System.out.print("Inserire un nickname: ");
            user = new User(scanner.nextLine());
            try {
                client.connectUser(user);
                nickNameOk = true;
            } catch (ConflictException e) {
                System.out.println("Il nickname inserito è già in uso!");
                nickNameOk = false;
            }
        }

        System.out.println("Selezionare l'opzione desiderata:");
        System.out.println("[1] Crea una lobby");
        System.out.println("[2] Visualizza la lista delle lobby");

        String option = scanner.nextLine();
        int lobbyId;

        switch (option){
            case "1":
                lobbyId = client.createLobby(user);
                System.out.println("Lobby creata correttamente, codice lobby: "+lobbyId);
                break;
            case "2":
                List<Lobby> allLobbies = client.getAllLobbies();
                for(Lobby lobby : allLobbies){
                    System.out.println("Lobby: " + lobby.getId());
                }
        }

    }
}
