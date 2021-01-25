package com.sda.remote2.enums;


public enum AuctionStatus {
    WON, LOST, ONGOING;

    public String getMessage(){
        switch (this){
            case WON: return "You won this item";
            case LOST: return "You lost this item";
            case ONGOING: return "Auction ongoing";
            default: return "";
        }
    }
}
