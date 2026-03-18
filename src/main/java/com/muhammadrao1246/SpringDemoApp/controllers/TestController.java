package com.muhammadrao1246.SpringDemoApp.controllers;

import com.muhammadrao1246.SpringDemoApp.models.Order;
import com.muhammadrao1246.SpringDemoApp.models.Projections.OrderListInfo;
import com.muhammadrao1246.SpringDemoApp.models.Projections.ProductInfo;
import com.muhammadrao1246.SpringDemoApp.repositories.BrandRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.CategoryRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.OrdersRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.ProductRepository;
import com.muhammadrao1246.SpringDemoApp.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final ProductRepository productRepo;
    private final BrandRepository brandRepo;
    private final CategoryRepository categoryRepo;
    private final OrdersRepository ordersRepo;

    @GetMapping("/products")
    public Page<ProductInfo> testProductList(@RequestParam String search, @PageableDefault Pageable pageable){
        System.out.println("Using Specification Crieria api");

        return productRepo.findBy(
                (root, query, builder)->(
                    builder.or(
                            builder.like(builder.lower(root.get("name")), "%"+search+"%"),
                            builder.like(builder.lower(root.get("description")), "%"+search+"%")
                            )
                ), q -> q.as(ProductInfo.class).page(pageable));

//        System.out.println("Using Derived Query method");
//        return productRepo.findProductsByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(search, search, pageable);

//        System.out.println("Using JPQL Query");
//        return productRepo.searchProducts(search, pageable);
    }

    // <h1>test dynamic projections</h1>
    @GetMapping("/products/dynamic")
    public ResponseEntity<?> testProductsDynamicProjections(@RequestParam String key){
        return ResponseEntity.ok(switch (key) {
            case "orders" ->
                // fetching all orders of product
                productRepo.getAllOrdersByProductId(UUID.fromString("9e0f1a2b-3c4d-4e5f-8a6b-7c8d9e0f1a2b"), OrderListInfo.class);
            case "total_orders" ->
                productRepo.getTotalOrdersByProductId(UUID.fromString("9e0f1a2b-3c4d-4e5f-8a6b-7c8d9e0f1a2b"));
            case "product_by_category" ->
                    productRepo.findProductsByCategory_Id(1, ProductInfo.class);
            case "product_by_brand" ->
                    productRepo.findProductsByBrand_Id(1, ProductInfo.class);
            default -> new HashSet<>();
        });
    }

    @GetMapping("/products/{id}")
    public ProductInfo testProductById(@PathVariable UUID id){
        // using dynamic projection
        return productRepo.findProductById(id, ProductInfo.class).orElse(null); 
        // using derived query method with projection
//        return productRepo.findProductById(id
    }



}
