package client;

import common.ConflictException;
import common.Hangman;
import common.User;

import java.net.URI;
import java.net.http.HttpRequest;
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

    public static void main(String[] args) {
        HangmanClient client = new HangmanClient("localhost",port);
        Scanner scanner = new Scanner(System.in);

        boolean nickNameOk = false;

        while (!nickNameOk){
            System.out.println("Inserire un nickname: ");
            User user = new User(scanner.nextLine());
            try {
                client.connectUser(user);
                nickNameOk = true;
            } catch (ConflictException e) {
                System.out.println("Il nickname inserito è già in uso!");
                nickNameOk = false;
            }
        }

    }
}
