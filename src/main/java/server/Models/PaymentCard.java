package server.Models;

import lombok.*;
import server.FactoryGson.GsonDateFormatGetter;

import java.util.Date;

@Getter
@Setter
@Builder(builderMethodName = "cardBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCard {

    private int PaymentCardId;
    private String CardNumber;
    private int CVV;
    private Date expityDate;

    @Override
    public String toString()
    {
        return new GsonDateFormatGetter().getGson().toJson(this);
    }
}
