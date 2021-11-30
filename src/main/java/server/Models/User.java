package server.Models;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    protected int userId;
    protected String login;
    protected String name;
    protected String password;


    public User(int id, String login, String name, String password) {
        this.userId = id;
        this.login = login;
        this.name = name;
        this.password = password;
    }
    public User(User user) {
        this.userId = user.userId;
        this.login = user.login;
        this.name = user.name;
        this.password = user.password;
    }
    public User() {}
    @Override
    public String toString()
    {
            return new Gson().toJson(this);
    }
}
