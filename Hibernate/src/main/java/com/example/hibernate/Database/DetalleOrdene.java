package com.example.hibernate.Database;

import javax.persistence.*;

@Entity
@Table(name = "detalle_ordenes", schema = "proveedores_productos")
public class DetalleOrdene {
    @EmbeddedId
    private DetalleOrdeneId id;

    @MapsId("ordenid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordenid", nullable = false)
    private com.example.hibernate.Database.Ordene ordenid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productoid", nullable = false)
    private com.example.hibernate.Database.Producto productoid;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    public DetalleOrdeneId getId() {
        return id;
    }

    public void setId(DetalleOrdeneId id) {
        this.id = id;
    }

    public com.example.hibernate.Database.Ordene getOrdenid() {
        return ordenid;
    }

    public void setOrdenid(com.example.hibernate.Database.Ordene ordenid) {
        this.ordenid = ordenid;
    }

    public com.example.hibernate.Database.Producto getProductoid() {
        return productoid;
    }

    public void setProductoid(com.example.hibernate.Database.Producto productoid) {
        this.productoid = productoid;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

}