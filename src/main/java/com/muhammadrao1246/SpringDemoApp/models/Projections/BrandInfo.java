package com.muhammadrao1246.SpringDemoApp.models.Projections;


import java.io.Serializable;

/**
 * Projection for {@link com.muhammadrao1246.SpringDemoApp.models.Brand}
 */
public record BrandInfo(Integer id, String name)  implements Serializable {
}
