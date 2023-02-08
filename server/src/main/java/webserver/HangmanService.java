package webserver;
import common.Hangman;
import common.LocalHangman;
import io.javalin.Javalin;
import presentation.GsonUtils;
import webserver.games.GameController;
import webserver.lobbies.LobbyController;
import webserver.users.UserController;
import webserver.utils.Filters;
import webserver.utils.Plugins;

public class HangmanService {

    private static final String API_VERSION = "0.1.0";

    public static final String BASE_URL = "/hangman/v" + API_VERSION;

    private static final int DEFAULT_PORT = 10000;

    private final int port;

    private final Javalin server;

    private final Hangman localHangman = new LocalHangman();

    public HangmanService(int port){
        this.port = port;
        server = Javalin.create(config -> {
            config.plugins.enableDevLogging();
            config.jsonMapper(new JavalinGsonAdapter(GsonUtils.createGson()));
            config.plugins.register(Plugins.openApiPlugin(API_VERSION, "/doc"));
            config.plugins.register(Plugins.swaggerPlugin("/doc", "/ui"));
            config.plugins.register(Plugins.routeOverviewPlugin("/routes"));
        });

        //Put the instance of the LocalHangman in the context. (The get will be done by the Controllers)
        server.before("/*", Filters.putSingletonInContext(Hangman.class, localHangman));

        UserController.of(path("/users")).registerRoutes(server);
        LobbyController.of(path("/lobbies")).registerRoutes(server);
        GameController.of(path("/games")).registerRoutes(server);
    }

    public void start(){server.start(port);}

    public void stop(){server.stop();}

    public static void main(String[] args) {
        new HangmanService(args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT).start();
    }

    private static String path(String subPath) {
        return BASE_URL + subPath;
    }
}
