package com.example.compraagro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.compraagro.DetailActivity;
import com.example.compraagro.EditActivity;
import com.example.compraagro.R;
import com.example.compraagro.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements View.OnClickListener {


    private Context mContext;
    ArrayList<Product> mProduct;
    private View.OnClickListener listener;

    public ProductAdapter(Context mContext, ArrayList<Product> mProduct){
        this.mContext = mContext;
        this.mProduct = mProduct;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_market,parent,false);

        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Product product = mProduct.get(position);
        holder.producto.setText(product.getNombre());
        holder.descripcion.setText(product.getDescripcion());
        holder.cantidad.setText(product.getCantidad());
        holder.precio.setText("S/ "+product.getPrecio());
        holder.fecha.setText(product.getFecha());
        Glide.with(mContext).load(product.getUrlImagen()).into(holder.imagen);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(product.getIdUsuario().equals(firebaseUser.getUid())){

                    Intent intent = new Intent(mContext, EditActivity.class);
                    intent.putExtra("id", product.getIdProducto());
                    intent.putExtra("idUser", product.getIdUsuario());
                    mContext.startActivity(intent);
                } else {

                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id", product.getIdProducto());
                    intent.putExtra("idUser", product.getIdUsuario());
                    mContext.startActivity(intent);
                }

            }
        });

//        holder.imagen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, NewsDetailActivity.class);
//                intent.putExtra("newsid", news.getId());
//                mContext.startActivity(intent);
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView producto;
        private TextView descripcion;
        private TextView cantidad;
        private TextView precio;
        private TextView fecha;
        private ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            producto = itemView.findViewById(R.id.tvNombre);
            descripcion = itemView.findViewById(R.id.tvDescripcion);
            cantidad = itemView.findViewById(R.id.tvCantidad);
            precio = itemView.findViewById(R.id.tvPrecio);
            imagen = itemView.findViewById(R.id.ivProduct);
            fecha = itemView.findViewById(R.id.tvFecha);
        }
    }
}
