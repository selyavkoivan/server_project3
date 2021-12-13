package server.Models;

import lombok.*;
import server.FactoryGson.GsonDateFormatGetter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(builderMethodName = "messageBuilder")
public class Message {
    private int chatId;
    private String date;
    private boolean type;
    private Order order;
    private String message;
    @Override
    public String toString()
    {
        return new GsonDateFormatGetter().getGson().toJson(this);
    }
}
