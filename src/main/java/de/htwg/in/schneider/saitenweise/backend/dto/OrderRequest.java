package de.htwg.in.schneider.saitenweise.backend.dto;

import java.util.List;

public class OrderRequest {
    private List<OrderItem> items;
    private Address address;

    // Getters and Setters
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
}
