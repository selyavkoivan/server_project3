package server.Models;

import lombok.*;
import server.FactoryGson.GsonGetter;

@Getter
@Setter
@Builder(builderMethodName = "materialBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    protected int materialId;
    protected String material;
    protected String color;
    protected String pattern;

    @Override
    public String toString()
    {
        return new GsonGetter().getGson().toJson(this);
    }
}
