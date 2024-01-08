package kz.runtime.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_characteristic")
public class ProductCharacteristic {
    @Id
    @Column(name = "product_characteristic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productCharacteristicID;

    @Column(name = "characteristic")
    private String characteristicText;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "characteristic_id")
    private Characteristic characteristic;

    public void setProductCharacteristicID(long productCharacteristicID) {
        this.productCharacteristicID = productCharacteristicID;
    }

    public void setCharacteristicText(String characteristicText) {
        this.characteristicText = characteristicText;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public long getProductCharacteristicID() {
        return productCharacteristicID;
    }

    public String getCharacteristicText() {
        return characteristicText;
    }

    public Product getProduct() {
        return product;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }
}
