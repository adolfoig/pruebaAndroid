package com.example.proyectoglobal.ui.clases.presupuesto;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoglobal.databinding.ViewholderPresupuestoBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PresupuestoAdapter extends RecyclerView.Adapter<PresupuestoAdapter.PresupuestoViewHolder> {

    // Lista de anuncios que se muestra en el RecyclerView
    List<Presupuesto> presupuestoList;

    public PresupuestoAdapter() {
    }


    public PresupuestoAdapter(List<Presupuesto> presupuestoList) {
        this.presupuestoList = presupuestoList;
    }

    @NonNull
    @Override
    public PresupuestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PresupuestoViewHolder(ViewholderPresupuestoBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PresupuestoViewHolder holder, int position) {
        Presupuesto presupuesto = presupuestoList.get(position);
        holder.binding.tvContenido.setText(presupuesto.getNombre());
        holder.binding.tvUsuario.setText("Usuario: " + presupuesto.getGmail());
        holder.binding.tvFecha.setText("Fecha: " + formatearFecha(presupuesto.getFecha()));
    }

    @Override
    public int getItemCount() {
        return presupuestoList != null ? presupuestoList.size() : 0;
    }

    public void setPresupuestoList(List<Presupuesto> anunciosList) {
        this.presupuestoList = anunciosList;
        notifyDataSetChanged();
    }

    // Devuelve el anuncio que esté en la posición pasada por parámetro
    // Lo utilizamos para saber qué anuncio eliminar con el gesto de desplazar
    public Presupuesto obtenerPresupuesto(int posicion) {
        return this.presupuestoList.get(posicion);
    }

    // Elimina el anuncio que esté en la posición pasada por parámetro
    // Lo utilizamos para eliminar el anuncio de la lista
    public void eliminarPresupuesto(int posicion) {
        this.presupuestoList.remove(posicion);
        notifyItemRemoved(posicion);
    }

    // Retorna el timestamp en una fecha con el formato legible
    private String formatearFecha(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    static class PresupuestoViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderPresupuestoBinding binding;

        // Constructor: Asigna la vista inflada al ViewHolder
        public PresupuestoViewHolder(@NonNull ViewholderPresupuestoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}