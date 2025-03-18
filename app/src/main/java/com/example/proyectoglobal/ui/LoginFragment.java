package com.example.proyectoglobal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.example.proyectoglobal.MainActivity;
import com.example.proyectoglobal.R;
import com.example.proyectoglobal.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private GoogleSignInClient googleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflamos la vista y configuramos el binding
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        configurarClienteGoogleSignIn();
        inicializarLauncherGoogleSignIn();

        // Evento para el botón de Google Sign-In
        binding.googleSignInButton.setOnClickListener(v -> signInWithGoogle());

        // Evento para inicio de sesión
        binding.loginButton.setOnClickListener(v -> loginUser());

        // Evento para abrir el fragmento de registro
        binding.registerTextView.setOnClickListener(v -> abrimosRegistroFragment());

        return binding.getRoot(); // Retornamos la vista inflada
    }

    private void configurarClienteGoogleSignIn() {
        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Usa tu Web client ID
                .requestEmail()
                .build();

        // Inicializar Google Sign-In
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void inicializarLauncherGoogleSignIn() {
        // Inicializar el ActivityResultLauncher para manejar la respuesta de Google Sign-In
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        gestionarResultadoSignIn(task);
                    } else {
                        Toast.makeText(getActivity(), "Error en el inicio de sesión con Google", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void gestionarResultadoSignIn(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Toast.makeText(getActivity(), "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Inicio de sesión con Google exitoso", Toast.LENGTH_SHORT).show();
                        redirectToMain();
                    } else {
                        Toast.makeText(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish(); // Añadir esta línea
    }

    private void loginUser() {
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        // Validación de campos vacíos
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser usuario = mAuth.getCurrentUser();
                        Toast.makeText(getActivity(), "Inicio de sesión exitoso: " + usuario.getEmail(), Toast.LENGTH_SHORT).show();
                        redirectToMain();
                    } else {
                        Toast.makeText(getActivity(), "Error debes registrarte primero: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void abrimosRegistroFragment() {
        RegistroFragment registroFragment = new RegistroFragment();

        // Reemplazar el fragmento actual por el de registro
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, registroFragment)
                .addToBackStack(null) // Añadir a la pila de retroceso
                .commit();
    }
}
