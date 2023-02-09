package webserver.games;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.openapi.*;
import webserver.Controller;
import webserver.HangmanService;
import webserver.games.impl.GameControllerImpl;
import webserver.lobbies.LobbyController;
import webserver.lobbies.impl.LobbyControllerImpl;

public interface GameController extends Controller {

    @OpenApi(
            operationId = "GameApi::startGame",
            path = HangmanService.BASE_URL + "/games",
            methods = {HttpMethod.POST},
            tags = {"games"},
            description = "Start a new game",
            requestBody = @OpenApiRequestBody(
                    description = "The creator's nickname",
                    required = true,
                    content = {
                            @OpenApiContent(
                                    from = String.class,
                                    mimeType = ContentType.JSON
                            )
                    }
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The nickname provided is correct, the lobby is created and it's id returned.",
                            content = {
                                    @OpenApiContent(
                                            from = String.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided nickname corresponds to no known user"
                    )
            }
    )
    void postGame(Context context) throws HttpResponseException;

    void putWordToGuess(Context context) throws HttpResponseException;

    void getGame(Context context) throws HttpResponseException;

    void putGuessAttempt(Context context) throws HttpResponseException;

    static GameController of(String root) {
        return new GameControllerImpl(root);
    }
}
