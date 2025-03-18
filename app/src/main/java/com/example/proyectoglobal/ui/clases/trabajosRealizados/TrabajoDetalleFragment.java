package com.example.proyectoglobal.ui.clases.trabajosRealizados;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectoglobal.R;
import com.example.proyectoglobal.ui.clases.subclases.producto.ProductoAdapter;
import com.example.proyectoglobal.ui.clases.subclases.producto.Productos;

import java.util.ArrayList;

public class TrabajoDetalleFragment extends Fragment {

    private RecyclerView recyclerViewProductos;
    private ProductoAdapter productoAdapter;
    private TrabajoRealizadosViewModel trabajoViewModel;

    private TextView tvNombreTrabajo;
    private ImageView imgTrabajoDetalle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trabajo_detalle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNombreTrabajo = view.findViewById(R.id.tvNombreTrabajoDetalle);
        imgTrabajoDetalle = view.findViewById(R.id.imgTrabajo);
        recyclerViewProductos = view.findViewById(R.id.recyclerProductos);
        recyclerViewProductos.setLayoutManager(new GridLayoutManager(getContext(), 1)); // 2 columnas

        productoAdapter = new ProductoAdapter(new ArrayList<>());
        recyclerViewProductos.setAdapter(productoAdapter);

        // Obtener el nombre del trabajo desde el Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String trabajoNombre = bundle.getString("trabajoNombre");

            // Usamos el ViewModel para obtener el trabajo y sus productos
            trabajoViewModel = new ViewModelProvider(this).get(TrabajoRealizadosViewModel.class);
            trabajoViewModel.obtenerTrabajoConProductos(trabajoNombre).observe(getViewLifecycleOwner(), trabajo -> {
                if (trabajo != null) {
                    // Log para verificar los productos y sus campos
                    Log.d("TrabajoDetalleFragment", "Trabajo: " + trabajo.getNombre());
                    for (Productos producto : trabajo.getProductos()) {
                        Log.d("TrabajoDetalleFragment", "Producto: " + producto.getNombre() +
                                ", Descripción: " + producto.getDescripcion() +
                                ", Precio: " + producto.getPrecio());
                    }

                    // Verificar si los productos tienen descripción y precio
                    if (trabajo.getProductos() != null && !trabajo.getProductos().isEmpty()) {
                        productoAdapter.setProductosList(trabajo.getProductos());
                        productoAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("TrabajoDetalleFragment", "No hay productos para mostrar.");
                    }
                } else {
                    Log.e("TrabajoDetalleFragment", "No se encontró el trabajo.");
                }
            });
        }
    }
}
