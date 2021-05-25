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
import com.example.compraagro.R;
import com.example.compraagro.model.Commentary;
import com.example.compraagro.model.Product;

import java.util.ArrayList;

public class CommentaryAdapter extends RecyclerView.Adapter<CommentaryAdapter.ViewHolder> implements View.OnClickListener {


    private Context mContext;
    ArrayList<Commentary> mCommentary;
    private View.OnClickListener listener;

    public CommentaryAdapter(Context mContext, ArrayList<Commentary> mCommentary){
        this.mContext = mContext;
        this.mCommentary = mCommentary;
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

    @NonNull
    @Override
    public CommentaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_commentary,parent,false);

        view.setOnClickListener(this);

        return new CommentaryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentaryAdapter.ViewHolder holder, int position) {
        final Commentary commentary = mCommentary.get(position);
        holder.tvTitle.setText(commentary.getTitle());
        holder.tvDescription.setText(commentary.getDescription());
        holder.tvNameComentator.setText(commentary.getNameCommentator());
        holder.tvStars.setText(commentary.getStars());

//        Glide.with(mContext).load(commentary.getUrlImagen()).into(holder.imagen);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, DetailActivity.class);
//                intent.putExtra("id", product.getIdProducto());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mCommentary.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvNameComentator;
        private TextView tvStars;
        //private ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvNameComentator = itemView.findViewById(R.id.tvNameComentator);
            tvStars = itemView.findViewById(R.id.tvStars);
            //imagen = itemView.findViewById(R.id.ivProduct);
        }
    }

}
