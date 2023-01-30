package client;

import java.net.URI;

public class HangmanClient extends AbstractHttpClientStub{
    private static final int port = 10000;

    public HangmanClient(URI host) {
        super(host, "hangman", "0.1.0");
    }

    public HangmanClient(String host, int port){
        this(URI.create("http://" + host + ":" + port));
    }

    public static void main(String[] args) {
        HangmanClient client = new HangmanClient("localhost",port);
        System.out.println("Client avviato!");
    }
}
