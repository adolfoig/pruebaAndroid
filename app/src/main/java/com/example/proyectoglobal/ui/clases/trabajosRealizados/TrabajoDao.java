package com.example.proyectoglobal.ui.clases.trabajosRealizados;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface TrabajoDao {

    @Insert
    void insertarTrabajo(Trabajo trabajo);

    @Query("SELECT * FROM trabajo WHERE nombre = :trabajoNombre")
    LiveData<Trabajo> obtenerTrabajoPorNombre(String trabajoNombre);

    @Query("SELECT * FROM trabajo")
    LiveData<List<Trabajo>> obtenerTodosLosTrabajos();

    @Delete
    void eliminarTrabajo(Trabajo trabajo);

    @Update
    void actualizarTrabajo(Trabajo trabajo);
}
