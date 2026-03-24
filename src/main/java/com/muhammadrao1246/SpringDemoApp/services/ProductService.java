package com.muhammadrao1246.SpringDemoApp.services;


import com.muhammadrao1246.SpringDemoApp.mappers.ProductMapper;
import com.muhammadrao1246.SpringDemoApp.models.DTO.ProductSaveDto;
import com.muhammadrao1246.SpringDemoApp.models.Product;
import com.muhammadrao1246.SpringDemoApp.models.Projections.CachePage;
import com.muhammadrao1246.SpringDemoApp.models.Projections.ProductInfo;
import com.muhammadrao1246.SpringDemoApp.repositories.BrandRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.CategoryRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// There are different annotations to inject beans or di
// one is @service annotation
// its better to know the purpose of the class created as bean thats why
@Service
@RequiredArgsConstructor
public class ProductService {

    // injecting Product Repo into service to get dynamic data
    private final ProductRepository repo;

    private final CategoryRepository categoryRepo;
    private final BrandRepository brandRepo;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String CACHE_NAMES = "products";

    // using this notation so AOP can intercept cache this function's response then send it fast
    @Cacheable(cacheNames = CACHE_NAMES, key = "'query '+#searchQuery+'&pageInfo '+#pageable.pageNumber")
    public Page<ProductInfo> getAll(String searchQuery, Pageable pageable){

        String key = CACHE_NAMES+"::page "+pageable.getPageNumber()+"&query "+searchQuery;
        CachePage<ProductInfo> cachedContent = (CachePage<ProductInfo>) redisTemplate.opsForValue().get(key);

        // if cache hit then send
        if(cachedContent != null){
            return new PageImpl<>(cachedContent.items(), pageable, cachedContent.items().size());
        }

        // return object going to be
        Page<ProductInfo> result = null;

        // 3 QUERY METHODS BELOW

        // 1. JPA Criteria API (Specifications)
        // Specification<Product> spec = Specification.unrestricted();
        // spec = spec.or((root, query, criteriaBuilder) ->
        //    criteriaBuilder.like(root.get("name"), "%"+searchQuery+"%")
        // ).or((root, query, criteriaBuilder) ->
        //         criteriaBuilder.like(root.get("description"), "%"+searchQuery+"%")
        // );
        // result = repo.findBy(spec, q -> q.as(ProductInfo.class).page(pageable));
        // System.out.println("Using JPQL Query with Specification as Parameter: "+result.getContent().size());


        // 3. Derived Query method
        // result = repo.findProductsByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(searchQuery, searchQuery, pageable);
        // System.out.println("Using Derived Query method: "+result.getContent().size());

        // 2. Using JPQL Query Simple
        result = repo.searchProducts(searchQuery, pageable);
        System.out.println("Using JPQL Query Simple: "+repo.searchProducts(searchQuery, pageable).getContent().size());

        // we cannot cache pageImpl cause it does not have constructor
        // so just cache the content
        // AFTER handling caching
        // if cache miss then fetch, save and send
        CachePage<ProductInfo> toCache = new CachePage<>(result.getContent());
        redisTemplate.opsForValue().set(key, toCache, Duration.ofSeconds(60));

        return result;
    }

    @Cacheable(cacheNames = CACHE_NAMES, key = "#id")
    public ProductInfo getById(UUID id){
//        return repo.findProductById(id, ProductInfo.class).orElse(null);
        return repo.findProductById(id).orElse(null);
    }

    // tracking newly added product in db by caching it
    @CachePut(cacheNames = CACHE_NAMES, key = "#result.id")
    public ProductInfo addProduct(ProductSaveDto dto){
        Product product = new Product();
        ProductMapper.fromDTOToProduct(brandRepo, categoryRepo, dto, product);
        // save the product cause image can be uploaded later
        product = repo.save(product);
        System.out.println(product);
        return ProductMapper.fromProductToDto(product);
    }

    // all db operations going to be in-persistence context whenever changes happen in entity will relfected back according cascade types
    @Transactional
    // we have to use CachePut to update previously red product from cache
    @CachePut(cacheNames = CACHE_NAMES, key = "#id")
    public ProductInfo updateProduct(ProductSaveDto dto, UUID id){
        System.out.println(id);
        Product product = repo.findById(id).orElseThrow(()->new EntityNotFoundException("Product not found with id: "+id.toString()));
        System.out.println(product);
        ProductMapper.fromDTOToProduct(brandRepo, categoryRepo, dto, product);
        repo.save(product);
        return ProductMapper.fromProductToDto(product);
    }

    // we need to remove cached element if it is deleted
    @CacheEvict(cacheNames = CACHE_NAMES, key = "#id")
    public void delete(UUID id) {
        repo.deleteById(id);
    }

    @Transactional
    public boolean updateProductImage(MultipartFile image, UUID id){
        Product product = repo.findById(id).orElseThrow(()->new EntityNotFoundException("Product not found with id: "+id.toString()));
        return ProductMapper.updateProductImage(image, product);
    }

    public Product getModelById(UUID id) {
        return repo.findById(id).orElse(null);
    }
}
