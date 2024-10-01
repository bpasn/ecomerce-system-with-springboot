package com.app.application.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.app.domain.models.EOrderStatus;

public class OrderDTO {
    private String id;
    private EOrderStatus orderStatus;
    private Set<OrderItemDTO> orderItems = new HashSet<>();
    private Double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public EOrderStatus getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(EOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    public Set<OrderItemDTO> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(Set<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    @Override
    public String toString() {
        return "OrderDTO [id=" + id + ", orderStatus=" + orderStatus + ", orderItems=" + orderItems + ", totalAmount="
                + totalAmount + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

    

    

}
