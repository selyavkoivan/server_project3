package server.Models;

import com.google.gson.Gson;


public class Admin extends User {
    private int adminId;
    private String position;

    public Admin(int adminId, String position, User user) {
        super(user);
        this.adminId = adminId;
        this.position = position;
    }


    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int id) {
        this.adminId = id;
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
        return new Gson().toJson(this);
    }
}
