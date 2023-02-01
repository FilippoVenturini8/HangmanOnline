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
                lobbyId = client.createLobby(actualUser);
                System.out.println("Lobby creata correttamente, codice lobby: "+lobbyId);
                while (true){
                    ; //TODO gioco
                }
                //break;
            case "2":

                boolean lobbyOk = false;

                while (!lobbyOk){
                    List<Lobby> allLobbies = client.getAllLobbies();
                    for(Lobby lobby : allLobbies){
                        System.out.println("Lobby: " + lobby.getId() + "(" + lobby.getConnectedUserNumber() + "/2)");
                    }

                    System.out.print("Inserisci il codice della lobby a cui vuoi connetterti: ");
                    String lobbyIdToConnect = scanner.nextLine();

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

                break;
        }

    }
}
