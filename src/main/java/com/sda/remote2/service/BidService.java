package com.sda.remote2.service;

import com.sda.remote2.dto.BidDto;
import com.sda.remote2.mapper.BidMapper;
import com.sda.remote2.model.Bid;
import com.sda.remote2.model.Product;
import com.sda.remote2.model.User;
import com.sda.remote2.repository.BidRepository;
import com.sda.remote2.repository.ProductRepository;
import com.sda.remote2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private BidMapper bidMapper;
    private BidRepository bidRepository;

    @Autowired
    public BidService(ProductRepository productRepository, UserRepository userRepository, BidMapper bidMapper, BidRepository bidRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.bidMapper = bidMapper;
        this.bidRepository = bidRepository;
    }

    public void addBid(BidDto bidDto, String productId, String userEmail) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.parseInt(productId));
        if(!optionalProduct.isPresent()) {
            throw new IllegalArgumentException("Product id is not valid!");
        }
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if(!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User email is not valid!");
        }
        Bid bid = bidMapper.map(bidDto, optionalProduct.get(), optionalUser.get());
        bidRepository.save(bid);
    }

    public void assignWinners() {
        // Ne intereseaza toate produsele care au endDate-ul in trecut si care nu au castigatori asignati
        List<Product> unassignedProducts = productRepository.findEligibleAndUnassigned(LocalDate.now());
        for (Product product : unassignedProducts) {
            Optional<Bid> optionalBid = product.getBids()
                    .stream()
                    .max(Comparator.comparing(Bid::getValue));
            if(!optionalBid.isPresent()) {
                continue;
            }
            product.setWinner(optionalBid.get().getUser());
            productRepository.save(product);
        }
    }
}
