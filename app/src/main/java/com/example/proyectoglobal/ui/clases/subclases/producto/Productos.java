package com.example.proyectoglobal.ui.clases.subclases.producto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "producto")
public class Productos {

    @PrimaryKey
    private Long id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private String imagen;
    private double precio;

    // Constructor, getters y setters
    public Productos() {

    }

    public Productos(Long id, String nombre, String tipo, String descripcion, String imagen, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
