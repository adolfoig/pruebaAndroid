package com.example.proyectoglobal;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.proyectoglobal.databinding.ActivityMainBinding;
import com.example.proyectoglobal.ui.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());

        // Inicializamos FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Verificamos si el usuario está autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Si el usuario no está autenticado, redirigir al login
            mostrarLoginFragment();
        } else {
            // Si el usuario está autenticado, configurar la navegación como de costumbre
            configurarNavegacion();
        }
    }

    private void mostrarLoginFragment() {
        // Si el usuario no está autenticado, mostramos el LoginFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment()) // Asegúrate de que el contenedor esté correctamente definido en el layout
                .commit();
    }

    private void configurarNavegacion() {
        // Si el usuario está autenticado, configuramos la navegación
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.registrarProductos, R.id.insertarTrabajo, R.id.trabajosRealizados, R.id.presupuestos)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        setSupportActionBar(binding.toolbar);
        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_fragment)).getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        NavigationUI.setupWithNavController(binding.toolbar, navController, mAppBarConfiguration);
        binding.bottomNavigation.setItemIconTintList(null);
    }
}
