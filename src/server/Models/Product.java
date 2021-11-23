package server.Models;

import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

public class Product extends Material  {
    private int productId;
    private String name;
    private String description;
    private double price;
    private String type;
    private List<Size> sizes;

    public Product(int materialId, String materialName, String color, String pattern, int productId, String name, String description, double price, String type) {
        super(materialId, materialName, color, pattern);
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.sizes = new ArrayList<>();
    }
    public Product(int materialId, String materialName, String color, String pattern, int productId, String name, String description, double price, String type, List<Size> sizes) {
        super(materialId, materialName, color, pattern);
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.sizes = sizes;
    }




    public int getProductId() {
        return productId;
    }

    public void setProductId(int id) {
        this.productId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

    public void addSize(Size size)
    {
       sizes.add(size);
    }
}
