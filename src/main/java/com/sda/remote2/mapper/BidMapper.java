package com.sda.remote2.mapper;

import com.sda.remote2.dto.BidDto;
import com.sda.remote2.model.Bid;
import com.sda.remote2.model.Product;
import com.sda.remote2.model.User;
import org.springframework.stereotype.Service;

@Service
public class BidMapper {
    public Bid map(BidDto bidDto, Product product, User user) {
        Bid result = new Bid();
        result.setUser(user);
        result.setProduct(product);
        result.setValue(Integer.parseInt(bidDto.getValue()));
        return result;
    }
}
