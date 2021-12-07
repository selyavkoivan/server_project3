package server.FactoryGson;

import com.google.gson.Gson;

public class GsonGetter implements IGson {
    @Override
    public Gson getGson() {
        return new Gson();
    }
}
