package com.telusko.ecom.project.Controller;

import com.telusko.ecom.project.Model.Product;
import com.telusko.ecom.project.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
//to avoid crossorigin error like port 8080 to other post of front end
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    ProductService productService;
    ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @RequestMapping("/")
//    public String greet() {
//        return "hello world";
//    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }
    
    @GetMapping("/product/{productId}")
//    while returing we need send the status code so we use response entity
    public ResponseEntity<Product> getProduct(@PathVariable int productId) {
        Product product = productService.getProduct(productId);
        if(product != null)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) {
        try {
            Product product1 = productService.addProduct(product,imageFile);
            return new ResponseEntity<>(product1 , HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    this is for ui to show the image
    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {
        Product product = productService.getProduct(productId);
        byte[] imageFile = product.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType()))
            .body(imageFile);
    }

    @PutMapping("product/{productId}")
    public ResponseEntity<?> updateProductById(@PathVariable int productId,
                                               @RequestPart Product product,
                                               @RequestPart MultipartFile imageFile) {
        Product product1 = null;
        try {
            product1 = productService.updateProductById(productId, product, imageFile);
            return new ResponseEntity<>(product1 , HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("product/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable int productId) {
        if(productService.deleteProductById(productId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return new ResponseEntity<>(products , HttpStatus.OK);
    }
}
