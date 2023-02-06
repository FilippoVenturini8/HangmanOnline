package presentation;

import com.google.gson.*;
import common.Lobby;
import common.User;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LobbyDeserializer implements JsonDeserializer<Lobby> {

    private String getPropertyAsString(JsonObject object, String name) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return value.getAsString();
        }
        return null;
    }

    private <T> T getPropertyAs(JsonObject object, String name, Class<T> type, JsonDeserializationContext context) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return context.deserialize(value, type);
        }
        return null;
    }

    @Override
    public Lobby deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = json.getAsJsonObject();

        var id = getPropertyAs(object, "id", Integer.class, context);

        var usersArray = object.getAsJsonArray("users");

        List<User> users = new ArrayList<>(usersArray.size());
        for (JsonElement item : usersArray) {
            if (item.isJsonNull()) continue;
            users.add(context.deserialize(item, User.class)); //TODO NOME PROPRIETà ?
        }

        Lobby lobby = new Lobby(id);
        for(User user : users){
            lobby.addUser(user);
        }

        return lobby;
    }
}
