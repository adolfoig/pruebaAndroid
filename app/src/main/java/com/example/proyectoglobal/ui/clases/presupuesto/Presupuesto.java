package com.example.proyectoglobal.ui.clases.presupuesto;

import androidx.room.Entity;

import com.example.proyectoglobal.ui.clases.subclases.material.Material;

import java.util.List;

@Entity(tableName = "presupuesto")
public class Presupuesto {

    //atributos
    private int numero;
    private String nombre;
    private String apellidos;
    private String gmail;
    private Long fecha;
    private List<Material> listaMaterial;

    //constructor
    public Presupuesto() {

    }

    public Presupuesto(int numero, String nombre, String apellidos, String gmail, Long fecha, List<Material> listaMaterial) {
        this.numero = numero;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.gmail = gmail;
        this.fecha = fecha;
        this.listaMaterial = listaMaterial;
    }

    //getters y setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public List<Material> getListaMaterial() {
        return listaMaterial;
    }
    public void setListaMaterial(List<Material> listaMaterial) {
        this.listaMaterial = listaMaterial;
    }

    public Long getFecha() {
        return fecha;
    }
    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Presupuesto{" +
                "numero=" + numero +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", gmail='" + gmail + '\'' +
                ", fecha=" + fecha +
                ", listaMaterial=" + listaMaterial +
                '}';
    }
}
