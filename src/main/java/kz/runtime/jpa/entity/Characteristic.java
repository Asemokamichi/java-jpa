package kz.runtime.jpa.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "characteristics")
public class Characteristic {
    @Id
    @Column(name = "characteristic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long characteristicID;

    @Column(name = "characteristics_name")
    private String characteristicName;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "characteristic")
    private List<ProductCharacteristic> productCharacteristics;

    public void setCharacteristicID(long characteristicID) {
        this.characteristicID = characteristicID;
    }

    public void setCharacteristicName(String characteristicName) {
        this.characteristicName = characteristicName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setProductCharacteristics(List<ProductCharacteristic> productCharacteristics) {
        this.productCharacteristics = productCharacteristics;
    }

    public long getCharacteristicID() {
        return characteristicID;
    }

    public Object getCharacteristicName() {
        return characteristicName;
    }

    public Category getCategory() {
        return category;
    }

    public List<ProductCharacteristic> getProductCharacteristics() {
        return productCharacteristics;
    }
}
