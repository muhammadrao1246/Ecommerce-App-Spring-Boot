package com.muhammadrao1246.SpringDemoApp.controllers;

import com.muhammadrao1246.SpringDemoApp.models.DTO.OrdersCreateDto;
import com.muhammadrao1246.SpringDemoApp.models.Order;
import com.muhammadrao1246.SpringDemoApp.models.Projections.OrderDetailInfo;
import com.muhammadrao1246.SpringDemoApp.models.Projections.OrderListInfo;
import com.muhammadrao1246.SpringDemoApp.models.User;
import com.muhammadrao1246.SpringDemoApp.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    // will be injected through required args constructor declaration
    private final OrdersService ordersService;

    @GetMapping("")
    public ResponseEntity<Page<OrderListInfo>> getAllOrders(@AuthenticationPrincipal User user, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC, page = 0) Pageable page){
        // getting all orders
        return ResponseEntity.ok(ordersService.getAll(page, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailInfo> getAllOrders(@PathVariable UUID id){
        return ResponseEntity.ok(ordersService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<Order> createOrder(@RequestBody OrdersCreateDto dto){
        return new ResponseEntity<>(ordersService.save(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable UUID id){
        // order going to reside in db but will be marked as cancelled
        ordersService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID id){
        ordersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
