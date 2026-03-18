package com.muhammadrao1246.SpringDemoApp.mappers;

import com.muhammadrao1246.SpringDemoApp.models.Brand;
import com.muhammadrao1246.SpringDemoApp.models.Category;
import com.muhammadrao1246.SpringDemoApp.models.DTO.ProductSaveDto;
import com.muhammadrao1246.SpringDemoApp.models.Product;
import com.muhammadrao1246.SpringDemoApp.models.Projections.ProductInfo;
import com.muhammadrao1246.SpringDemoApp.repositories.BrandRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class ProductMapper {
    public static boolean updateProductImage(MultipartFile image, Product product){
        if(image == null) return false;
        try {
            product.setImageType(image.getContentType());
            product.setImageName(image.getOriginalFilename());
            product.setImageData(image.getBytes());
            return true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Mappers
    public static void fromDTOToProduct(BrandRepository brandRepo, CategoryRepository categoryRepo, ProductSaveDto dto, Product product) {
        // only set if they are present in dto
        product.setName(Optional.ofNullable(dto.getName()).orElse(product.getName()));
        product.setDescription(Optional.ofNullable(dto.getDescription()).orElse(product.getDescription()));
        product.setPrice(Optional.ofNullable(dto.getPrice()).orElse(product.getPrice()));
        product.setQuantity(Optional.ofNullable(dto.getQuantity()).orElse(product.getQuantity()));

        // fetch brand and set if the brand is changed from the previous one then JPA will going to auto update
        Brand brand = brandRepo.findById(dto.getBrandId()).orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + dto.getBrandId()));
        product.setBrand(brand);

        // same for category
        Category category = categoryRepo.findById(dto.getCategoryId()).orElseThrow(()->new EntityNotFoundException("Category not found with id: "+dto.getCategoryId()));
        product.setCategory(category);

        // setting image if available
        ProductMapper.updateProductImage(dto.getImage(), product);
    }

    public static ProductInfo fromProductToDto(Product product){
        return new ProductInfo() {
            @Override
            public UUID getId() {
                return product.getId();
            }

            @Override
            public String getName() {
                return product.getName();
            }

            @Override
            public String getDescription() {
                return product.getDescription();
            }

            @Override
            public BigDecimal getPrice() {
                return product.getPrice();
            }

            @Override
            public BigDecimal getQuantity() {
                return product.getQuantity();
            }

            @Override
            public Instant getCreatedAt() {
                return product.getCreatedAt();
            }

//            @Override
//            public String getImageUrl() {
//                return product.getImageName() != null ? "/products/"+product.getId()+"/image" : null;
//            }
//
//            @Override
//            public String getImageName() {
//                return product.getImageName();
//            }

            @Override
            public BrandInfo getBrand() {
                return new BrandInfo() {
                    @Override
                    public Integer getId() {
                        return product.getBrand().getId();
                    }

                    @Override
                    public String getName() {
                        return product.getBrand().getName();
                    }
                };
            }

            @Override
            public CategoryInfo getCategory() {
                return new CategoryInfo() {
                    @Override
                    public Integer getId() {
                        return product.getCategory().getId();
                    }

                    @Override
                    public String getName() {
                        return product.getCategory().getName();
                    }
                };
            }
        };
    }
}
