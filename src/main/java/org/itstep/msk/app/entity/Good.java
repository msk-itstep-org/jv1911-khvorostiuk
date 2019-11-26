package org.itstep.msk.app.entity;

import org.itstep.msk.app.entity.enums.Catalog;

import javax.persistence.*;

@Entity
@Table(name = "goods")
public class Good {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Double price;

    @Column
    private String manufacturer;

    @Column
    @Enumerated(EnumType.STRING)
    private Catalog catalog;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
