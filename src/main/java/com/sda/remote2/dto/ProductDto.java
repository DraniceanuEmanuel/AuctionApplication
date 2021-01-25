package com.sda.remote2.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDto {
    private String ownerName;
    private String name;
    private String description;
    private String startingPrice;
    private String currentBid;
    private String userMaximBid;
    private String category;
    private String startBiddingDate;
    private String endBiddingDate;
    private String id;
    private String image;
    private String statusMessage;
    private String statusCode;

}
