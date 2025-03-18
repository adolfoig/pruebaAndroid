package com.example.proyectoglobal.ui.clases.trabajosRealizados;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.proyectoglobal.ui.clases.subclases.producto.Productos;

import java.util.ArrayList;
import java.util.List;

public class TrabajoRealizadosViewModel extends ViewModel {

    private final TrabajoRepository repository;
    private final LiveData<List<Trabajo>> trabajosLiveData;

    public TrabajoRealizadosViewModel() {
        repository = new TrabajoRepository();
        trabajosLiveData = repository.obtenerTrabajos();
    }

    // Obtener trabajos
    public LiveData<List<Trabajo>> obtenerTrabajos() {
        return trabajosLiveData;
    }

    // Agregar trabajo
    public void agregarTrabajo(Trabajo trabajo) {
        repository.agregarTrabajo(trabajo);
    }

    // Obtener trabajo con productos asociados
    public LiveData<Trabajo> obtenerTrabajoConProductos(String trabajoNombre) {
        return repository.obtenerTrabajoConProductos(trabajoNombre);
    }
}
