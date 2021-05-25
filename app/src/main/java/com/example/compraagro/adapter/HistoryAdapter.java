package com.example.compraagro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.compraagro.DetailActivity;
import com.example.compraagro.R;
import com.example.compraagro.model.Product;
import com.example.compraagro.model.Transaction;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    ArrayList<Transaction> mTransaction;
    private View.OnClickListener listener;

    public HistoryAdapter(Context mContext, ArrayList<Transaction> mTransaction){
        this.mContext = mContext;
        this.mTransaction = mTransaction;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_history,parent,false);

        view.setOnClickListener(this);

        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

        final Transaction product = mTransaction.get(position);
        holder.producto.setText(product.getNameProduct());
        holder.descripcion.setText(product.getState()+" el "+product.getDate());
        holder.cantidad.setText(product.getWeight());
        holder.precio.setText(product.getPrice());
//
//        Glide.with(mContext).load(product.getUrlImagen()).into(holder.imagen);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, DetailActivity.class);
//                intent.putExtra("id", product.getIdProducto());
//                mContext.startActivity(intent);
//            }
//        });

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
        return mTransaction.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView producto;
        private TextView descripcion;
        private TextView cantidad;
        private TextView precio;
        //private ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            producto = itemView.findViewById(R.id.tvNameTransaction);
            descripcion = itemView.findViewById(R.id.tvDescription);
            cantidad = itemView.findViewById(R.id.tvWeight);
            precio = itemView.findViewById(R.id.tvPrice);
            //imagen = itemView.findViewById(R.id.ivProduct);
        }
    }

}
