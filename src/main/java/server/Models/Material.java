package server.Models;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Material {
    protected int materialId;
    protected String material;
    protected String color;
        protected String pattern;


    public Material(int materialId, String materialName, String color, String pattern) {
        this.materialId = materialId;
        this.material = materialName;
        this.color = color;
        this.pattern = pattern;
    }

    public Material() {}
    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
