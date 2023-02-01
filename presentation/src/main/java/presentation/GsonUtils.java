package presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Lobby;
import common.User;

public class GsonUtils {
    public static Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                //TODO AGGIUNGERE I SERIALIZZATORI E DESERIALIZZATORI
                .registerTypeAdapter(User.class, new UserSerializer())
                .registerTypeAdapter(User.class, new UserDeserializer())
                .registerTypeAdapter(Lobby.class, new LobbySerializer())
                .registerTypeAdapter(Lobby.class, new LobbyDeserializer())
                .create();
    }
}
