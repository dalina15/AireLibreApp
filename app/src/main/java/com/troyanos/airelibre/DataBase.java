package com.troyanos.airelibre;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DataBase extends AppCompatActivity {

    public EditText base_de_datos;
    public Button busqueda;
    public TextView resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        base_de_datos=findViewById(R.id.base_de_datos);
        busqueda=findViewById(R.id.busqueda);
        resultado=findViewById(R.id.resultado);

        busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();

                String n= base_de_datos.getText().toString();
                String direc_espacios = databaseAccess.getInfo(n);

                resultado.setText(direc_espacios);
                databaseAccess.close();
            }
        });
    }
}