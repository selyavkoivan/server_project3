package server.Models;

import lombok.*;
import server.FactoryGson.GsonDateFormatGetter;

@Getter
@Setter
@Builder(builderMethodName = "adminBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends User {
    private int adminId;
    private String position;

    public Admin(int adminId, String position, User user) {
        super(user);
        this.adminId = adminId;
        this.position = position;
    }
    @Override
    public String toString()
    {
        return new GsonDateFormatGetter().getGson().toJson(this);
    }
}
