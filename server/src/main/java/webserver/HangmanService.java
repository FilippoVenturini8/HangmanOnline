package webserver;
import io.javalin.Javalin;

public class HangmanService {

    private static final int DEFAULT_PORT = 10000;

    private final int port;

    private final Javalin server;

    public HangmanService(int port){
        this.port = port;
        server = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });
    }

    public void start(){server.start(port);}

    public void stop(){server.stop();}

    public static void main(String[] args) {
        new HangmanService(args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT).start();
    }
}
