import org.junit.Test;
import server.FactoryGson.GsonDateFormatGetter;
import server.FactoryGson.GsonGetter;

import java.util.Date;

public class DateFormatTest {

    @Test
    public void testGsonDateTimeFormat()
    {
        System.out.println(new GsonDateFormatGetter().getGson().toJson(new Date()));
    }

    @Test
    public void testGsonDefaultDateTimeFormat()
    {
        System.out.println(new GsonGetter().getGson().toJson(new Date()));
    }
}
