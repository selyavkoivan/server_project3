package server.Models;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends User {
    private int adminId;
    private String position;

    public Admin(int adminId, String position, User user) {
        super(user);
        this.adminId = adminId;
        this.position = position;
    }
    public Admin() {
        super();
    }
    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
