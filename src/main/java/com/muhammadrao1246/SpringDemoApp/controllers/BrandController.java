package com.muhammadrao1246.SpringDemoApp.controllers;

import com.muhammadrao1246.SpringDemoApp.models.Brand;
import com.muhammadrao1246.SpringDemoApp.models.Category;
import com.muhammadrao1246.SpringDemoApp.models.DTO.BrandDto;
import com.muhammadrao1246.SpringDemoApp.models.DTO.CategoryDto;
import com.muhammadrao1246.SpringDemoApp.services.BrandService;
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
@RequestMapping("/brand")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
class BrandController {
    private final BrandService service;

    @GetMapping("")
    public ResponseEntity<Page<Brand>> getAllBrands(@PageableDefault(size = 5, sort = "name", page = 0) Pageable pageable){
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Integer id){
        Brand brand = service.getById(id);
        if(brand == null) return ResponseEntity.notFound().location(URI.create("/brand/"+id)).build();
        return ResponseEntity.ok(brand);
    }

    @PostMapping()
    public ResponseEntity<Brand> createBrand(@RequestBody BrandDto dto){
        Brand brand = service.save(dto);
        System.out.println("Brand Created Successfully!");
        System.out.println(brand);
        if(brand.getId() == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return new ResponseEntity<>(brand, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody BrandDto dto){
        Brand brand = service.update(dto, id);
        System.out.println("Brand Updated Successfully!");
        System.out.println(brand);
        return ResponseEntity.noContent().location(URI.create("/brand/"+id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
