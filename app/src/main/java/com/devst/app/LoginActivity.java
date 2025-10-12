package com.devst.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;


import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {

    //Creamos variables
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
        String pass  = edtPass.getText()  != null ? edtPass.getText().toString() : "";

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

        // Simulación de login
        boolean ok = email.equals("estudiante@st.cl") && pass.equals("123456");
        if (ok) {
            Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();

            // Ir al nuevo Activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            // Enviamos el email al Home
            intent.putExtra("email_usuario", email);
            startActivity(intent);

            // Cerrar la pantalla de login para que no vuelva atrás con "Back"
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }

}