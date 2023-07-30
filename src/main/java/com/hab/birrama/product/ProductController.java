package com.hab.birrama.product;

import com.hab.birrama.members.Member;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("{productId}")
    public Product getProduct(@PathVariable("productId") Integer id){
        return productService.getProduct(id);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping
    public void addProduct(@RequestBody Product product){
        productService.addProduct(product);
    }

    @PutMapping("{productId}")
    public void updateProduct(
            @PathVariable("productId") Integer id,
            @RequestBody Product product){

        productService.updateProduct(id, product);
    }

    @DeleteMapping("{productId}")
    public void deleteProduct(
            @PathVariable("productId") Integer id
    ){
        productService.deleteProduct(id);
    }


}
