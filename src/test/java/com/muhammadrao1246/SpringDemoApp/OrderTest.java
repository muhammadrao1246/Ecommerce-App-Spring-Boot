package com.muhammadrao1246.SpringDemoApp;

import com.muhammadrao1246.SpringDemoApp.models.DTO.OrderProductDto;
import com.muhammadrao1246.SpringDemoApp.models.DTO.OrdersCreateDto;
import com.muhammadrao1246.SpringDemoApp.services.OrdersService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OrderTest {

    @Autowired
    private OrdersService ordersService;

    @Test
    @Commit
    public void testOrderCreation() {
        Set<OrderProductDto> products = new HashSet<>(Arrays.asList(
//                new OrderProductDto(UUID.fromString("2a1b8c7d-7c6f-4c9b-9a3f-6e1e2d3c4b5a"), 30),
                new OrderProductDto(UUID.fromString("4d5c6b7a-8e9f-4a1b-9c2d-3e4f5a6b7c8d"), 2),
                new OrderProductDto(UUID.fromString("9e0f1a2b-3c4d-4e5f-8a6b-7c8d9e0f1a2b"), 1)
//                ,
//                new OrderProductDto(UUID.fromString("2a1b327d-7c6f-4c9b-9a3f-6e1e2d3c4b5a"), 10)
        ));

        OrdersCreateDto dto = new OrdersCreateDto(products);

        ordersService.save(dto);
    }
}
