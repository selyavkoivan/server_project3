package server.Models;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Order {

    private int orderId;
    private User user;
    private Product product;
    private int count;
    private Date date;

    public Order(int orderId, User user, Product product, int count, Date date) {
        this.orderId = orderId;
        this.user = user;
        this.product = product;
        this.count = count;
        this.date = date;
    }
    public Order()
    {
        user = new User();
        product = new Product();
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
