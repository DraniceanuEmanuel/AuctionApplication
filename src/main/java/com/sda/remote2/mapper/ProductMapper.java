package com.sda.remote2.mapper;

import com.sda.remote2.dto.ProductDto;
import com.sda.remote2.model.Bid;
import com.sda.remote2.model.Product;
import com.sda.remote2.model.ProductCategory;
import com.sda.remote2.model.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.sda.remote2.enums.AuctionStatus.*;

@Service
public class ProductMapper {
    public Product map(ProductDto productDto, MultipartFile multipartFile, User user) {
        Product product = new Product();
         product.setName(productDto.getName());
         product.setDescription(productDto.getDescription());
         product.setProductCategory(ProductCategory.valueOf(productDto.getCategory()));
         product.setStartingPrice(Integer.parseInt(productDto.getStartingPrice()));
         product.setStartBiddingDate(LocalDate.parse(productDto.getStartBiddingDate()));
         product.setEndBiddingDate(LocalDate.parse(productDto.getEndBiddingDate()));
        try {
            product.setImage(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setUser(user);
        return product;
    }
    public List<ProductDto> map(List<Product> products) {
        return map(products, Optional.empty());
    }
    public List<ProductDto> map(List<Product> products, String email) {
        return map(products, Optional.of(email));
    }

    private List<ProductDto> map(List<Product> products, Optional<String> optionalEmail) {

        List<ProductDto> result = new ArrayList<>();
        for(int index = 0; index<products.size(); index++){
           Product product = products.get(index);
           ProductDto productDto = map(product, optionalEmail);
           result.add(productDto);
        }
        return result;
    }

    public ProductDto map(Product product, Optional<String> optionalUserEmail) {

        ProductDto result = new ProductDto();
        result.setName(product.getName());
        result.setDescription(product.getDescription());
        result.setCategory(product.getProductCategory().toString());
        result.setStartingPrice(product.getStartingPrice().toString());
        result.setStartBiddingDate(product.getStartBiddingDate().toString());
        result.setEndBiddingDate(product.getEndBiddingDate().toString());
        result.setId(product.getId().toString());

        String base64imageAsString = Base64.encodeBase64String(product.getImage());
        result.setImage(base64imageAsString);
        result.setOwnerName(product.getUser().getFirstName());

        Optional<Bid> optionalBid = product.getBids()
                .stream()
                .max(Comparator.comparing(Bid::getValue));
        if(optionalBid.isPresent()) {
            result.setCurrentBid(optionalBid.get().getValue().toString());
        } else {
            result.setCurrentBid("0");
        }
        if(optionalUserEmail.isPresent()) {
            Optional<Bid> optionalUserMaxBid = product.getBids()
                    .stream()
                    .filter(bid -> bid.getUser().getEmail().equals(optionalUserEmail.get()))
                    .max(Comparator.comparing(Bid::getValue));
            if(optionalUserMaxBid.isPresent()) {
                result.setUserMaximBid(optionalUserMaxBid.get().getValue().toString());
            } else {
                result.setUserMaximBid("0");
            }
        }
        assignStatus(product, result, optionalUserEmail);
        return result;
    }

    private void assignStatus(Product product, ProductDto productDto, Optional<String> optionalUserEmail) {
        if(!optionalUserEmail.isPresent()) {
            return;
        }
        String userEmail = optionalUserEmail.get();
        if(product.getWinner() != null) {
            User winner = product.getWinner();
            if(winner.getEmail().equals(userEmail)) {
                productDto.setStatusMessage(WON.getMessage());
                productDto.setStatusCode(WON.name());
            } else {
                productDto.setStatusMessage(LOST.getMessage());
                productDto.setStatusCode(LOST.name());
            }
        } else {
            productDto.setStatusMessage(ONGOING.getMessage());
            productDto.setStatusCode(ONGOING.name());
        }
    }
}
