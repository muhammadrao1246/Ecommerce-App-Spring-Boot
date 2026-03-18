package com.muhammadrao1246.SpringDemoApp;

import com.muhammadrao1246.SpringDemoApp.models.Brand;
import com.muhammadrao1246.SpringDemoApp.models.Category;
import com.muhammadrao1246.SpringDemoApp.models.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    @Commit
    public void testProductCreation() {
        Brand brand = entityManager.find(Brand.class, 2);
        Category category = entityManager.find(Category.class, 4);

        assertNotNull(brand, "Expected Brand with id=2 to exist for this test");
        assertNotNull(category, "Expected Category with id=4 to exist for this test");

        Product product = Product.builder()
                .name("Heavy product")
                .description("This is a very heavy product")
                .price(BigDecimal.valueOf(2_000_000))
                .quantity(BigDecimal.valueOf(213))
                .brand(brand)
                .category(category)
                .build();

        entityManager.persist(product);
        entityManager.flush();

        assertNotNull(product.getId(), "Product id should be generated after persist/flush");
    }
}
