package server.Models;

import com.google.gson.Gson;


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

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int id) {
        this.materialId = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
