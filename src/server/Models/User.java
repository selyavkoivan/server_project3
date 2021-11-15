package server.Models;

import server.Enums.Const;

public class User {
    private int id;
    private String login;
    private String name;
    private String password;


    public User(int id, String login, String name, String password) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.password = password;
    }
    public User(String data)
    {
        var dataArray = data.split(Const.b);
        id = Integer.parseInt(dataArray[0]);
        login = dataArray[1];
        name = dataArray[2];
        password = dataArray[3];

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
            return id + Const.b + login + Const.b + name + Const.b + password;
    }
}
