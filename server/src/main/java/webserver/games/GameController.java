package webserver.games;

import common.Game;
import common.Lobby;
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
            path = HangmanService.BASE_URL + "/games/{lobbyId}",
            methods = {HttpMethod.POST},
            tags = {"games"},
            description = "Start a new game",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "The id of the lobby where to start the game",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "The new game to put in the lobby",
                    required = true,
                    content = {
                            @OpenApiContent(
                                    from = Game.class,
                                    mimeType = ContentType.JSON
                            )
                    }
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided id corresponds to a lobby, the game is now started and associated to the lobby, nothing is returned."
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to not existing lobby"
                    )
            }
    )
    void postGame(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "GameApi::setWordToGuess",
            path = HangmanService.BASE_URL + "/games/{lobbyId}",
            methods = {HttpMethod.PUT},
            tags = {"games"},
            description = "Set the word to guess, given the id of the game's lobby",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "Id of the lobby",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "The word to guess",
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
                            description = "The provided id corresponds to a lobby, the word is set, and it's encoded version is returned",
                            content = {
                                    @OpenApiContent(
                                            from = String.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to not existing lobby"
                    )
            }
    )
    void putWordToGuess(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "GameApi::getGame",
            path = HangmanService.BASE_URL + "/lobbies/{lobbyId}",
            methods = {HttpMethod.GET},
            tags = {"games"},
            description = "Gets the data of a game, given the corresponding lobby's id.",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "Id of the lobby",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided id corresponds to a lobby, the data of it's game are returned",
                            content = {
                                    @OpenApiContent(
                                            from = Game.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to not existing lobby"
                    )
            }
    )
    void getGame(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "GameApi::setWordToGuess",
            path = HangmanService.BASE_URL + "/games/attempt/{lobbyId}",
            methods = {HttpMethod.PUT},
            tags = {"games"},
            description = "Try an attept to guess the word, given the game's lobby id",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "Id of the lobby",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "The attempt",
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
                            description = "The provided id corresponds to a lobby, the attempt is tried and the result of the attempt is returned",
                            content = {
                                    @OpenApiContent(
                                            from = Boolean.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to not existing lobby"
                    )
            }
    )
    void putGuessAttempt(Context context) throws HttpResponseException;

    static GameController of(String root) {
        return new GameControllerImpl(root);
    }
}
