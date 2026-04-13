package com.example.hibernate.Database;

import javax.persistence.*;

@Entity
@Table(name = "productos", schema = "proveedores_productos")
public class Producto {
    @Id
    @Column(name = "productoid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proveedorid", nullable = false)
    private com.example.hibernate.Database.Proveedore proveedorid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoriaid", nullable = false)
    private Categoria categoriaid;

    @Column(name = "descripcion", length = 50)
    private String descripcion;

    @Column(name = "preciounit", nullable = false)
    private Double preciounit;

    @Column(name = "existencia", nullable = false)
    private Integer existencia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.example.hibernate.Database.Proveedore getProveedorid() {
        return proveedorid;
    }

    public void setProveedorid(com.example.hibernate.Database.Proveedore proveedorid) {
        this.proveedorid = proveedorid;
    }

    public Categoria getCategoriaid() {
        return categoriaid;
    }

    public void setCategoriaid(Categoria categoriaid) {
        this.categoriaid = categoriaid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPreciounit() {
        return preciounit;
    }

    public void setPreciounit(Double preciounit) {
        this.preciounit = preciounit;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }

}