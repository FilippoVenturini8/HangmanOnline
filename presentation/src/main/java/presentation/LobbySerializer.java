package presentation;

import com.google.gson.*;
import common.Lobby;
import common.User;

import java.lang.reflect.Type;

public class LobbySerializer implements JsonSerializer<Lobby> {
    @Override
    public JsonElement serialize(Lobby src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();

        JsonArray users = new JsonArray();

        for(User user : src.getUsers()){
            users.add(context.serialize(user));
        }

        object.addProperty("id", src.getId());
        object.add("users", users);

        return object;
    }
}
