package com.example.proyectoglobal.ui.clases.trabajosRealizados;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectoglobal.R;

import java.util.List;

public class TrabajoAdapter extends RecyclerView.Adapter<TrabajoAdapter.TrabajoViewHolder> {

    private List<Trabajo> trabajosList;

    public TrabajoAdapter(List<Trabajo> trabajosList) {
        this.trabajosList = trabajosList;
    }

    @NonNull
    @Override
    public TrabajoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_trabajo, parent, false);
        return new TrabajoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrabajoViewHolder holder, int position) {
        Trabajo trabajo = trabajosList.get(position);
        holder.tvNombreTrabajo.setText(trabajo.getNombre());

        // Cargar imagen con Glide
        Glide.with(holder.imgTrabajo.getContext())
                .load(trabajo.getImagenUrl())
                .into(holder.imgTrabajo);

        // Crear Bundle con datos del trabajo
        Bundle bundle = new Bundle();
        bundle.putString("trabajoNombre", trabajo.getNombre());

        // Log para verificar datos
        Log.d("TrabajoAdapter", "Trabajo seleccionado: " + trabajo.getNombre());

        // Configurar clic en el item
        holder.itemView.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            if (navController != null) {
                navController.navigate(R.id.action_trabajosRealizados_to_trabajoDetalleFragment, bundle);
            } else {
                Log.e("TrabajoAdapter", "NavController es NULL");
            }
        });
    }

    @Override
    public int getItemCount() {
        return trabajosList.size();
    }

    public void setTrabajosList(List<Trabajo> trabajosList) {
        this.trabajosList = trabajosList;
        notifyDataSetChanged();
    }

    public static class TrabajoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreTrabajo;
        ImageView imgTrabajo;

        public TrabajoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreTrabajo = itemView.findViewById(R.id.tvTituloTrabajo);
            imgTrabajo = itemView.findViewById(R.id.imgTrabajoV);
        }
    }
}
