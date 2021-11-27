package server.Models;

import com.google.gson.Gson;


public class Size {
    private int sizeId;
    private String size;
    private int count;



    public Size(int id, String size, int count) {
        this.sizeId = id;
        this.size = size;
        this.count = count;
    }

    public int getSizeId() {
        return sizeId;
    }
    public void setSizeId(int id) {
        this.sizeId = id;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
