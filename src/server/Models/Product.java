package server.Models;

import server.Enums.Const;

public class Product extends Material {
    private int id;
    private String name;
    private String description;
    private double price;
    private int count;
    private String size;






    public Product(String data)
    {
        super(data);
        var dataArray = data.split(Const.b);
        id = Integer.parseInt(dataArray[4]);
        name = dataArray[5];
        description = dataArray[6];
        price = Double.parseDouble(dataArray[7]);
        count = Integer.parseInt(dataArray[8]);
        size = dataArray[9];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    @Override
    public String toString()
    {
        return super.toString() + Const.b + id + Const.b + name +
                Const.b + description + Const.b + price + Const.b + count + Const.b + size;
    }
}
