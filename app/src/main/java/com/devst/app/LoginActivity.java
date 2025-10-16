package com.devst.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPass;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPass  = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> intentoInicioSesion());

        findViewById(R.id.tvRecuperarpass).setOnClickListener(v ->
                Toast.makeText(this, "Función pendiente: recuperar contraseña", Toast.LENGTH_SHORT).show());

        findViewById(R.id.tvCrear).setOnClickListener(v ->
                Toast.makeText(this, "Función pendiente: crear cuenta", Toast.LENGTH_SHORT).show());
    }

    private void intentoInicioSesion() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";
        String pass  = edtPass.getText()  != null ? edtPass.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Ingresa tu correo");
            edtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Correo inválido");
            edtEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            edtPass.setError("Ingresa tu contraseña");
            edtPass.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            edtPass.setError("Mínimo 6 caracteres");
            edtPass.requestFocus();
            return;
        }

        // Leer datos desde perfil_data
        SharedPreferences perfilPrefs = getSharedPreferences("perfil_data", MODE_PRIVATE);
        String correoGuardado = perfilPrefs.getString("correo", "estudiante@st.cl");
        String contrasenaGuardada = perfilPrefs.getString("contrasena", "123456");

        boolean ok = email.equals(correoGuardado) && pass.equals(contrasenaGuardada);
        if (ok) {
            Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();

            // Guardar sesión activa
            SharedPreferences sesionPrefs = getSharedPreferences("user_session", MODE_PRIVATE);
            sesionPrefs.edit().putBoolean("sesion_activa", true).apply();

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("email_usuario", email);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}