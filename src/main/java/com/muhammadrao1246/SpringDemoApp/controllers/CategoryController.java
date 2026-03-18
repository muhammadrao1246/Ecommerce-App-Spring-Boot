package com.muhammadrao1246.SpringDemoApp.controllers;

import com.muhammadrao1246.SpringDemoApp.models.Category;
import com.muhammadrao1246.SpringDemoApp.models.DTO.CategoryDto;
import com.muhammadrao1246.SpringDemoApp.repositories.CategoryRepository;
import com.muhammadrao1246.SpringDemoApp.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
class CategoryController {
    private final CategoryService service;

    @GetMapping("")
    public ResponseEntity<Page<Category>> getAllCategories(@PageableDefault(size = 5, sort = "name", page = 0) Pageable pageable){
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id){
        Category category = service.getById(id);
        if(category == null) return ResponseEntity.notFound().location(URI.create("/category/"+id)).build();
        return ResponseEntity.ok(category);
    }

    @PostMapping()
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto dto){
        Category category = service.save(dto);
        System.out.println("Category Created Successfully!");
        System.out.println(category);
        if(category.getId() == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto dto){
        Category category = service.update(dto, id);
        System.out.println("Category Updated Successfully!");
        System.out.println(category);
        return ResponseEntity.noContent().location(URI.create("/category/"+id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
