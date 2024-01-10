package com.example.agendaonline.Perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendaonline.MenuPrincipal;
import com.example.agendaonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Perfil_Usuario extends AppCompatActivity {

    ImageView imagen_perfill;
    TextView Correo_Perfil, Uid_Perfil;
    EditText Nombres_Perfil, Apellidos_Perfil, Edad_Perfil, Telefono_Perfil, Domicilio_Perfil,
            Universidad_Perfil, Profesion_Perfil;
    Button Guardar_Datos;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        InicializarVariables();
    }

    private void InicializarVariables(){
        imagen_perfill = findViewById(R.id.imagen_perfill);
        Correo_Perfil = findViewById(R.id.Correo_Perfil);
        Uid_Perfil = findViewById(R.id.Uid_Perfil);
        Nombres_Perfil = findViewById(R.id.Nombres_Perfil);
        Apellidos_Perfil = findViewById(R.id.Apellidos_Perfil);
        Edad_Perfil = findViewById(R.id.Edad_Perfil);
        Telefono_Perfil = findViewById(R.id.Telefono_Perfil);
        Domicilio_Perfil = findViewById(R.id.Domicilio_Perfil);
        Universidad_Perfil = findViewById(R.id.Universidad_Perfil);
        Profesion_Perfil = findViewById(R.id.Profesion_Perfil);
        Guardar_Datos = findViewById(R.id.Guardar_Datos);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");



    }

    private void LecturaDeDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //Obtener sus datos
                    String uid = ""+ dataSnapshot.child("uid").getValue();
                    String nombres = ""+dataSnapshot.child("nombres").getValue();
                    String apellidos = "" + dataSnapshot.child("apellidos").getValue();
                    String correo = "" + dataSnapshot.child("correo").getValue();
                    String edad = "" + dataSnapshot.child("edad").getValue();
                    String telefono = "" + dataSnapshot.child("telefono").getValue();
                    String domicilio = "" + dataSnapshot.child("domicilio").getValue();
                    String universidad = "" + dataSnapshot.child("universidad").getValue();
                    String profesion = "" + dataSnapshot.child("profesion").getValue();

                    //SETEO DE DATOS
                    Uid_Perfil.setText(uid);
                    Nombres_Perfil.setText(nombres);
                    Apellidos_Perfil.setText(apellidos);
                    Correo_Perfil.setText(correo);
                    Edad_Perfil.setText(edad);
                    Telefono_Perfil.setText(telefono);
                    Domicilio_Perfil.setText(domicilio);
                    Universidad_Perfil.setText(universidad);
                    Profesion_Perfil.setText(profesion);

                }
                else {
                    Toast.makeText(Perfil_Usuario.this, "Esperando datos", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Perfil_Usuario.this, ""+ databaseError, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void ComrpobarInicioSeseion(){
        if (user!=null){
            LecturaDeDatos();
        }else {
            startActivity(new Intent(Perfil_Usuario.this, MenuPrincipal.class));
            finish();
        }
    }

    protected void onStart(){
        ComrpobarInicioSeseion();
        super.onStart();
    }

}