package com.example.proyectoglobal.ui.clases.presupuesto;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface PresupuestoDao {

    @Insert
    void insertarPresupuesto(Presupuesto presupuesto);

    @Query("SELECT * FROM presupuesto")
    LiveData<List<Presupuesto>> obtenerTodosLosPresupuestos();

    @Query("SELECT * FROM presupuesto WHERE idPresupuesto = :id")
    LiveData<Presupuesto> obtenerPresupuestoPorId(int id);

    @Update
    void actualizarPresupuesto(Presupuesto presupuesto);

    @Delete
    void eliminarPresupuesto(Presupuesto presupuesto);
}
