package com.example.proyectoglobal.ui.clases.presupuesto;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.proyectoglobal.ui.clases.subclases.material.Material;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresupuestoRepository {

    private FirebaseFirestore db;
    private CollectionReference coleccionTrabajos;

    public PresupuestoRepository() {
        db = FirebaseFirestore.getInstance();
        coleccionTrabajos = db.collection("presupuestos");
    }

    public String getConnectedUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public LiveData<List<Presupuesto>> obtenerPresupuestos() {
        MutableLiveData<List<Presupuesto>> presupuestoLiveData = new MutableLiveData<>();

        // Escucha en tiempo real los cambios en los documentos
        db.collection("presupuestos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Presupuesto> listaPresupuesto = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    // Obtener el valor del campo "numero" y asegurarnos de que sea un número
                    Object numeroObj = doc.get("numero"); // Obtener el valor sin castear directamente
                    int numero = 0;

                    if (numeroObj instanceof Number) {
                        numero = ((Number) numeroObj).intValue(); // Convertir si es un número
                    } else if (numeroObj instanceof String) {
                        try {
                            numero = Integer.parseInt((String) numeroObj); // Si es un String, intentar convertirlo a entero
                        } catch (NumberFormatException e) {
                            numero = 0; // Valor por defecto si no se puede convertir
                        }
                    }

                    String nombre = doc.getString("nombre");
                    String apellido = doc.getString("apellido");
                    String gmail = doc.getString("gmail");

                    // Verificar el tipo del campo "fecha"
                    Object fechaObj = doc.get("fecha"); // Obtener el valor sin castear directamente
                    Long fecha = null;

                    if (fechaObj instanceof Number) {
                        fecha = ((Number) fechaObj).longValue(); // Convertir si es un número
                    } else if (fechaObj instanceof String) {
                        try {
                            fecha = Long.parseLong((String) fechaObj); // Si es un String, intenta convertirlo a Long
                        } catch (NumberFormatException e) {
                            fecha = 0L; // Valor por defecto si no se puede convertir
                        }
                    } else {
                        fecha = 0L; // Valor por defecto si es nulo o de otro tipo
                    }

                    // Obtener la lista de materiales
                    List<Material> listaMaterial = (List<Material>) doc.get("listaMaterial");

                    // Crear el objeto Presupuesto con los datos obtenidos
                    Presupuesto presupuesto = new Presupuesto(numero, nombre, apellido, gmail, fecha, listaMaterial);
                    listaPresupuesto.add(presupuesto);
                }
                presupuestoLiveData.setValue(listaPresupuesto); // Actualizar el LiveData con la lista de presupuestos
            } else {
                presupuestoLiveData.setValue(new ArrayList<>()); // Lista vacía si la consulta falla
            }
        });
        return presupuestoLiveData;
    }

    // Método para agregar un presupuesto a Firestore
    public void agregarPresupuesto(Presupuesto presupuesto) {
        Map<String, Object> presupuestoData = new HashMap<>();
        presupuestoData.put("numero", presupuesto.getNumero());
        presupuestoData.put("nombre", presupuesto.getNombre());
        presupuestoData.put("apellido", presupuesto.getApellidos());
        presupuestoData.put("gmail", presupuesto.getGmail());
        presupuestoData.put("fecha", presupuesto.getFecha());
        presupuestoData.put("listaMaterial", presupuesto.getListaMaterial());

        coleccionTrabajos.add(presupuestoData)
                .addOnSuccessListener(documentReference -> Log.d("PresupuestoRepository", "Presupuesto guardado con ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("PresupuestoRepository", "Error al guardar presupuesto", e));
    }


    public void eliminarPresupuesto(Presupuesto presupuesto) {
        coleccionTrabajos.document(String.valueOf(presupuesto.getNumero()))
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("PresupuestoRepository", "Presupuesto eliminado"))
                .addOnFailureListener(e -> Log.e("PresupuestoRepository", "Error al eliminar presupuesto", e));
    }

    public void calcularTotal(List<Material> listaMaterial, TotalCallback callback) {
        double total = 0.0;

        for (Material material : listaMaterial) {
            double precioUnitario = obtenerPrecioUnitario(material.getId());
            total += precioUnitario * material.getCantidad();
        }

        callback.onTotalCalculated(total);
    }

    private double obtenerPrecioUnitario(long materialId) {
        return 10.0;
    }

    public interface TotalCallback {
        void onTotalCalculated(double total);
    }
}
