package com.example.agendaonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendaonline.AgregarNota.Agregar_Nota;
import com.example.agendaonline.ListarNotas.Listar_Notas;
import com.example.agendaonline.NotasImportantes.Notas_Importantes;
import com.example.agendaonline.Perfil.Perfil_Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {

    Button CerrarSesion, AgregarNotas, ListarNotas, Importantes, Contactos, AcercaDe;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView UidPrincipal, NombrePrincipal, CorreoPrincipal;
    Button EstadoCuentaPrincipal;
    ProgressBar progressBarDatos;
    ProgressDialog progressDialog;

    LinearLayoutCompat Linear_Nombres, Linear_Correo, Linear_Verificacion;

    DatabaseReference Usuarios;

    Dialog dialog_cuenta_verificada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        UidPrincipal = findViewById(R.id.UidPrincipal);
        NombrePrincipal = findViewById(R.id.NombrePrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);
        EstadoCuentaPrincipal = findViewById(R.id.EstadoCuentaPrincipal);
        progressBarDatos = findViewById(R.id.progressBarDatos);

        dialog_cuenta_verificada = new Dialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor...");
        progressDialog.setCanceledOnTouchOutside(false);

        Linear_Nombres = findViewById(R.id.Linear_Nombres);
        Linear_Correo = findViewById(R.id.Linear_Correo);
        Linear_Verificacion = findViewById(R.id.Linear_Verificacion);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        AgregarNotas = findViewById(R.id.AgregarNotas);
        ListarNotas = findViewById(R.id.ListarNotas);
        Importantes = findViewById(R.id.Importantes);
        Contactos = findViewById(R.id.Contactos);
        AcercaDe = findViewById(R.id.AcercaDe);
        CerrarSesion = findViewById(R.id.CerrarSesion);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        EstadoCuentaPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isEmailVerified()){
                    //Si la cuenta esta verificada
                    //Toast.makeText(MenuPrincipal.this, "Cuenta ya verificada", Toast.LENGTH_SHORT).show();
                    AnimacionCuentaVerificada();
                }else {
                    //Si la cuenta no esta verificada
                    VerificarCuentaCorrreo();
                }
            }
        });

        AgregarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Obtenemos la informacion de los TextView*/
                String uid_usuario = UidPrincipal.getText().toString();
                String correo_usuario = CorreoPrincipal.getText().toString();

                /*Pasamos datos a la siguiente actividad*/
                Intent intent = new Intent(MenuPrincipal.this, Agregar_Nota.class);
                intent.putExtra("Uid", uid_usuario);
                intent.putExtra("Correo", correo_usuario);
                startActivity(intent);

            }
        });

        ListarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, Listar_Notas.class));
                Toast.makeText(MenuPrincipal.this, "Listar notas", Toast.LENGTH_SHORT).show();
            }
        });

        Importantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, Notas_Importantes.class));
                Toast.makeText(MenuPrincipal.this, "Notas Archivadas", Toast.LENGTH_SHORT).show();
            }
        });

        Contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class));
                Toast.makeText(MenuPrincipal.this, "Perfil Usuario", Toast.LENGTH_SHORT).show();
            }
        });

        AcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuPrincipal.this, "Acerca de ", Toast.LENGTH_SHORT).show();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalirAplicacion();
            }
        });

    }

    private void VerificarCuentaCorrreo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verificar cuenta")
                .setMessage("¿Estas seguro(a) de enviar instrucciones de verificacion a su correo electronico? "
                + user.getEmail())
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EnviarCorreoAVerificacion();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MenuPrincipal.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void EnviarCorreoAVerificacion() {

        progressDialog.setMessage("Enviando intrucciones de verificacion a su correo electronico " + user.getEmail());
        progressDialog.show();
        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Envio fue existoso
                        progressDialog.dismiss();
                        Toast.makeText(MenuPrincipal.this, "Instrucciones enviadas, revise su bandeja " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Si fallo el envio
                        Toast.makeText(MenuPrincipal.this, "Fallo debido a: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void VerificarEstadoDeCuenta(){

        String Verificado = "Verificado";
        String No_Verificado = "No Verificado";
        if(user.isEmailVerified()){
            EstadoCuentaPrincipal.setText(Verificado);
            EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(54, 178, 14));
        }else {
            EstadoCuentaPrincipal.setText(No_Verificado);
            EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(231,76,60));
        }
    }

    private void AnimacionCuentaVerificada(){
        Button EntendidoVerificado;

        dialog_cuenta_verificada.setContentView(R.layout.dialogo_cuenta_verificada);

        EntendidoVerificado = dialog_cuenta_verificada.findViewById(R.id.EntendidoVerificado);

        EntendidoVerificado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_cuenta_verificada.dismiss();
            }
        });

        dialog_cuenta_verificada.show();
        dialog_cuenta_verificada.setCanceledOnTouchOutside(false);

    }

    @Override
    protected void onStart() {
        ComprobarIniciosSesion();
        super.onStart();

    }

    private void ComprobarIniciosSesion(){
        if(user!=null){
            //El usuario ha iniciado sesion
            CargaDeDatos();
        }else {
            //Lo dirigira al MainActivity
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }

    private void CargaDeDatos(){

        VerificarEstadoDeCuenta();

        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Si el usuario existe
                if(snapshot.exists()){
                    //El progressbar se oculta
                    progressBarDatos.setVisibility(View.GONE);
                    //Los TextView se muestran
                    //UidPrincipal.setVisibility(View.VISIBLE);
                    //NombrePrincipal.setVisibility(View.VISIBLE);
                    //CorreoPrincipal.setVisibility(View.VISIBLE);
                    Linear_Nombres.setVisibility(View.VISIBLE);
                    Linear_Correo.setVisibility(View.VISIBLE);
                    Linear_Verificacion.setVisibility(View.VISIBLE);

                    //Obtenr los datos
                    String uid = "" + snapshot.child("uid").getValue();
                    String nombres = "" + snapshot.child("nombres").getValue();
                    String correo = "" + snapshot.child("correo").getValue();

                    //Setear los datos en los respectivos TextView
                    UidPrincipal.setText(uid);
                    NombrePrincipal.setText(nombres);
                    CorreoPrincipal.setText(correo);

                    //Habilitar los botones del menu
                    AgregarNotas.setEnabled(true);
                    ListarNotas.setEnabled(true);
                    Importantes.setEnabled(true);
                    Contactos.setEnabled(true);
                    AcercaDe.setEnabled(true);
                    CerrarSesion.setEnabled(true);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Perfil_usuario){
            startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesion exitosamente", Toast.LENGTH_SHORT).show();
    }
}