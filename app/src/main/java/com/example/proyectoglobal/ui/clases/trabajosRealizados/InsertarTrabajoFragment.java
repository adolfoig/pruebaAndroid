package com.example.proyectoglobal.ui.clases.trabajosRealizados;

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
import com.example.proyectoglobal.ui.clases.subclases.producto.ProductoViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InsertarTrabajoFragment extends Fragment {

    private EditText etNombreTrabajo;
    private Button btnGuardarTrabajo, btnSubirImagen;
    private ImageView imgTrabajo;
    private Uri imageUri;
    private TrabajoRealizadosViewModel trabajoViewModel;

    // Firebase storage
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.fragment_insertar_trabajo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Vincula los elementos de la vista
        etNombreTrabajo = view.findViewById(R.id.etNombreTrabajo);
        imgTrabajo = view.findViewById(R.id.imgTrabajo);
        btnGuardarTrabajo = view.findViewById(R.id.btnGuardarTrabajo);
        btnSubirImagen = view.findViewById(R.id.btnSeleccionarImagen);

        // Verifica que los elementos no sean null
        if (btnGuardarTrabajo == null) {
            Log.e("InsertarTrabajoFragment", "El botón btnGuardarTrabajo no se encuentra en el layout");
        }
        if (btnSubirImagen == null) {
            Log.e("InsertarTrabajoFragment", "El botón btnSubirImagen no se encuentra en el layout");
        }

        // Inicializar ViewModel
        trabajoViewModel = new ViewModelProvider(this).get(TrabajoRealizadosViewModel.class);

        // Inicializar Firebase Storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        // Configurar los botones
        btnGuardarTrabajo.setOnClickListener(v -> guardarTrabajo());
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
            imgTrabajo.setImageURI(imageUri);  // Muestra la imagen seleccionada en el ImageView
            Toast.makeText(getContext(), "Imagen seleccionada", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarTrabajo() {
        String nombreTrabajo = etNombreTrabajo.getText().toString().trim();

        // Validación: Asegurarse de que el nombre no esté vacío
        if (nombreTrabajo.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingrese un nombre para el trabajo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Valor por defecto de imagen, en caso de no seleccionar una imagen
        final String[] imagenTrabajo = {"sin-imagen"}; // Aquí es donde se guarda la URL si se seleccionó una imagen

        if (imageUri != null) {
            // Subir imagen a Firebase Storage
            StorageReference imageRef = storageReference.child("trabajos/" + System.currentTimeMillis() + ".jpg");
            UploadTask uploadTask = imageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // La URL de la imagen se obtiene correctamente
                    imagenTrabajo[0] = uri.toString();  // Asignamos la URL de la imagen

                    // Crear el objeto Trabajo
                    Trabajo trabajo = new Trabajo(nombreTrabajo, imagenTrabajo[0]);

                    // Guardar el trabajo en la base de datos (Firestore)
                    //trabajoViewModel.agregarTrabajo(trabajo);

                    Toast.makeText(getContext(), "Trabajo guardado", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Si no se seleccionó una imagen, guardar el trabajo con el valor por defecto
            Trabajo trabajo = new Trabajo(nombreTrabajo, imagenTrabajo[0]);

            // Guardar el trabajo en la base de datos (Firestore)
            trabajoViewModel.agregarTrabajo(trabajo);

            Toast.makeText(getContext(), "Trabajo guardado sin imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        etNombreTrabajo.setText("");
        imgTrabajo.setImageURI(null);
        imageUri = null;
    }
}
