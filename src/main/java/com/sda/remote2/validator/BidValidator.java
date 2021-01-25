package com.sda.remote2.validator;

import com.sda.remote2.dto.BidDto;
import com.sda.remote2.model.Bid;
import com.sda.remote2.model.Product;
import com.sda.remote2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BidValidator {

    private ProductRepository productRepository;

    @Autowired
    public BidValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(BidDto bidDto, String productId, BindingResult bindingResult) {
        Integer bidValue = 0;
        try {
             bidValue = Integer.parseInt(bidDto.getValue());
        } catch (NumberFormatException exception) {
            bindingResult.addError(new FieldError("bidDto", "value", "Value should be a number!"));
        }
        Optional<Product> optionalProduct = productRepository.findById(Integer.parseInt(productId));
        if(!optionalProduct.isPresent()) {
            bindingResult.addError(new FieldError("bidDto", "value", "Product does not exist!"));
        }
        Product product = optionalProduct.get();
        Optional<Bid> optionalBid = product.getBids()
                .stream()
                .max(Comparator.comparing(Bid::getValue));
        if(!optionalBid.isPresent()) {
            //Sunt primu licitator
            if(product.getStartingPrice().compareTo(bidValue)>0) {
                bindingResult.addError(new FieldError("bidDto", "value", "Value is less than the starting price."));
            }
        } else {
            if(bidValue.compareTo(optionalBid.get().getValue())<=0) {
                bindingResult.addError(new FieldError("bidDto", "value", "Value is less or equal to the current bid."));
            }
        }
    }
}
