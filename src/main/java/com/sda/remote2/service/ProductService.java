package com.sda.remote2.service;

import com.sda.remote2.dto.ProductDto;
import com.sda.remote2.mapper.ProductMapper;
import com.sda.remote2.model.Product;
import com.sda.remote2.model.User;
import com.sda.remote2.repository.ProductRepository;
import com.sda.remote2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, UserRepository userRepository){
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userRepository = userRepository;
    }
    public void addProduct(ProductDto productDto, MultipartFile multipartFile, String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if(!optionalUser.isPresent()) {
            throw new IllegalArgumentException("No user with this email!");
        }
        Product product = productMapper.map(productDto, multipartFile, optionalUser.get());
        productRepository.save(product);
    }

    public List<ProductDto> getAllProducts() {
       List<Product> products = productRepository.findAll();
       List<ProductDto> productList = productMapper.map(products);
       return productList;
    }

    public ProductDto getProductById(String productId, String userEmail) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.parseInt(productId));
        if(!optionalProduct.isPresent()) {
            throw new IllegalArgumentException("No product with this ID!");
        }
        return  productMapper.map(optionalProduct.get(), Optional.of(userEmail));
    }

    public List<ProductDto> getProductsFor(String email) {
        List<Product> products = productRepository.findByUserEmail(email);
        List<ProductDto> productList = productMapper.map(products);
        return productList;
    }

    public List<ProductDto> getAllActiveProducts() {
        List<Product> products = productRepository.findAllActiveOn(LocalDate.now());
        List<ProductDto> productList = productMapper.map(products);
        return productList;
    }

    public List<ProductDto> getProductsBiddedBy(String email) {
        List<Product> products = productRepository.findByBidder(email);
        List<ProductDto> productList = productMapper.map(products, email);
        return productList;
    }
}
