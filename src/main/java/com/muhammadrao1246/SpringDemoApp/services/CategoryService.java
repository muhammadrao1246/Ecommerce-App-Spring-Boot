package com.muhammadrao1246.SpringDemoApp.services;

import com.muhammadrao1246.SpringDemoApp.models.Brand;
import com.muhammadrao1246.SpringDemoApp.models.Category;
import com.muhammadrao1246.SpringDemoApp.models.DTO.BrandDto;
import com.muhammadrao1246.SpringDemoApp.models.DTO.CategoryDto;
import com.muhammadrao1246.SpringDemoApp.repositories.BrandRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service class responsible for handling operations related to the Category entity.
 * This class interacts with the CategoryRepository to perform CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    // using required args constructor or constructor injection
    private final CategoryRepository repo;

    public Page<Category> getAll(Pageable pageable){
        // fetching using repository with limited options
        return repo.findAll(pageable);
    }

    public Category getById(Integer id){
        return repo.findById(id).orElse(null);
    }

    public Category save(CategoryDto dto){
        Category category = new Category();
        category.setName(dto.getName());
        return repo.save(category);
    }

    @Transactional
    public Category update(CategoryDto dto, Integer id){
        Category category = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Category not found with id: "+id));
        category.setName(dto.getName());
        return category;
    }

    public void delete(Integer id){
        Category category = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Category not found with id: "+id));
        repo.delete(category);
    }


}
