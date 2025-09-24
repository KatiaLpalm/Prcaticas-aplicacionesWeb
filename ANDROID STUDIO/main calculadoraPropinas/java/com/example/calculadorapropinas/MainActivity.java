package com.example.calculadorapropinas;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText vista1, vista2;
    RadioGroup radioGroup;
    RadioButton radioBu1, radioBu2, radioBu3;
    TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        vista1 = findViewById(R.id.vista1);
        vista2 = findViewById(R.id.vista2);
        radioGroup = findViewById(R.id.radioGroup);
        radioBu1 = findViewById(R.id.radioBu1);
        radioBu2 = findViewById(R.id.radioBu2);
        radioBu3 = findViewById(R.id.radioBu3);
        resultado = findViewById(R.id.resultado);

        // Listener para detectar cuando se selecciona un porcentaje
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            double porcentaje = 0.0;
            if (checkedId == R.id.radioBu1) porcentaje = 0.05;
            else if (checkedId == R.id.radioBu2) porcentaje = 0.10;
            else if (checkedId == R.id.radioBu3) porcentaje = 0.15;

            calcularPropina(porcentaje);
        });

        // Ajusta el padding para pantallas edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void calcularPropina(double porcentaje) {
        try {
            double cuenta = Double.parseDouble(vista1.getText().toString());
            double personas = Double.parseDouble(vista2.getText().toString());
            if (personas <= 0) {
                resultado.setText("Personas debe ser > 0");
                return;
            }

            double propina = cuenta * porcentaje;
            double totalPorPersona = (cuenta + propina) / personas;
            resultado.setText(String.format("Por persona: %.2f", totalPorPersona));

        } catch (NumberFormatException e) {
            resultado.setText("Ingresa valores válidos");
        }
    }
}
