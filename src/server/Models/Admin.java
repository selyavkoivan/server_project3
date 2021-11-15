package server.Models;

import server.Enums.Const;

public class Admin {
    private int id;
    private String position;
    private User user;


    public Admin(int id, String position, User user) {
        this.id = id;
        this.position = position;
        this.user = user;
    }
    public Admin(String data)
    {
        var dataAdday = data.split(Const.b);
        System.out.println(dataAdday[0]);
        id = Integer.parseInt(dataAdday[0]);
        position = dataAdday[1];
        data = "";
        for (int i = 2; i < dataAdday.length; i++)
        {
            data += dataAdday[i] + Const.b;
        }

        user = new User(data);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return id + Const.b + position + Const.b + user.toString();
    }
}
