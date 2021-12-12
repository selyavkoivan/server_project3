package server.Models;

import com.google.gson.Gson;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(builderMethodName = "productBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Product extends Material {
    private int productId;
    private String name;
    private String description;
    private double price;
    private String type;
    private int popularity;
    private List<Size> sizes;

    public Product(int materialId, String materialName, String color, String pattern, int productId, String name, String description, double price, String type, int popularity) {
        super(materialId, materialName, color, pattern);
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.popularity = popularity;
        this.sizes = new ArrayList<>();
    }
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
        this.sizes = new ArrayList<>();

        this.sizes.addAll(sizes);

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
