package com.muhammadrao1246.SpringDemoApp.handlers;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiRenderer {
    private String message;
    private String status;
    private Object data;
    private Map<String, ArrayList<Object>> errors;

    @CreationTimestamp
    private String timestamp;
}
