package server.Models;


import lombok.*;
import server.FactoryGson.GsonDateFormatGetter;

import java.util.Date;

@Getter
@Setter
@Builder(builderMethodName = "orderBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private int orderId;
    private User user;
    private Product product;
    private int count;
    private Date date;
    private boolean delivery;
    private String deliveryAddress;
    private Date deliveryDate;
    private int orderStatus;

    @Override
    public String toString()
    {
        return new GsonDateFormatGetter().getGson().toJson(this);
    }
}
