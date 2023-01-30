package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Objects;

public class AbstractHttpClientStub {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final URI serviceURI;

    public AbstractHttpClientStub(URI host, String root, String version) {
        Objects.requireNonNull(host);
        this.serviceURI = host.resolve(String.format("/%s/v%s", root, version));
    }

    //TODO aggiungere la serializzazione e deserializzazione
}
