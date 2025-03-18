package com.example.proyectoglobal.ui.clases.subclases.producto;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductoViewModel extends AndroidViewModel {

    private ProductoRepository repository;

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductoRepository();
    }

    public LiveData<List<Productos>> obtenerProductosPorTrabajo(String trabajoNombre) {
        return repository.obtenerProductosPorTrabajo(trabajoNombre);
    }

    public void agregarProducto(Productos productos) {
        repository.agregarProducto(productos);
    }

}
