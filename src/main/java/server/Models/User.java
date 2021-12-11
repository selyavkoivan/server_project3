package server.Models;

import lombok.*;
import server.FactoryGson.GsonDateFormatGetter;

@Getter
@Setter
@Builder(builderMethodName = "userBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    protected int userId;
    protected String login;
    protected String name;
    protected String password;
    protected PaymentCard card;
    protected boolean status;

    public User(User user) {
        this.userId = user.userId;
        this.login = user.login;
        this.name = user.name;
        this.password = user.password;
        card = user.card;
        status = user.status;
    }
    @Override
    public String toString()
    {
            return new GsonDateFormatGetter().getGson().toJson(this);
    }
}
