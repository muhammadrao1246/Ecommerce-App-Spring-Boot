package com.muhammadrao1246.SpringDemoApp.repositories;

import com.muhammadrao1246.SpringDemoApp.models.Product;
import com.muhammadrao1246.SpringDemoApp.models.Projections.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product>, QueryByExampleExecutor<Product> {
    // WHEN YOU DECLARE FUNCTION WITHO YOUR OWN PROPOSED NAME OR FEATURES YOU SHOULD USE @QUERY
    // FETCH NEEDED COLUMNS YOURSELF TO BE MAPPED ON TO THE PROJECTION
    // searching with @Query JQL or native sql
    @Query("select p.id as id, p.name as name, p.description as description, p.price as price, p.quantity as quantity, p.imageName as imageName, p.createdAt as createdAt, p.brand as brand, p.category as category  from Product p " +
            "where lower(p.name) like lower(concat('%', :query, '%'))" +
            " or lower(p.description) like lower(concat('%', :query, '%'))")
    Page<ProductInfo> searchProducts(@Param("query") String query, Pageable pageable);

    // can also use Derived Query method framework going to build query on itself
    Page<ProductInfo> findProductsByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name, String description, Pageable pageable);

    // projection mapping for Specification api not going to work like this ()
    // because Specification can only be used through service layer
//    @Query("select p.id as id, p.name as name, p.description as description, p.price as price, p.quantity as quantity, p.imageName as imageName, p.createdAt as createdAt, p.brand as brand, p.category as category  from Product p")
//    Page<ProductInfo> findAllProducts(Specification<Product> spec, Pageable pageable);

    // DYNAMIC PROJECTIONS FUNCTIONS BELOW
    // get collection of products paginated
    <T> Page<T> findProductsByNameContainsIgnoreCase(String name, Pageable pageable, Class<T> projection);

    <T> Collection<T> findProductsByCategory_Id(Integer categoryId, Class<T> projection);

    <T> Set<T> findProductsByBrand_Id(Integer brandId, Class<T> projection);

    // for single product
    <T> Optional<T> findProductById(UUID id, Class<T> projection);

    // get total orders of the product
    @Query("select count(op) from OrderProduct op where op.product.id = :id")
    Integer getTotalOrdersByProductId(@Param("id") UUID id);

    // getting orders of product
    @Query("select o from Order o join o.products p where p.product.id = CAST(:id as uuid) ")
    <T> Set<T> getAllOrdersByProductId(@Param("id") UUID id, Class<T> projection);

    // AGGREGATE OR JOIN CALLS BELOW
    // i want to know total_product_units_sold_count, average_price, max_price, min_price,
    // getting top sold products
    @Query("select p.id, p.name, p.description, p.price, p.quantity, avg(op.priceAtThatTime) as averagePrice, avg(op.priceAtThatTime*op.quantity) as averageSoldRevenue, sum(op.quantity) as totalQuantitySold, count(op.id) as totalOrders from Product p " +
            "inner join OrderProduct op ON op.product.id = p.id " +
            "group by p order by totalOrders desc, totalQuantitySold desc")
    <T> Set<T> getTopSoldProducts(Class<T> projection);

    // top sold in last month (can be in order repo also)
    @Query("select p.id, p.name, p.description, p.price, p.quantity, sum(op.quantity) as totalUnitsSold from Product p inner join OrderProduct op ON op.product.id = p.id " +
            "inner join Order o ON o.id = op.order.id " +
            "where o.createdAt >= date_trunc('month', current_date) " +
            "group by p.id " +
            "order by totalUnitsSold desc")
    <T> Set<T> getTopSoldProductsInLastMonth(Class<T> projection);


    // SINGLE INSTANCE CALLS BELOW

    // product with all its orders
    @Query("select p.id, p.name, op.order.id, op.order.status, op.order.totalAmount, op.quantity, op.priceAtThatTime from Product p right join OrderProduct op ON op.product.id = p.id where p.id = cast(:id as uuid)")
    <T> Set<T> getProductWithOrders(UUID id, Class<T> projection);

    // WHEN YOU DECLARE FUNCTION WITH YOUR OWN PROPOSED NAME OR FEATURES YOU SHOULD USE @QUERY
    // FETCH NEEDED COLUMNS YOURSELF TO BE MAPPED ON TO THE PROJECTION
//    @Query("select p.id as id, p.name as name, p.description as description, p.price as price, p.quantity as quantity, p.imageName as imageName, p.createdAt as createdAt, p.brand as brand, p.category as category from Product p " +
//            "where p.id = :id")
//    Optional<ProductInfo> findProductById(UUID id);

    ProductInfo findProductById(UUID id);
}
