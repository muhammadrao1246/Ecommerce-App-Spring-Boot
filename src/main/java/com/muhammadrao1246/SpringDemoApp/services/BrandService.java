package com.muhammadrao1246.SpringDemoApp.services;

import com.muhammadrao1246.SpringDemoApp.models.Brand;
import com.muhammadrao1246.SpringDemoApp.models.DTO.BrandDto;
import com.muhammadrao1246.SpringDemoApp.repositories.BrandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service class responsible for handling operations related to the Brand entity.
 * This class interacts with the BrandRepository to perform CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class BrandService {
    // using required args constructor or constructor injection
    private final BrandRepository repo;

    public Page<Brand> getAll(Pageable pageable){
        // fetching using repository with limited options
        return repo.findAll(pageable);
    }

    public Brand getById(Integer id){
        return repo.findById(id).orElse(null);
    }

    public Brand save(BrandDto dto){
        Brand brand = new Brand();
        brand.setName(dto.getName());

        return repo.save(brand);
    }

    @Transactional
    public Brand update(BrandDto dto, Integer id){
        Brand brand = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Brand not found with id: "+id));
        brand.setName(dto.getName());
        return brand;
    }

    public void delete(Integer id){
        Brand brand = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Brand not found with id: "+id));
        repo.delete(brand);
    }


}
