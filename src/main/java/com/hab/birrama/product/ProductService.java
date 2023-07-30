package com.hab.birrama.product;

import com.hab.birrama.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void addProduct(Product product) {
        product.setTimestamp(new Timestamp(System.currentTimeMillis()));
        productRepository.save(product);
    }

    public void updateProduct(Integer productId, Product request) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product updateProduct = product.get();

            updateProduct.setProductName(request.getProductName());
            updateProduct.setEmoji(request.getEmoji());
            updateProduct.setCategory(request.getCategory());
            updateProduct.setUnit(request.getUnit());
            updateProduct.setStatus(request.getStatus());
            updateProduct.setTimestamp(new Timestamp(System.currentTimeMillis()));
            updateProduct.setUnitBuyingPrice(request.getUnitBuyingPrice());
            updateProduct.setUnitSellingPrice(request.getUnitSellingPrice());

            productRepository.save(updateProduct);
        } else {
            throw new NotFoundException(
                    "Product with id " + productId + " does not exists"
            );
        }
    }

    public void deleteProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            productRepository.deleteById(id);
        } else{
            throw new NotFoundException(
                    "Product with id " + id + " does not exist"
            );
        }

    }
}