package com.example.proyectoglobal.ui.clases.trabajosRealizados;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoglobal.ui.clases.presupuesto.Presupuesto;
import com.example.proyectoglobal.ui.clases.presupuesto.PresupuestoRepository;
import com.example.proyectoglobal.ui.clases.subclases.producto.Productos;

import java.util.ArrayList;
import java.util.List;

public class PresupuestosViewModel extends ViewModel {

    private final PresupuestoRepository repository;
    private final LiveData<List<Presupuesto>> presupuestoLiveData;

    public PresupuestosViewModel() {
        repository = new PresupuestoRepository();
        presupuestoLiveData = repository.obtenerPresupuestos();
    }

    // Obtener trabajos
    public LiveData<List<Presupuesto>> obtenerPresupuesto() {
        return presupuestoLiveData;
    }

    // Agregar trabajo
    public void agregarPresupuesto(Presupuesto presupuesto) {
        repository.agregarPresupuesto(presupuesto);
    }

}
