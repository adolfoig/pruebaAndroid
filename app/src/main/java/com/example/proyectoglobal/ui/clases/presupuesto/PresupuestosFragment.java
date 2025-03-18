package com.example.proyectoglobal.ui.clases.presupuesto;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoglobal.R;
import com.example.proyectoglobal.databinding.FragmentPresupuestoBinding;
import com.example.proyectoglobal.ui.clases.trabajosRealizados.PresupuestosViewModel;
import com.example.proyectoglobal.ui.clases.trabajosRealizados.TrabajoAdapter;
import com.example.proyectoglobal.ui.clases.trabajosRealizados.TrabajoRealizadosViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PresupuestosFragment extends Fragment {

    private RecyclerView recyclerViewPresupuestos;
    private PresupuestoAdapter presupuestoAdapter;
    private PresupuestosViewModel presupuestosViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_presupuesto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewPresupuestos = view.findViewById(R.id.recyclerPresupuestos);

        if (recyclerViewPresupuestos == null) {
            Log.e("PresupuestosFragment", "ERROR: recyclerViewPresupuestos es NULL");
            return;
        }

        // Configurar RecyclerView con GridLayoutManager de 2 columnas
        recyclerViewPresupuestos.setLayoutManager(new GridLayoutManager(getContext(), 1));

        // Inicializar el adaptador
        presupuestoAdapter = new PresupuestoAdapter(new ArrayList<>());
        recyclerViewPresupuestos.setAdapter(presupuestoAdapter);

        // Obtener presupuestos desde el ViewModel
        presupuestosViewModel = new ViewModelProvider(this).get(PresupuestosViewModel.class);
        presupuestosViewModel.obtenerPresupuesto().observe(getViewLifecycleOwner(), presupuesto -> {
            if (presupuesto != null && !presupuesto.isEmpty()) {
                presupuestoAdapter.setPresupuestoList(presupuesto);
            } else {
                Log.d("PresupuestosFragment", "No hay presupuestos disponibles.");
            }
        });

        // Configurar el botón "+" para navegar al fragmento de crear presupuesto
        View btnAgregarPresupuesto = view.findViewById(R.id.addButton); // Asegúrate de que este ID esté en tu layout XML
        if (btnAgregarPresupuesto != null) {
            btnAgregarPresupuesto.setOnClickListener(v -> {
                // Navegar al fragmento de RellenarPresupuestoFragment
                RellenarPresupuestoFragment fragment = new RellenarPresupuestoFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(containerId(), fragment);
                transaction.addToBackStack(null); // Permite volver atrás
                transaction.commit();
            });
        }
    }
    private int containerId() {
        return requireActivity().findViewById(android.R.id.content).getId();
    }
}

