package server.Models;

import com.google.gson.Gson;


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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString()
    {
            return new Gson().toJson(this);
    }
}
