package server.Models;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Size {
    private int sizeId;
    private String size;
    private int count;



    public Size(int id, String size, int count) {
        this.sizeId = id;
        this.size = size;
        this.count = count;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
