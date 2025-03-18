package com.example.proyectoglobal.ui.clases.presupuesto;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectoglobal.R;
import com.example.proyectoglobal.ui.clases.subclases.material.Material;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RellenarPresupuestoFragment extends Fragment {

    private EditText etNumeroPresupuesto, etFecha, etNombre, etApellido, etCorreo, etDescripcion, etIdProducto, etCantidad, etPrecioUnitario;
    private Button btnCalcular;
    private List<Material> listaMaterial;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rellenar_presupuesto, container, false);

        // Inicializamos los campos
        etNumeroPresupuesto = root.findViewById(R.id.etNumeroPresupuesto);
        etFecha = root.findViewById(R.id.etFecha);
        etNombre = root.findViewById(R.id.etNombre);
        etApellido = root.findViewById(R.id.etApellido);
        etCorreo = root.findViewById(R.id.etCorreo);
        etDescripcion = root.findViewById(R.id.etDescripcion);
        etIdProducto = root.findViewById(R.id.etIdProducto);
        etCantidad = root.findViewById(R.id.etCantidad);
        btnCalcular = root.findViewById(R.id.btnCalcular);

        listaMaterial = new ArrayList<>();

        // Configuración del botón Calcular
        btnCalcular.setOnClickListener(v -> calcularTotal());

        return root;
    }

    private void calcularTotal() {
        String cantidadStr = etCantidad.getText().toString();
        String idProductoStr = etIdProducto.getText().toString().trim();  // Aseguramos que no haya espacios
        String nombre = etNombre.getText().toString();
        String correo = etCorreo.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String fecha = etFecha.getText().toString();

        // Verificación de que todos los campos estén llenos
        if (nombre.isEmpty() || correo.isEmpty() || descripcion.isEmpty() || fecha.isEmpty() || idProductoStr.isEmpty() || cantidadStr.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log para verificar el ID del producto antes de realizar la consulta
        Log.d("RellenarPresupuestoFragment", "ID del producto: " + idProductoStr);

        // Convertimos la cantidad a un número
        double cantidad = Double.parseDouble(cantidadStr);

        // Verificamos si el idProducto es un número (long)
        try {
            long idProducto = Long.parseLong(idProductoStr);  // Convertimos el idProducto a long

            // Log para verificar el tipo de ID
            Log.d("RellenarPresupuestoFragment", "ID del producto (long): " + idProducto);

            // Consultamos Firestore para obtener el precio del producto con el ID dado (buscamos en el campo 'id' y no en el ID del documento)
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("productos")
                    .whereEqualTo("id", idProducto) // Buscamos el campo "id" que es Long
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0); // Nos aseguramos de coger el primer documento (si hay coincidencia)
                                // Obtenemos el precio del producto desde la base de datos
                                Double precioUnitario = document.getDouble("precio");
                                if (precioUnitario != null) {
                                    // Calculamos el precio total
                                    double total = cantidad * precioUnitario;

                                    // Pasamos los datos al siguiente fragmento
                                    Bundle args = new Bundle();
                                    args.putString("numero", etNumeroPresupuesto.getText().toString());
                                    args.putString("fecha", fecha);
                                    args.putString("nombre", nombre);
                                    args.putString("correo", correo);
                                    args.putString("descripcion", descripcion);
                                    args.putString("idProducto", String.valueOf(idProducto));
                                    args.putString("cantidad", cantidadStr);
                                    args.putString("precioUnitario", String.valueOf(precioUnitario));
                                    args.putString("precioTotal", String.valueOf(total));

                                    // Creamos y configuramos el fragmento final
                                    PresupuestoFinalFragment fragment = new PresupuestoFinalFragment();
                                    fragment.setArguments(args); // Pasamos los datos al fragmento final
                                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(containerId(), fragment);
                                    transaction.addToBackStack(null); // Permite volver atrás
                                    transaction.commit();
                                } else {
                                    Toast.makeText(getContext(), "El producto no tiene un precio asignado", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Producto no encontrado en la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error al consultar el producto", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "El ID del producto no es válido", Toast.LENGTH_SHORT).show();
        }
    }
    private int containerId() {
        return requireActivity().findViewById(android.R.id.content).getId();
    }
}
