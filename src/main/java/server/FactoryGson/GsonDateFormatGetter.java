package server.FactoryGson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonDateFormatGetter implements IGson {
    @Override
    public Gson getGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }
}
