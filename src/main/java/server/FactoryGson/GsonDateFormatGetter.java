package server.FactoryGson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.Consts.DateFormatter;

public class GsonDateFormatGetter implements IGson {
    @Override
    public Gson getGson() {
        return new GsonBuilder().setDateFormat(DateFormatter.DateFormatterPattern).create();
    }
}
