package com.zayen.dto.response;


public class ClientItemStatsDTO {
    private long itemsBought;

    // Constructor
    public ClientItemStatsDTO(long itemsBought) {
        this.itemsBought = itemsBought;
    }

    // Getter & Setter
    public long getItemsBought() {
        return itemsBought;
    }

    public void setItemsBought(long itemsBought) {
        this.itemsBought = itemsBought;
    }
}
