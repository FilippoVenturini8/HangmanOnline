package webserver.lobbies;

import common.Lobby;
import common.User;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.openapi.*;
import webserver.Controller;
import webserver.HangmanService;
import webserver.lobbies.impl.LobbyControllerImpl;
import webserver.users.UserController;
import webserver.users.impl.UserControllerImpl;

public interface LobbyController extends Controller {

    @OpenApi(
            operationId = "LobbyApi::createLobby",
            path = HangmanService.BASE_URL + "/lobbies",
            methods = {HttpMethod.POST},
            tags = {"lobbies"},
            description = "Create a new Lobby",
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
    void postLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::deleteLobby",
            path = HangmanService.BASE_URL + "/lobbies/{lobbyId}",
            methods = {HttpMethod.DELETE},
            tags = {"lobbies"},
            description = "Deletes a lobby, given it's id",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "The id of the lobby to remove",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "201",
                            description = "The provided id corresponds to a lobby, which is thus removed. Nothing is returned"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to not existing lobby"
                    )
            }
    )
    void deleteLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::getAllLobbies",
            path = HangmanService.BASE_URL + "/lobbies",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "Retrieves all lobbies",
            queryParams = {
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "A List containing all the system's lobbies.",
                            content = {
                                    @OpenApiContent(
                                            from = Lobby[].class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    )
            }
    )
    void getAllLobbies(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::getLobby",
            path = HangmanService.BASE_URL + "/lobbies/{lobbyId}",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "Gets the data of a lobby, given it's id.",
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
                            description = "The provided id corresponds to a lobby, whose data is thus returned",
                            content = {
                                    @OpenApiContent(
                                            from = Lobby.class,
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
    void getLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::addUserToLobby",
            path = HangmanService.BASE_URL + "/lobbies/{lobbyId}",
            methods = {HttpMethod.PUT},
            tags = {"lobbies"},
            description = "Put the user corresponding to the nickname passed, in the lobby corresponding to the id passed.",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "Id of the lobby",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "The user's nickname",
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
                            description = "The provided id corresponds to a lobby, the user is now connected to the lobby, nothing is returned"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to not existing lobby"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: the lobby is full"
                    )
            }
    )
    void putUserInLobby(Context context) throws HttpResponseException;


    @OpenApi(
            operationId = "LobbyApi::addUserToLobby",
            path = HangmanService.BASE_URL + "/lobbies/exit/{lobbyId}",
            methods = {HttpMethod.PUT},
            tags = {"lobbies"},
            description = "Disconnect the user corresponding to the nickname passed from the lobby corresponding to the id passed.",
            pathParams = {
                    @OpenApiParam(
                            name = "lobbyId",
                            type = String.class,
                            description = "Id of the lobby",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "The user's nickname",
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
                            description = "The provided id corresponds to a lobby, the user is now disconnected from the lobby, nothing is returned"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to not existing lobby"
                    )
            }
    )
    void putOutUserFromLobby(Context context) throws HttpResponseException;

    static LobbyController of(String root) {
        return new LobbyControllerImpl(root);
    }

}
