package com.example.hibernate.Database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categorias", schema = "proveedores_productos")
public class Categoria {
    @Id
    @Column(name = "categoriaid", nullable = false)
    private Integer id;

    @Column(name = "nombrecat", nullable = false, length = 50)
    private String nombrecat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombrecat() {
        return nombrecat;
    }

    public void setNombrecat(String nombrecat) {
        this.nombrecat = nombrecat;
    }

}