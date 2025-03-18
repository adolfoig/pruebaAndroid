package com.example.proyectoglobal.ui.clases.subclases.producto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectoglobal.R;
import com.example.proyectoglobal.ui.clases.trabajosRealizados.Trabajo;
import com.example.proyectoglobal.ui.clases.trabajosRealizados.TrabajoRealizadosViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrarProductosFragment extends Fragment {

    private EditText etIdProducto, etNombreProducto, etTipoProducto, etDescripcionProducto, etPrecioProducto;
    private Button btnGuardarProducto, btnSubirImagen;
    private ImageView imgProducto;
    private Uri imageUri;
    private ProductoViewModel productoViewModel;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registrar_productos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Vincula los elementos de la vista
        etIdProducto = view.findViewById(R.id.idProducto);
        etNombreProducto = view.findViewById(R.id.nombreProducto);
        etTipoProducto = view.findViewById(R.id.tipoProducto);
        etDescripcionProducto = view.findViewById(R.id.descripcionProducto);
        etPrecioProducto = view.findViewById(R.id.precioProducto);
        imgProducto = view.findViewById(R.id.imagenProducto);
        btnGuardarProducto = view.findViewById(R.id.btnRegistrarProducto);
        btnSubirImagen = view.findViewById(R.id.btnSubirImagen);

        // Inicializa Firebase y ViewModel
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firestore = FirebaseFirestore.getInstance();

        // Configura los botones
        btnGuardarProducto.setOnClickListener(v -> guardarProducto());
        btnSubirImagen.setOnClickListener(v -> seleccionarImagen());
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100); // Iniciar la selección de la imagen
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            imgProducto.setImageURI(imageUri);  // Muestra la imagen seleccionada en el ImageView
            Toast.makeText(getContext(), "Imagen seleccionada", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarProducto() {
        // Obtener los datos del producto
        String idProductoStr = etIdProducto.getText().toString().trim();
        String nombreProducto = etNombreProducto.getText().toString().trim();
        String tipoProducto = etTipoProducto.getText().toString().trim();
        String descripcionProducto = etDescripcionProducto.getText().toString().trim();
        String precioProductoStr = etPrecioProducto.getText().toString().trim();

        // Validación de los campos
        if (idProductoStr.isEmpty() || nombreProducto.isEmpty() || tipoProducto.isEmpty() || descripcionProducto.isEmpty() || precioProductoStr.isEmpty()) {
            Toast.makeText(getContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Usar arrays para hacer las variables mutables
        final Long[] idProducto = {null}; // Usamos un array para hacer mutable el valor
        try {
            idProducto[0] = Long.parseLong(idProductoStr);  // Asignamos el valor al array
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Por favor ingrese un ID válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Usamos arrays para el precio
        final double[] precioProducto = {0}; // Usamos un array para hacer mutable el valor
        try {
            precioProducto[0] = Double.parseDouble(precioProductoStr);  // Asignamos el valor al array
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Por favor ingrese un precio válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Usamos una variable final para la imagen
        final String[] imagenProducto = {"sin-imagen"};  // Declaramos como final para poder usarlo en la lambda

        // Subir imagen si se seleccionó una
        if (imageUri != null) {
            StorageReference imageRef = storageReference.child("productos/" + System.currentTimeMillis() + ".jpg");
            UploadTask uploadTask = imageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imagenProducto[0] = uri.toString();  // Aquí modificamos la variable dentro de la lambda
                    // Crear el objeto Producto con los datos
                    Productos producto = new Productos(idProducto[0], nombreProducto, tipoProducto, descripcionProducto, imagenProducto[0], precioProducto[0]);

                    // Agregar producto al ViewModel
                    productoViewModel.agregarProducto(producto);

                    // Usamos el nombre del producto para encontrar el trabajo adecuado
                    asociarProductoAlTrabajo(producto);

                    Toast.makeText(getContext(), "Producto guardado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Si no se seleccionó imagen, guardar el producto con la imagen por defecto
            Productos producto = new Productos(idProducto[0], nombreProducto, tipoProducto, descripcionProducto, imagenProducto[0], precioProducto[0]);
            productoViewModel.agregarProducto(producto);

            // Usamos el nombre del producto para encontrar el trabajo adecuado
            asociarProductoAlTrabajo(producto);

            Toast.makeText(getContext(), "Producto guardado sin imagen", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
    }


    private void asociarProductoAlTrabajo(Productos producto) {
        // Buscar el trabajo con nombre que coincide con el nombre del producto
        firestore.collection("trabajos")
                .whereEqualTo("nombre", producto.getNombre())  // Usamos el nombre del producto para buscar el trabajo
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Si el trabajo existe, obtén el primer trabajo
                        String idTrabajo = queryDocumentSnapshots.getDocuments().get(0).getId(); // Obtén el ID del trabajo

                        // Obtiene la lista de productos del trabajo
                        List<Map<String, Object>> listaProductos = (List<Map<String, Object>>) queryDocumentSnapshots.getDocuments().get(0).get("productos");

                        if (listaProductos == null) {
                            listaProductos = new ArrayList<>();
                        }

                        // Crear un mapa para el producto con todos los datos del producto
                        Map<String, Object> productoMap = new HashMap<>();
                        productoMap.put("id", producto.getId());  // Añade el ID del producto
                        productoMap.put("nombre", producto.getNombre());  // Añade el nombre del producto
                        productoMap.put("tipo", producto.getTipo());  // Añade el tipo del producto
                        productoMap.put("descripcion", producto.getDescripcion());  // Añade la descripción
                        productoMap.put("precio", producto.getPrecio());  // Añade el precio
                        productoMap.put("imagen", producto.getImagen());  // Añade la imagen

                        // Agregar el mapa del producto a la lista de productos del trabajo
                        listaProductos.add(productoMap);

                        // Actualiza el trabajo con la nueva lista de productos
                        firestore.collection("trabajos").document(idTrabajo)
                                .update("productos", listaProductos)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("RegistrarProductos", "Producto agregado al trabajo con éxito");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("RegistrarProductos", "Error al agregar producto al trabajo", e);
                                });
                    } else {
                        Log.e("RegistrarProductos", "Trabajo no encontrado con ese nombre");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("RegistrarProductos", "Error al buscar trabajo", e);
                });
    }

    private void limpiarCampos() {
        etIdProducto.setText("");
        etNombreProducto.setText("");
        etTipoProducto.setText("");
        etDescripcionProducto.setText("");
        etPrecioProducto.setText("");
        imgProducto.setImageURI(null); // Limpiar la imagen seleccionada
    }

}
