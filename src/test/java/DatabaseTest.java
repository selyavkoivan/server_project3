import org.junit.Test;
import server.Database;
import server.Models.Product;
import server.Models.Size;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTest {
    @Test
    public void testDatabaseAddProductWithoutSizes() {
        Product product = new Product(0, "Хлопок", "Зеленый", "Логотип", 0,
                "Converse Chuck Taylor High Top", "70 лучше", 180, "Кеды");
        Database.getDatabase().addProduct(product.toString());
    }
    @Test
    public void testDatabaseAddProduct() {
        List<Size> sizes = new ArrayList<>();
        sizes.add(new Size(0, "23", 32));
        sizes.add(new Size(0, "43", 12));
        Product product = new Product(0, "Хлопок", "Коричневые", "Логотип", 0,
                "Converse Chuck Taylor High Top", "70 лучше", 180, "Кеды", sizes);
        Database.getDatabase().addProduct(product.toString());
    }
    @Test
    public void testDatabaseEditProduct()
    {
        List<Size> sizes = new ArrayList<>();
        sizes.add(new Size(0, "23", 32));
        sizes.add(new Size(0, "2", 12));
        sizes.add(new Size(0, "43", 12));
        Product product = new Product(0, "Хлопок", "Красный", "Логотип", 4,
                "Converse Chuck Taylor Low Top", "70 лучше", 180, "Кеды", sizes);
        Database.getDatabase().editProduct(product.toString());
    }
    @Test
    public void testDatabaseDeleteProduct()
    {
        Product product = new Product(0, "", "", "", 12,
                "", "", 180, "");
        Database.getDatabase().deleteProduct(product.toString());
    }
}
