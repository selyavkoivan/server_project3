package server.Models;

import server.Enums.Const;

public class Material {
    protected int id;
    protected String material;
    protected String color;
    protected String pattern;


    public Material(String data)
    {
        var dataArray = data.split(Const.b);
        id = Integer.parseInt(dataArray[0]);
        material = dataArray[1];
        color = dataArray[2];
        pattern = dataArray[3];
    }
    public Material()
    {
        id = 0;
        material = "0";
        color = "0";
        pattern = "0";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return id + Const.b + material + Const.b + color + Const.b + pattern;
    }
}
