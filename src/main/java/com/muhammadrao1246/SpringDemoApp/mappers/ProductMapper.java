package com.muhammadrao1246.SpringDemoApp.mappers;

import com.muhammadrao1246.SpringDemoApp.models.Brand;
import com.muhammadrao1246.SpringDemoApp.models.Category;
import com.muhammadrao1246.SpringDemoApp.models.DTO.ProductSaveDto;
import com.muhammadrao1246.SpringDemoApp.models.Product;
import com.muhammadrao1246.SpringDemoApp.models.Projections.BrandInfo;
import com.muhammadrao1246.SpringDemoApp.models.Projections.CategoryInfo;
import com.muhammadrao1246.SpringDemoApp.models.Projections.ProductInfo;
import com.muhammadrao1246.SpringDemoApp.repositories.BrandRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public class ProductMapper {
    public static boolean updateProductImage(MultipartFile image, Product product) {
        if (image == null) return false;
        try {
            product.setImageType(image.getContentType());
            product.setImageName(image.getOriginalFilename());
            product.setImageData(image.getBytes());
            return true;
        } catch (IOException e) {
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
        Category category = categoryRepo.findById(dto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + dto.getCategoryId()));
        product.setCategory(category);

        // setting image if available
        ProductMapper.updateProductImage(dto.getImage(), product);
    }

    public static ProductInfo fromProductToDto(Product product) {
        return new ProductInfo(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getQuantity(), product.getCreatedAt(), new BrandInfo(product.getBrand().getId(), product.getBrand().getName()), new CategoryInfo(product.getCategory().getId(), product.getCategory().getName()));
    }
}
