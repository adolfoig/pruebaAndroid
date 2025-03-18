package com.example.proyectoglobal.ui.clases.subclases.producto;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoglobal.R;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Productos> listaProductos;

    public ProductoAdapter(List<Productos> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout para cada item de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_producto, parent, false);

        if (view != null) {
            Log.d("ProductoAdapter", "Layout inflado correctamente");
        } else {
            Log.e("ProductoAdapter", "Error al inflar el layout");
        }

        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        if (listaProductos != null && !listaProductos.isEmpty()) {
            Productos producto = listaProductos.get(position);

            Log.d("ProductoAdapter", "Producto: " + producto.getNombre() +
                    ", Descripción: " + producto.getDescripcion() +
                    ", Precio: " + producto.getPrecio());

            if (producto != null) {
                if (holder.tvNombreProducto != null) {
                    holder.tvNombreProducto.setText(producto.getNombre());
                }

                if (holder.tvDescripcionProducto != null) {
                    String descripcion = producto.getDescripcion();
                    if (descripcion != null && !descripcion.isEmpty()) {
                        holder.tvDescripcionProducto.setText(descripcion);
                    } else {
                        holder.tvDescripcionProducto.setText("Descripción no disponible");
                    }
                }

                if (holder.tvPrecioProducto != null) {
                    double precio = producto.getPrecio();
                    if (precio > 0) {
                        holder.tvPrecioProducto.setText(String.format("Precio: $%.2f", precio));
                    } else {
                        holder.tvPrecioProducto.setText("Precio no disponible");
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaProductos != null ? listaProductos.size() : 0;
    }

    // Método para actualizar la lista de productos en el Adapter
    public void setProductosList(List<Productos> productos) {
        this.listaProductos = productos;
        notifyDataSetChanged();
    }

    // ViewHolder que mantiene las vistas del item
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProducto, tvDescripcionProducto, tvPrecioProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Vinculamos las vistas del XML con el código Java
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvDescripcionProducto = itemView.findViewById(R.id.tvDescripcionProducto);
            tvPrecioProducto = itemView.findViewById(R.id.tvPrecioProducto);

            if (tvNombreProducto == null || tvDescripcionProducto == null || tvPrecioProducto == null) {
                Log.e("ProductoAdapter", "Error al vincular las vistas en el ViewHolder");
            }
        }
    }
}
