package com.muhammadrao1246.SpringDemoApp.models.Projections;


import java.io.Serializable;

/**
 * Projection for {@link com.muhammadrao1246.SpringDemoApp.models.Category}
 */
public record CategoryInfo(Integer id, String name)  implements Serializable {
}
