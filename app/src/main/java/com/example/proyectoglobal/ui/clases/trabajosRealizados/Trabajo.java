package com.example.proyectoglobal.ui.clases.trabajosRealizados;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import com.example.proyectoglobal.ui.clases.subclases.producto.Productos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity(tableName = "trabajo")
public class Trabajo {

    @PrimaryKey(autoGenerate = true)
    private Long id; // Agregado el campo 'id' como PrimaryKey para Room
    private String nombre;
    private String imagenUrl; // URL de la imagen en Firebase Storage (opcional)

    @Ignore
    private List<Productos> productos; // Lista de productos (no se almacena directamente en la base de datos, se maneja como relación)

    public Trabajo() {

    }

    // Constructor sin productos (se usa cuando creas un nuevo trabajo sin productos)
    public Trabajo(String nombre, String imagenUrl) {
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
    }

    // Constructor con productos (se usa cuando tienes productos en la lista)
    public Trabajo(String nombre, String imagenUrl, List<Productos> productos) {
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.productos = productos;
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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public List<Productos> getProductos() {
        return productos;
    }

    public void setProductos(List<Productos> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Trabajo{" +
                "nombre='" + nombre + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", productos=" + productos +
                '}';
    }

    public void setProductosFromFirestore(List<Map<String, Object>> productosData) {
        this.productos = new ArrayList<>();
        for (Map<String, Object> productoData : productosData) {
            Productos producto = new Productos();

            // Convertir ID correctamente (puede ser un Long o un String)
            Object idObject = productoData.get("id");
            if (idObject instanceof String) {
                producto.setId(Long.valueOf((String) idObject));  // Si el ID es un String, lo convertimos a Long
            } else if (idObject instanceof Long) {
                producto.setId((Long) idObject);  // Si el ID es un Long, simplemente lo asignamos
            }

            producto.setNombre((String) productoData.get("nombre"));
            producto.setTipo((String) productoData.get("tipo"));
            producto.setDescripcion((String) productoData.get("descripcion"));
            producto.setImagen((String) productoData.get("imagen"));
            producto.setPrecio((Double) productoData.get("precio"));

            this.productos.add(producto);
        }
    }

}
