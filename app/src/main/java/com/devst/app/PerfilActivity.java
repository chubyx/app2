package com.devst.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    private EditText editCorreo, editContrasena, editNombre;
    private Button btnEditar, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        // Referencias a los elementos de la vista
        editNombre = findViewById(R.id.txtNombre);
        editCorreo = findViewById(R.id.txtCorreo);
        editContrasena = findViewById(R.id.txtContrasena);
        btnEditar = findViewById(R.id.btnEditar);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Leer datos desde perfil_data
        SharedPreferences perfilPrefs = getSharedPreferences("perfil_data", MODE_PRIVATE);
        String nombreGuardado = perfilPrefs.getString("nombre", "Nombre Usuario");
        String correoGuardado = perfilPrefs.getString("correo", "estudiante@st.cl");
        String contrasenaGuardada = perfilPrefs.getString("contrasena", "123456");

        editNombre.setText(nombreGuardado);
        editCorreo.setText(correoGuardado);
        editContrasena.setText(contrasenaGuardada);

        // Acción para editar perfil
        btnEditar.setOnClickListener(v -> {
            String nuevoNombre = editNombre.getText().toString().trim();
            String nuevoCorreo = editCorreo.getText().toString().trim();
            String nuevaContrasena = editContrasena.getText().toString().trim();

            if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty() || nuevaContrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar datos en perfil_data
            perfilPrefs.edit()
                    .putString("nombre", nuevoNombre)
                    .putString("correo", nuevoCorreo)
                    .putString("contrasena", nuevaContrasena)
                    .apply();

            Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();

            // Enviar el nombre editado como resultado
            Intent resultIntent = new Intent();
            resultIntent.putExtra("nombre_editado", nuevoNombre);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Acción para cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            // Solo borra la sesión, no el perfil
            SharedPreferences sesionPrefs = getSharedPreferences("user_session", MODE_PRIVATE);
            sesionPrefs.edit().clear().apply();

            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}