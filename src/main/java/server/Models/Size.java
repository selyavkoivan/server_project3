package server.Models;

import lombok.*;
import server.FactoryGson.GsonGetter;

@Getter
@Setter
@Builder(builderMethodName = "sizeBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class Size {
    private int sizeId;
    private String size;
    private int count;


    @Override
    public String toString()
    {
        return new GsonGetter().getGson().toJson(this);
    }
}
