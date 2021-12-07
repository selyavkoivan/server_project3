package server.Models;

import lombok.*;
import server.FactoryGson.GsonGetter;

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

    public User(User user) {
        this.userId = user.userId;
        this.login = user.login;
        this.name = user.name;
        this.password = user.password;
    }
    @Override
    public String toString()
    {
            return new GsonGetter().getGson().toJson(this);
    }
}
