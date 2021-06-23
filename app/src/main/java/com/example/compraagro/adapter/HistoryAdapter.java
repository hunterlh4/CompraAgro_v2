package com.example.compraagro.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.compraagro.DetailActivity;
import com.example.compraagro.R;
import com.example.compraagro.model.Product;
import com.example.compraagro.model.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        final Transaction transaction = mTransaction.get(position);
        holder.producto.setText(transaction.getNameProduct());
        holder.descripcion.setText(transaction.getState()+" el "+transaction.getDate());
        holder.cantidad.setText(transaction.getWeight());
        holder.precio.setText(transaction.getPrice());

        if(transaction.getState().equals("Reservado") ){

            holder.background.setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorReservado));
        } else if(transaction.getState().equals("Vendido") ){

            holder.background.setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorVendido));
        }else if(transaction.getState().equals("Rechazado") ){

            holder.background.setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorFallido));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

                if(transaction.getIdSeller().equals(firebaseUser.getUid()) && transaction.getState().equals("Reservado")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Aceptar transaccion")
                            .setMessage("Al aceptar usted como vendedor enviara los productos al comprador.")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    denyTransaction(transaction.getIdTransaction());
                                }
                            })
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    acceptTransaction(transaction.getIdTransaction());
                                }
                            });
                    builder.create().show();
                } else {

                    Toast.makeText(mContext,holder.descripcion.getText(),Toast.LENGTH_SHORT).show();
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
        return mTransaction.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView producto;
        private TextView descripcion;
        private TextView cantidad;
        private TextView precio;
        private LinearLayout background;
        //private ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            producto = itemView.findViewById(R.id.tvNameTransaction);
            descripcion = itemView.findViewById(R.id.tvDescription);
            cantidad = itemView.findViewById(R.id.tvWeight);
            precio = itemView.findViewById(R.id.tvPrice);
            background = itemView.findViewById(R.id.background);
            //imagen = itemView.findViewById(R.id.ivProduct);
        }
    }

    private void acceptTransaction(String idTransaction){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transactions");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transactions = snapshot.getValue(Transaction.class);

                    if(transactions.getIdTransaction().equals(idTransaction)){
                        transactions.setState("Vendido");
                        reference.child(transactions.getIdTransaction()).setValue(transactions);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void denyTransaction(String idTransaction){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transactions");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transactions = snapshot.getValue(Transaction.class);

                    if(transactions.getIdTransaction().equals(idTransaction)){
                        transactions.setState("Rechazado");
                        reference.child(transactions.getIdTransaction()).setValue(transactions);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
