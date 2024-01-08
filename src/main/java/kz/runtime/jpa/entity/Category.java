package kz.runtime.jpa.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryID;
    @Column(name = "category_name")
    private String categoryName;

    @Override
    public String toString() {
        return "Category" + categoryName +
                "categoryID=" + categoryID +
                ", categoryName='" + categoryName + '\'' +
                ", products=" + products +
                ", characteristics=" + characteristics +
                '}';
    }

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @OneToMany(mappedBy = "category")
    private List<Characteristic> characteristics;

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setCharacteristics(List<Characteristic> characteristics) {
        this.characteristics = characteristics;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Characteristic> getCharacteristics() {
        return characteristics;
    }
}
