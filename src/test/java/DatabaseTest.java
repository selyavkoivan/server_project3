import com.google.gson.Gson;
import org.junit.Test;
import server.Database.DatabaseManager;
import server.Models.Order;
import server.Models.Product;
import server.Models.Size;
import server.Models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseTest {
    @Test
    public void testDatabaseAddProductWithoutSizes() {
        Product product = new Product(0, "Хлопок", "Зеленый", "Логотип", 0,
                "Converse Chuck Taylor High Top", "70 лучше", 180, "Кеды");
        DatabaseManager.getDatabase().addProduct(product.toString());
    }

    @Test
    public void testDatabaseAddProduct() {
        List<Size> sizes = new ArrayList<>();
        sizes.add(new Size(0, "23", 32));
        sizes.add(new Size(0, "43", 12));
        Product product = new Product(0, "Хлопок", "Коричневые", "Логотип", 0,
                "Converse Chuck Taylor High Top", "70 лучше", 180, "Кеды", sizes);
        DatabaseManager.getDatabase().addProduct(product.toString());
    }

    @Test
    public void testDatabaseEditProduct() {
        List<Size> sizes = new ArrayList<>();
        sizes.add(new Size(0, "23", 32));
        sizes.add(new Size(0, "2", 12));
        sizes.add(new Size(0, "43", 12));
        Product product = new Product(0, "Хлопок", "Красный", "Логотип", 4,
                "Converse Chuck Taylor Low Top", "70 лучше", 180, "Кеды", sizes);
        DatabaseManager.getDatabase().editProduct(product.toString());
    }

    @Test
    public void testDatabaseDeleteProduct() {
        Product product = new Product(){{
            setProductId(12);
        }};
        DatabaseManager.getDatabase().deleteProduct(product.toString());
    }

    @Test
    public void testDatabaseShowOrders()
    {
        System.out.println(new Gson().toJson(DatabaseManager.getDatabase().showOrders()));
    }
    @Test
    public void testDatabaseShowUsers()
    {
        System.out.println(new Gson().toJson(DatabaseManager.getDatabase().showUsers()));
    }
    @Test
    public void testDatabaseCreateOrder()
    {
        Order order = new Order();
        order.setCount(1);
        order.setDate(new Date());
        User user = new User();
        user.setUserId(5);
        order.setUser(user);
        order.setProduct(new Product());
        order.getProduct().addSize(new Size(4, "2", 2));
        DatabaseManager.getDatabase().createOrder(new Gson().toJson(order));
    }
    @Test
    public void testDatabaseDeleteOrder()
    {
        Order order = new Order();
        order.setOrderId(11);
        DatabaseManager.getDatabase().deleteOrder(new Gson().toJson(order));
    }
}
