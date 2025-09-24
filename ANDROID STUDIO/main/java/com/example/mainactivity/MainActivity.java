package com.example.miprimeraapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView pantalla;
        Button boton;
        EditText entrada;

        pantalla = findViewById(R.id.screen);
        boton = findViewById(R.id.click);
        entrada = findViewById(R.id.enter);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = entrada.getText().toString();
                pantalla.setText(texto);
                entrada.setText("");
            }
        });
    }
}