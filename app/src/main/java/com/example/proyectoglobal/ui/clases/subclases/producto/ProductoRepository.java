package com.example.proyectoglobal.ui.clases.subclases.producto;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductoRepository {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public LiveData<List<Productos>> obtenerProductosPorTrabajo(String trabajoNombre) {
        MutableLiveData<List<Productos>> productosLiveData = new MutableLiveData<>();

        Log.d("ProductoRepository", "Obteniendo productos para el trabajo: " + trabajoNombre);

        firestore.collection("trabajos")
                .whereEqualTo("nombre", trabajoNombre)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        List<Object> productosArray = (List<Object>) document.get("productos");

                        List<Productos> productosList = new ArrayList<>();

                        if (productosArray != null) {
                            for (Object obj : productosArray) {
                                if (obj instanceof Map) {
                                    Map<String, Object> productoMap = (Map<String, Object>) obj;

                                    long id = productoMap.containsKey("id") ? (long) productoMap.get("id") : 0;
                                    String nombre = (String) productoMap.get("nombre");
                                    String descripcion = (String) productoMap.get("descripcion");
                                    String imagen = (String) productoMap.get("imagen");
                                    long precio = productoMap.containsKey("precio") ? (long) productoMap.get("precio") : 0;
                                    String tipo = (String) productoMap.get("tipo");

                                    Productos producto = new Productos(id, nombre, descripcion, imagen, tipo, precio);
                                    productosList.add(producto);
                                }
                            }
                        }

                        productosLiveData.setValue(productosList);
                        Log.d("ProductoRepository", "Productos obtenidos con éxito: " + productosList.size());
                    } else {
                        Log.w("ProductoRepository", "No se encontró el trabajo: " + trabajoNombre);
                    }
                })
                .addOnFailureListener(e -> {
                    productosLiveData.setValue(null);
                    Log.w("ProductoRepository", "Error al obtener productos.", e);
                });

        return productosLiveData;
    }

    public void agregarProducto(Productos producto) {
        firestore.collection("productos")
                .add(producto)
                .addOnSuccessListener(documentReference -> {
                    Log.d("ProductoRepository", "Producto agregado con éxito: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("ProductoRepository", "Error al agregar el producto", e);
                });
    }
}
