import com.google.gson.Gson;
import org.junit.Test;
import server.Database.OrderManager;
import server.Database.ProductManager;
import server.Database.UserManager;
import server.FactoryGson.GsonDateFormatGetter;
import server.FactoryGson.GsonGetter;
import server.Models.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseTest {
    @Test
    public void testDatabaseAddProductWithoutSizes() throws SQLException {
        Product product = new Product(0, "Хлопок", "Зеленый", "Логотип", 0,
                "Converse Chuck Taylor High Top", "70 лучше", 180, "Кеды");
        ProductManager.getDatabaseManager().addProduct(product.toString());
    }

    @Test
    public void testDatabaseAddProduct() throws SQLException {
        List<Size> sizes = new ArrayList<>();
        sizes.add(new Size(0, "23", 32));
        sizes.add(new Size(0, "43", 12));
        Product product = new Product(0, "Хлопок", "Коричневые", "Логотип", 0,
                "Converse Chuck Taylor High Top", "70 лучше", 180, "Кеды", sizes);
        ProductManager.getDatabaseManager().addProduct(product.toString());
    }

    @Test
    public void testDatabaseEditProduct() throws SQLException {
        List<Size> sizes = new ArrayList<>();
        sizes.add(new Size(0, "23", 32));
        sizes.add(new Size(0, "2", 12));
        sizes.add(new Size(0, "43", 12));
        Product product = new Product(0, "Хлопок", "Красный", "Логотип", 4,
                "Converse Chuck Taylor Low Top", "70 лучше", 180, "Кеды", sizes);
        ProductManager.getDatabaseManager().editProduct(product.toString());
    }

    @Test
    public void testDatabaseDeleteProduct() throws SQLException {
        Product product = Product.productBuilder().productId(23).build();
        ProductManager.getDatabaseManager().deleteProduct(product.toString());
    }

    @Test
    public void testDatabaseShowUsers() throws SQLException {
        System.out.println(new Gson().toJson(UserManager.getDatabaseManager().showUsers()));
    }
    @Test
    public void testDatabaseShowOrders() throws SQLException {
        System.out.println(new Gson().toJson(OrderManager.getDatabaseManager().showOrders()));
    }
    @Test
    public void testDatabaseCreateOrder() throws SQLException {

        Order order = Order.orderBuilder().count(1).date(new Date()).delivery(false).user(User.userBuilder().userId(5).build()).
                product(Product.productBuilder().sizes(new ArrayList<>()).build()).build();
        order.getProduct().addSize(Size.sizeBuilder().sizeId(4).build());
        OrderManager.getDatabaseManager().createOrder(new GsonDateFormatGetter().getGson().toJson(order));
    }
    @Test
    public void testDatabaseDeleteOrder() throws SQLException {
        Order order = new Order();
        order.setOrderId(11);
        OrderManager.getDatabaseManager().deleteOrder(new Gson().toJson(order));
    }
    @Test
    public void testDatabaseAddCard() throws SQLException {
        User user = User.userBuilder().userId(23).card(new PaymentCard(1, "1234567812345678", 332, new Date())).build();
        UserManager.getDatabaseManager().AddCard(new GsonDateFormatGetter().getGson().toJson(user));
    }
    @Test
    public void testDatabaseDeleteCard() throws SQLException {
        User user = User.userBuilder().userId(23).build();
        UserManager.getDatabaseManager().DeleteCard(new GsonDateFormatGetter().getGson().toJson(user));
    }
    @Test
    public void testDatabaseProductPopularity() throws SQLException{
        System.out.println(new GsonGetter().getGson().toJson(ProductManager.getDatabaseManager().ShowGoodsPopularity()));
    }
}
