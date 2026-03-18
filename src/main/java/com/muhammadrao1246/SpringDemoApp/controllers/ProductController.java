package com.muhammadrao1246.SpringDemoApp.controllers;

import com.muhammadrao1246.SpringDemoApp.models.DTO.ProductSaveDto;
import com.muhammadrao1246.SpringDemoApp.models.Product;
import com.muhammadrao1246.SpringDemoApp.models.Projections.ProductInfo;
import com.muhammadrao1246.SpringDemoApp.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.UUID;

// controller is also a type of bean or managed class by spring through implementing IOC principle
//implementing using dependency injection
// Currently using Restcontroller the response should be converted to json format
// also to build rest api
@RestController
@CrossOrigin // allowing cross origin requests
@RequestMapping("/products")
public class ProductController {

    // Consuming another dependency or bean inside this controller bean
    // we need product service to perfrom actions on product data
    // can also use Autowiring
//    @Autowired(required = true)
    private final ProductService productService;

    // by using setter getter injection
//    @Autowired
//    public void setProductService(ProductService productService){
//        System.out.println("Setter Injection: ProductService is being injected!");
//        this.productService = productService;
//    }

    // using constructor injection - spring will inject it
    public ProductController(ProductService productService){
        System.out.println("Constructor Injection: ProductService is being injected!");
        this.productService = productService;
    }


    // creating routes or endpoints for api
    @RequestMapping()
    public ResponseEntity<Page<ProductInfo>> getAllProducts(@RequestParam(required = false, value = "") String searchQuery,
                                                        @PageableDefault(page=0, size=5, sort="name") Pageable pageable){
        return ResponseEntity.ok(productService.getAll(searchQuery, pageable));
    }

    // getting get method to get by id
    @GetMapping("/{id}")
    public ResponseEntity<ProductInfo> getProductById(@PathVariable UUID id){
        ProductInfo product = productService.getById(id);
        if(product == null) return ResponseEntity.notFound().location(URI.create("/api/products/"+id)).build();
        return ResponseEntity.ok(product);
    }

    // adding product
    @PostMapping()
    public ResponseEntity<ProductInfo> addProduct(@ModelAttribute ProductSaveDto dto){
        System.out.println(dto.getName() + " is being added!");
        ProductInfo created = productService.addProduct(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // updating product
    @PutMapping("/{id}")
    public ResponseEntity<ProductInfo> updateProduct(@PathVariable UUID id, @ModelAttribute ProductSaveDto dto){
        System.out.println(dto.getName() + " is being updated!");
        ProductInfo updated = productService.updateProduct(dto, id);
        return ResponseEntity.ok().body(updated);
    }

    // delete mapping
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable UUID id){
        productService.delete(id);
        return new ResponseEntity<>("Product Deleted!", HttpHeaders.EMPTY, 204);
    }

    // get image
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable UUID id){
        Product product = productService.getModelById(id);
        if(product == null) return ResponseEntity.notFound().location(URI.create("/api/products/"+id)).build();

        // want to give display link
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(product.getImageType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(product.getImageName()).build());
        return new ResponseEntity<>(product.getImageData(), headers, HttpStatus.OK);
    }

    // updating only image

    // get image
    @PostMapping("/{id}/image")
    public ResponseEntity<?> updateProductImage(@PathVariable UUID id, @RequestPart MultipartFile imageFile){
        // updating product image
        boolean isHappened = productService.updateProductImage(imageFile, id);
        return new ResponseEntity<>(isHappened ? "Image Uploaded Successfully!" : "Image Upload Failed!", HttpStatus.OK);
    }
}
