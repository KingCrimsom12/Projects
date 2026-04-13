package com.example.hibernate.Database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "proveedores", schema = "proveedores_productos")
public class Proveedore {
    @Id
    @Column(name = "proveedorid", nullable = false)
    private Integer id;

    @Column(name = "nombreprov", nullable = false, length = 50)
    private String nombreprov;

    @Column(name = "contacto", nullable = false, length = 50)
    private String contacto;

    @Column(name = "celuprov", length = 12)
    private String celuprov;

    @Column(name = "fijoprov", length = 12)
    private String fijoprov;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreprov() {
        return nombreprov;
    }

    public void setNombreprov(String nombreprov) {
        this.nombreprov = nombreprov;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCeluprov() {
        return celuprov;
    }

    public void setCeluprov(String celuprov) {
        this.celuprov = celuprov;
    }

    public String getFijoprov() {
        return fijoprov;
    }

    public void setFijoprov(String fijoprov) {
        this.fijoprov = fijoprov;
    }

}