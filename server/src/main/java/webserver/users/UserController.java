package webserver.users;

import common.User;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.openapi.*;
import webserver.Controller;
import webserver.HangmanService;
import webserver.users.impl.UserControllerImpl;

public interface UserController extends Controller {
    @OpenApi(
            operationId = "UserApi::connectUser",
            path = HangmanService.BASE_URL + "/users",
            methods = {HttpMethod.POST},
            tags = {"users"},
            description = "Connect a new user to the system",
            requestBody = @OpenApiRequestBody(
                    description = "The user's data",
                    required = true,
                    content = {
                            @OpenApiContent(
                                    from = User.class,
                                    mimeType = ContentType.JSON
                            )
                    }
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The nickname provided is correct, the user is now connected to the system. Nothing is returned."
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: the nickname can't be empty"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: the nickname has already been taken"
                    )
            }
    )
    void postUser(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "UserApi::findUser",
            path = HangmanService.BASE_URL + "/users/{userId}",
            methods = {HttpMethod.GET},
            tags = {"users"},
            description = "Gets the data of a user, given it's nickname.",
            pathParams = {
                    @OpenApiParam(
                            name = "userId",
                            type = String.class,
                            description = "Nickname of the user",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided nickname corresponds to a user, whose data is thus returned",
                            content = {
                                    @OpenApiContent(
                                            from = User.class,
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
    void getUser(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "UserApi::disconnectUser",
            path = HangmanService.BASE_URL + "/users/{userId}",
            methods = {HttpMethod.DELETE},
            tags = {"users"},
            description = "Deletes a user, given it's nickname",
            pathParams = {
                    @OpenApiParam(
                            name = "userId",
                            type = String.class,
                            description = "The nickname of the user to remove",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "201",
                            description = "The provided nickname corresponds to a user, which is thus removed. Nothing is returned"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided nickname corresponds to no known user"
                    )
            }
    )
    void deleteUser(Context context) throws HttpResponseException;

    static UserController of(String root) {
        return new UserControllerImpl(root);
    }
}
