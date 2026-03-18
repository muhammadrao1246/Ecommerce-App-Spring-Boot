package com.muhammadrao1246.SpringDemoApp.repositories;

import com.muhammadrao1246.SpringDemoApp.models.Order;
import com.muhammadrao1246.SpringDemoApp.models.Projections.OrderDetailInfo;
import com.muhammadrao1246.SpringDemoApp.models.Projections.OrderListInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Order, UUID> {

    @Query("select o from Order o where o.user.id = :uuid")
    Page<OrderListInfo> findAllOrders(Pageable pageable, UUID uuid);

    @Query("select o from Order o where o.id = :id")
    OrderDetailInfo findOrderDetailInfoById(UUID id);
}