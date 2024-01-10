package com.example.agendaonline.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaonline.R;

public class ViewHolder_Nota extends RecyclerView.ViewHolder {
    View mView;

    private ViewHolder_Nota.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); //Se ejecuta al presionar en el item
        void onItemLongClick(View view, int position); //Se ejecuta al mantener presionado en el Item
    }

    public void setOnClickListener(ViewHolder_Nota.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolder_Nota(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mClickListener.onItemClick(v, getAdapterPosition());
            }
        });



        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return false;
            }
        });

    }

    public void SetearDatos(Context context, String id_nota, String uid_usuario, String correo_ususario, String fecha_hora_registro, String titulo,
                            String descripcion, String fecha_nota, String estado){

        //DECLARAR LAS VISTAS
        TextView Id_nota_Item, Uid_Usuario_Item, Correo_Usuario_Item, Fecha_hora_registro_Item, Titulo_Item,
                Descripcion_Item, Fecha_Item, Estado_Item;

        ImageView Tarea_Finalizada_Item, Tarea_No_Finalizada_Item;

        //ESTABLECER LA CONEXCION CON EL ITEM
        Id_nota_Item = mView.findViewById(R.id.Id_nota_Item);
        Uid_Usuario_Item = mView.findViewById(R.id.Uid_Usuario_Item);
        Correo_Usuario_Item = mView.findViewById(R.id.Correo_Usuario_Item);
        Fecha_hora_registro_Item = mView.findViewById(R.id.Fecha_hora_registro_Item);
        Titulo_Item = mView.findViewById(R.id.Titulo_Item);
        Descripcion_Item = mView.findViewById(R.id.Descripcion_Item);
        Fecha_Item = mView.findViewById(R.id.Fecha_Item);
        Estado_Item = mView.findViewById(R.id.Estado_Item);

        Tarea_Finalizada_Item = mView.findViewById(R.id.Tarea_Finalizada_Item);
        Tarea_No_Finalizada_Item = mView.findViewById(R.id.Tarea_No_Finalizada_Item);

        //SETEAR LA INFORMACION DENTRO DEL ITEM
        Id_nota_Item.setText(id_nota);
        Uid_Usuario_Item.setText(uid_usuario);
        Correo_Usuario_Item.setText(correo_ususario);
        Fecha_hora_registro_Item.setText(fecha_hora_registro);
        Titulo_Item.setText(titulo);
        Descripcion_Item.setText(descripcion);
        Fecha_Item.setText(fecha_nota);
        Estado_Item.setText(estado);

        //Gestionamos el color del estado
        if (estado.equals("Finalizado")){
            Tarea_Finalizada_Item.setVisibility(View.VISIBLE);
        }else {
            Tarea_No_Finalizada_Item.setVisibility(View.VISIBLE);
        }


    }
}
