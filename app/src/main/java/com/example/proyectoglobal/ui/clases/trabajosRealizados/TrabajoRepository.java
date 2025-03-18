package com.example.proyectoglobal.ui.clases.trabajosRealizados;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.proyectoglobal.ui.clases.subclases.producto.Productos;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrabajoRepository {

    private FirebaseFirestore db;
    private CollectionReference coleccionTrabajos;

    public TrabajoRepository() {
        db = FirebaseFirestore.getInstance();
        coleccionTrabajos = db.collection("trabajos"); // Referencia a la colección "trabajos"
    }

    // Método para obtener los trabajos
    public LiveData<List<Trabajo>> obtenerTrabajos() {
        MutableLiveData<List<Trabajo>> trabajosLiveData = new MutableLiveData<>();

        db.collection("trabajos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Trabajo> listaTrabajos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String nombre = doc.getString("nombre");
                    Log.d("TrabajoRepository", "Trabajo nombre: " + nombre);

                    // Comprobar si el nombre es válido (no nulo o vacío)
                    if (nombre == null || nombre.isEmpty()) {
                        Log.e("TrabajoRepository", "El trabajo tiene un nombre inválido");
                        continue; // Saltar este trabajo si el nombre es inválido
                    }

                    // Manejar imagenUrl de manera segura
                    Object imagenUrlObject = doc.get("imagen");
                    String imagenUrl = "default_image_url";  // Valor predeterminado

                    // Verificar si imagenUrlObject no es null antes de continuar
                    if (imagenUrlObject != null) {
                        if (imagenUrlObject instanceof Long) {
                            // Si el campo es Long, conviértelo a String
                            imagenUrl = String.valueOf(imagenUrlObject);
                        } else if (imagenUrlObject instanceof String) {
                            // Si ya es String, simplemente asignarlo
                            imagenUrl = (String) imagenUrlObject;
                        } else {
                            // Si es otro tipo inesperado, loggear el error
                            Log.e("TrabajoRepository", "Tipo inesperado para imagenUrl: " + imagenUrlObject.getClass().getSimpleName());
                        }
                    } else {
                        Log.e("TrabajoRepository", "imagenUrl es null, asignando valor predeterminado");
                    }

                    // Verificar que los datos son correctos
                    if (imagenUrl == null || imagenUrl.isEmpty()) {
                        Log.e("TrabajoRepository", "El trabajo tiene una imagenUrl inválida");
                        continue; // Saltar este trabajo si la imagenUrl es inválida
                    }

                    // Crear el objeto Trabajo
                    Trabajo trabajo = new Trabajo(nombre, imagenUrl);
                    listaTrabajos.add(trabajo);
                }
                trabajosLiveData.setValue(listaTrabajos);
            } else {
                trabajosLiveData.setValue(new ArrayList<>()); // Lista vacía si falla
            }
        });

        return trabajosLiveData;
    }

    // Método para agregar un nuevo trabajo a Firestore
    public void agregarTrabajo(Trabajo trabajo) {
        Map<String, Object> trabajoData = new HashMap<>();
        trabajoData.put("nombre", trabajo.getNombre());
        trabajoData.put("imagen", trabajo.getImagenUrl());

        coleccionTrabajos.add(trabajoData)
                .addOnSuccessListener(documentReference -> Log.d("TrabajoRepository", "Trabajo guardado con ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("TrabajoRepository", "Error al guardar trabajo", e));
    }

    // Método para obtener un trabajo con sus productos
    public LiveData<Trabajo> obtenerTrabajoConProductos(String trabajoNombre) {
        MutableLiveData<Trabajo> trabajoLiveData = new MutableLiveData<>();

        db.collection("trabajos")
                .whereEqualTo("nombre", trabajoNombre)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        Trabajo trabajo = document.toObject(Trabajo.class);

                        if (trabajo != null) {
                            // Obtener los productos del campo "productos"
                            List<Object> productosArray = (List<Object>) document.get("productos");

                            // Si los productos son null o vacíos, inicializa la lista como vacía
                            if (productosArray == null) {
                                productosArray = new ArrayList<>();
                            }

                            List<Productos> productos = new ArrayList<>();

                            // Verificar si el array contiene productos como mapas
                            for (Object productoObj : productosArray) {
                                if (productoObj instanceof Map) {
                                    // Si el objeto es un mapa, se puede convertir a Productos
                                    Productos producto = convertMapToProducto((Map<String, Object>) productoObj);
                                    if (producto != null) {
                                        productos.add(producto);
                                    }
                                }
                            }

                            trabajo.setProductos(productos);
                            trabajoLiveData.setValue(trabajo);
                        } else {
                            trabajoLiveData.setValue(null);  // En caso de que el trabajo sea null
                        }
                    } else {
                        Log.e("TrabajoRepository", "Trabajo no encontrado.");
                        trabajoLiveData.setValue(null);  // Si no se encuentra el trabajo
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error obteniendo trabajo", e);
                    trabajoLiveData.setValue(null);  // Error en la obtención de trabajo
                });

        return trabajoLiveData;
    }

    // Función para convertir un Map a un objeto de tipo Productos
    private Productos convertMapToProducto(Map<String, Object> map) {
        if (map == null) return null;

        Long id = (Long) map.get("id");
        String nombre = (String) map.get("nombre");
        String tipo = (String) map.get("tipo");
        String descripcion = (String) map.get("descripcion");
        String imagen = (String) map.get("imagen");

        // Comprobar si "precio" es un Long o un Double y convertirlo correctamente
        Object precioObj = map.get("precio");
        double precio = 0;

        if (precioObj instanceof Long) {
            precio = ((Long) precioObj).doubleValue();  // Convertir Long a double
        } else if (precioObj instanceof Double) {
            precio = (Double) precioObj;  // Ya es Double, simplemente asignarlo
        }

        return new Productos(id, nombre, tipo, descripcion, imagen, precio);
    }

    // Método para actualizar el trabajo con los productos
    public void actualizarTrabajo(Trabajo trabajo) {
        // Aquí, si ya tienes el ID del trabajo (de Firestore), puedes actualizar directamente en Firestore
        db.collection("trabajos")
                .whereEqualTo("nombre", trabajo.getNombre())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String trabajoId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("trabajos").document(trabajoId)
                                .update("productos", trabajo.getProductos())
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Trabajo actualizado correctamente"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error al actualizar trabajo", e));
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error buscando trabajo para actualizar", e));
    }
}
