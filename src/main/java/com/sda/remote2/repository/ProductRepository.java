package com.sda.remote2.repository;

import com.sda.remote2.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select p from Product p where p.user.email = :email")
    List<Product> findByUserEmail(@Param("email") String email);

    @Query("select p from Product p where :now between p.startBiddingDate and p.endBiddingDate")
    List<Product> findAllActiveOn(@Param("now") LocalDate now);

    @Query("select distinct b.product from  Bid b where b.user.email = :email")
    List<Product> findByBidder(@Param("email") String email);

    @Query("select p from Product p where p.winner is null and p.endBiddingDate < :now")
    List<Product> findEligibleAndUnassigned(@Param("now") LocalDate now);
}
