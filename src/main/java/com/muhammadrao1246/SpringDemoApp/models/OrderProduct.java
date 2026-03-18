package com.muhammadrao1246.SpringDemoApp.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProduct {
    @EmbeddedId
    private OrderProductKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name="fk_order_product_order_id"))
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name="fk_order_product_product_id"))
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "price_at_that_time")
    private BigDecimal priceAtThatTime;
}
