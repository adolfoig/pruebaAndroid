package com.example.proyectoglobal.ui.clases.presupuesto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectoglobal.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PresupuestoFinalFragment extends Fragment {

    private TextView txtNumero, txtFecha, txtNombre, txtCorreo, txtDescripcion;
    private TextView txtIdProducto, txtCantidad, txtPrecioUnitario, txtPrecioTotal;
    private Button btnPublicar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar la vista
        View root = inflater.inflate(R.layout.fragment_presupuesto_final, container, false);

        // Inicializamos las vistas con findViewById()
        txtNumero = root.findViewById(R.id.txtNumero);
        txtFecha = root.findViewById(R.id.txtFecha);
        txtNombre = root.findViewById(R.id.txtNombre);
        txtCorreo = root.findViewById(R.id.txtCorreo);
        txtDescripcion = root.findViewById(R.id.txtDescripcion);
        txtIdProducto = root.findViewById(R.id.txtIdProducto);
        txtCantidad = root.findViewById(R.id.txtCantidad);
        txtPrecioUnitario = root.findViewById(R.id.txtPrecioUnitario);
        txtPrecioTotal = root.findViewById(R.id.txtPrecioTotal);
        btnPublicar = root.findViewById(R.id.btnPublicar);

        // Obtener los datos pasados del fragmento anterior
        Bundle args = getArguments();
        if (args != null) {
            txtNumero.setText("Nº: " + args.getString("numero"));
            txtFecha.setText("Fecha: " + args.getString("fecha"));
            txtNombre.setText("Nombre del Cliente: " + args.getString("nombre"));
            txtCorreo.setText("Correo Electrónico: " + args.getString("correo"));
            txtDescripcion.setText("Descripción: " + args.getString("descripcion"));
            txtIdProducto.setText("ID del Producto: " + args.getString("idProducto"));
            txtCantidad.setText("Cantidad: " + args.getString("cantidad"));
            txtPrecioUnitario.setText("Precio Unitario: " + args.getString("precioUnitario"));
            txtPrecioTotal.setText("Precio Total: " + args.getString("precioTotal"));
        }

        // Configurar el botón Publicar para guardar el presupuesto en Firestore
        btnPublicar.setOnClickListener(v -> publicarPresupuesto());

        return root;
    }

    private void publicarPresupuesto() {
        // Obtener los datos para publicar
        Map<String, Object> presupuesto = new HashMap<>();
        presupuesto.put("numero", txtNumero.getText().toString());
        presupuesto.put("fecha", txtFecha.getText().toString());
        presupuesto.put("nombre", txtNombre.getText().toString());
        presupuesto.put("correo", txtCorreo.getText().toString());
        presupuesto.put("descripcion", txtDescripcion.getText().toString());
        presupuesto.put("idProducto", txtIdProducto.getText().toString());
        presupuesto.put("cantidad", txtCantidad.getText().toString());
        presupuesto.put("precioUnitario", txtPrecioUnitario.getText().toString());
        presupuesto.put("precioTotal", txtPrecioTotal.getText().toString());

        // Publicar en Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("presupuestos").add(presupuesto)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Presupuesto publicado con éxito", Toast.LENGTH_SHORT).show();

                    // Redirigir al PresupuestoFragment
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(containerId(), new PresupuestosFragment());
                    transaction.commit();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al publicar el presupuesto", Toast.LENGTH_SHORT).show();
                });
    }
    private int containerId() {
        return requireActivity().findViewById(android.R.id.content).getId();
    }
}
