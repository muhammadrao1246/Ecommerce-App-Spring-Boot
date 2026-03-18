package com.muhammadrao1246.SpringDemoApp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

// another class that serves as Composite Key
// serializeable interface tells JVM to ready to convert this class Object into byte stream for save/transfer or exchange
@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderProductKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID orderId;
    private UUID productId;
}

