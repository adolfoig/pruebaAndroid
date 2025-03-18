package com.example.proyectoglobal.ui.clases.subclases.material;

import androidx.room.Entity;


@Entity(tableName = "material")
public class Material {

    private long id;
    private int cantidad;

    public Material() {

    }

    public Material(long id, int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                '}';
    }
}
