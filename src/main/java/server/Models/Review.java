package server.Models;

import lombok.*;
import server.FactoryGson.GsonGetter;

@Getter
@Setter
@Builder(builderMethodName = "reviewBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private int reviewId;
    private int rate;
    private double averageRate;
    private User user;
    private Product product;

    @Override
    public String toString()
    {
        return new GsonGetter().getGson().toJson(this);
    }
}
