package com.app.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "product_options")
public class ProductOptionEntity extends BaseEntity {
    @Column(name = "option_name")
    private String optionName;
    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "product_option_group_id")
    private ProductOptionGroupEntity productOptionGroup;

    
    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",updatable = false)
    private LocalDateTime updatedAt;

    
    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductOption [optionName=" + optionName + ", price=" + price + "]";
    }

    public ProductOptionGroupEntity getProductOptionGroup() {
        return productOptionGroup;
    }

    public void setProductOptionGroup(ProductOptionGroupEntity productOptionGroup) {
        this.productOptionGroup = productOptionGroup;
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

}
