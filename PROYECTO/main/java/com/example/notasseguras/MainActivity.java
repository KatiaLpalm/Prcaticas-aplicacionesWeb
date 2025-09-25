// Paquete base de la app
package com.example.notasseguras;

// Importaciones necesarias para la Activity
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Clase principal MainActivity: pantalla de inicio de sesión
public class MainActivity extends AppCompatActivity {

    // Referencias a las vistas de la interfaz
    private EditText etUsername, etPassword; // Campos de texto para usuario y contraseña
    private TextView tvResult;               // Texto para mostrar errores
    private Button btnLogin;                 // Botón de inicio de sesión

    // Credenciales de prueba (usuario/contraseña fijas)
    private static final String DEMO_USER = "demo@agenda.com";
    private static final String DEMO_PASS = "123456";

    // TAG para depuración con Logcat
    private static final String TAG = "LoginDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Vinculamos esta Activity con el layout activity_main.xml
        setContentView(R.layout.activity_main);

        // Asignamos las variables a las vistas definidas en el XML
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvResult   = findViewById(R.id.tvResult);
        btnLogin   = findViewById(R.id.btnLogin);

        // Ocultamos el mensaje de error al inicio
        tvResult.setVisibility(View.INVISIBLE);

        // Configuramos la acción al hacer clic en el botón
        btnLogin.setOnClickListener(v -> doLogin());
    }

    // Método que valida las credenciales e inicia sesión
    private void doLogin() {
        // Obtenemos el texto de los campos de usuario y contraseña
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validación: si el usuario está vacío, marcamos error
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Ingresa tu usuario");
            return;
        }

        // Validación: si la contraseña está vacía, marcamos error
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Ingresa tu contraseña");
            return;
        }

        // Mensaje de depuración (solo visible en Logcat)
        Log.d(TAG, "Usuario: " + username + " | Password len: " + password.length());

        // Verificamos si coincide con las credenciales de prueba
        if (DEMO_USER.equals(username) && DEMO_PASS.equals(password)) {
            // Si el login es correcto
            Log.d(TAG, "Login OK. Abriendo NotesActivity...");
            Toast.makeText(this, "Bienvenida/o", Toast.LENGTH_SHORT).show();

            // Creamos un Intent para abrir NotesActivity
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);

            // finish() cierra esta Activity para que no se pueda volver atrás
            finish();
        } else {
            // Si las credenciales son incorrectas
            Log.d(TAG, "Login FAIL");
            tvResult.setText("Usuario o contraseña incorrectos");
            tvResult.setVisibility(View.VISIBLE); // Mostramos el mensaje de error
        }
    }
}
