package server.Enums;

import server.Models.User;

import java.util.List;

public class ListConvertor {
    public static String Users(List<User> users)
    {
        String usersStr = "";
        for (var user:
             users) {
            usersStr += user.toString() + Const.w;
        }
        return usersStr;
    }
}
