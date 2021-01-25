package com.sda.remote2.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Integer startingPrice;
    private ProductCategory productCategory;
    private LocalDate startBiddingDate;
    private LocalDate endBiddingDate;

    @Lob
    private byte[] image;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.EAGER)
    private List<Bid> bids;

    @ManyToOne(cascade = CascadeType.ALL)
    private User winner;
}
