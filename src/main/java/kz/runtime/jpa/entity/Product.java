package kz.runtime.jpa.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productID;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "product_name")
    private String productName;
    double cost;

    @Override
    public String toString() {
        return "\nID        : " + productID +
                "\nНазвание  : " + productName +
                "\nКатегория : " + category.getCategoryName() +
                "\nСтоймость : " + cost;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setProductCharacteristics(List<ProductCharacteristic> productCharacteristics) {
        this.productCharacteristics = productCharacteristics;
    }

    @OneToMany(mappedBy = "product")
    private List<ProductCharacteristic> productCharacteristics;

    public long getProductID() {
        return productID;
    }

    public Category getCategory() {
        return category;
    }

    public String getProductName() {
        return productName;
    }

    public double getCost() {
        return cost;
    }

    public List<ProductCharacteristic> getProductCharacteristics() {
        return productCharacteristics;
    }
}
