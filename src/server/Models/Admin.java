package server.Models;

import server.Enums.Const;

public class Admin extends User {
    private int id;
    private String position;



    public Admin(int id, String position, User user) {
        super(user.toString());

        this.id = id;
        this.position = position;

    }
    public Admin(String data)
    {
        super(data);

        var dataArray = data.split(Const.b);

        id = Integer.parseInt(dataArray[4]);
        position = dataArray[5];

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




    @Override
    public String toString()
    {
        return super.toString() + Const.b + id + Const.b + position;
    }
}
