package com.example.proyectoglobal.ui.clases.subclases.producto;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface ProductoDao {

    @Insert
    void insertarProducto(Productos producto);

    @Query("SELECT * FROM producto WHERE id = :productoId")
    LiveData<Productos> obtenerProductoPorId(Long productoId);

    @Query("SELECT * FROM producto")
    LiveData<List<Productos>> obtenerTodosLosProductos();

    @Delete
    void eliminarProducto(Productos producto);

    @Update
    void actualizarProducto(Productos producto);
}
