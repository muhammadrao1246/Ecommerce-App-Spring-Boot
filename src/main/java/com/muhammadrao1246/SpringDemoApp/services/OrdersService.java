package com.muhammadrao1246.SpringDemoApp.services;

import com.muhammadrao1246.SpringDemoApp.models.*;
import com.muhammadrao1246.SpringDemoApp.models.DTO.OrderProductDto;
import com.muhammadrao1246.SpringDemoApp.models.DTO.OrdersCreateDto;
import com.muhammadrao1246.SpringDemoApp.models.Projections.OrderDetailInfo;
import com.muhammadrao1246.SpringDemoApp.models.Projections.OrderListInfo;
import com.muhammadrao1246.SpringDemoApp.repositories.OrdersRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;


/**
 * Service class responsible for handling operations related to the Orders entity.
 * This class interacts with the OrdersRepository to perform CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class OrdersService {
    // using required args constructor or constructor injection
    private final OrdersRepository repo;

    private final ProductRepository productRepo;


    @PreAuthorize("hasAuthority('ORDERS_READ')")
    public Page<OrderListInfo> getAll(Pageable pageable, User user){
        // fetching using repository with limited options
        return repo.findAllOrders(pageable, user.getId());
    }

    // method based authorization
    // or Method Level Access Control
    // we already implemented RBAC or Role Based Access Control
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ORDERS_READ')")
    // Only the creator of order can see its detail as per my exameple
    @PostAuthorize("returnObject != null && returnObject.user.id.toString() == authentication.principal.id.toString()")
    public OrderDetailInfo getById(UUID id){

        Order order = repo.findById(id).orElseThrow(()->new EntityNotFoundException("Order not found with id: "+id));

        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(u);
        System.out.println(order.getUser().getId());
        System.out.println(order.getUser().getId().equals(u.getId()));
        return repo.findOrderDetailInfoById(order.getId());
    }

    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public Order save(OrdersCreateDto dto){
        // OrderProductDto
        Set<OrderProductDto> orderProductDtoList = dto.getProducts();

        // getting all products from Product tables
        // preparing query which gets all provided products that have their quantity above the quantity provided
        // using specification Critera JPA API
        Specification<Product> spec = Specification.unrestricted();
        spec = spec.or((r, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for(OrderProductDto orderProduct : orderProductDtoList) {
                predicates.add(cb.and(
                        cb.equal(r.get("id"), orderProduct.getProductId()),
                        cb.greaterThanOrEqualTo(r.get("quantity"), orderProduct.getQuantity())
                ));
            }
            return cb.or(predicates);
        });
        List<Product> products = productRepo.findAll(spec);

        // if product count not equal that means data provided by user is not valid
        if(products.size() != orderProductDtoList.size()) throw new IllegalArgumentException("Invalid Product Ids or Quantity");

        // creating Order Object first
        Order order = new Order();

        // creating a hashmap of product quantity
        Map<UUID, Integer> productQuantityMap = orderProductDtoList.stream().collect(
                Collectors.toMap(OrderProductDto::getProductId, OrderProductDto::getQuantity)
        );

        // we have to construct OrderProducts also
        for(Product product : products){
            // getting quantity
            int quantity = productQuantityMap.get(product.getId());
            OrderProduct orderProductEntry = OrderProduct.builder()
                    .id(new OrderProductKey(order.getId(), product.getId()))
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .priceAtThatTime(product.getPrice())
                    .build();
            order.getProducts().add(orderProductEntry);

            // we should also dirty product quantity
            product.setQuantity(BigDecimal.valueOf(product.getQuantity().intValue() - quantity));

            // saving total amount
            order.setTotalAmount(order.getTotalAmount() + (product.getPrice().intValue()*quantity));
        }

        // setting the current user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        order.setUser(user);

        order = repo.save(order); // forcing this insert now so we can add its child later
        return order;
    }

    @Transactional
//    Defending against Insecure Direct Object Reference vulnerabilities when dealing with data that needs to be fetched first.
    // customer can only cancel his own order or the admin can
    @PostAuthorize("hasAnyRoles('ADMIN') or (hasAuthority('ORDERS_WRITE') and returnObject.user.id == authentication.getPrincipal().id)")
    public Order cancel(UUID orderId){
        Order order = repo.findById(orderId).orElseThrow(()-> new EntityNotFoundException("Order not found with id: "+orderId));
        // cancel order
        // CAN HAVE MORE STEPS - CAN CALL ANOTHER SERVICE FOR THIS ACTION
        order.setStatus("CANCELLED");
        return order;
    }

//    @Transactional
//    public Orders update(BrandDto dto, UUID id){
//        Orders category = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Category not found with id: "+id));
//        category.setName(dto.getName());
//        return category;
//    }

    // only admin can perform deletion
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id){
        Order order = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Category not found with id: "+id));
        repo.delete(order);
    }


}
