package com.example.proyectoglobal.ui.clases.trabajosRealizados;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoglobal.R;

import java.util.ArrayList;

public class TrabajosRealizadosFragment extends Fragment {

    private RecyclerView recyclerViewTrabajos;
    private TrabajoAdapter trabajoAdapter;
    private TrabajoRealizadosViewModel trabajoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trabajos_realizados, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewTrabajos = view.findViewById(R.id.recycler_trabajosRealizados);

        if (recyclerViewTrabajos == null) {
            Log.e("TrabajosRealizadosFragment", "ERROR: recyclerViewTrabajos es NULL");
            return;
        }

        // Configurar RecyclerView con GridLayoutManager de 2 columnas
        recyclerViewTrabajos.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Inicializar el adaptador
        trabajoAdapter = new TrabajoAdapter(new ArrayList<>());
        recyclerViewTrabajos.setAdapter(trabajoAdapter);

        // Obtener trabajos desde el ViewModel
        trabajoViewModel = new ViewModelProvider(this).get(TrabajoRealizadosViewModel.class);
        trabajoViewModel.obtenerTrabajos().observe(getViewLifecycleOwner(), trabajos -> {
            if (trabajos != null && !trabajos.isEmpty()) {
                trabajoAdapter.setTrabajosList(trabajos);
            } else {
                Log.d("TrabajosRealizadosFragment", "No hay trabajos disponibles.");
            }
        });
    }
}
