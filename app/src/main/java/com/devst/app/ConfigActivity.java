package com.devst.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class ConfigActivity extends AppCompatActivity {

    private Switch switchNotificaciones, switchModoOscuro;
    private CheckBox checkActualizaciones;
    private EditText editCorreo;
    private Spinner spinnerIdioma;
    private Button btnIdioma, btnCallCenter, btnEnviarFeedback;

    private SharedPreferences prefs;
    private final String[] codigosIdioma = {"es", "en", "pt", "fr"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar idioma guardado antes de cargar la vista
        prefs = getSharedPreferences("config", MODE_PRIVATE);
        int idiomaGuardado = prefs.getInt("idiomaSeleccionado", 0);
        String codigoIdioma = codigosIdioma[idiomaGuardado];
        aplicarIdioma(codigoIdioma);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Inicializar componentes
        switchNotificaciones = findViewById(R.id.switchNotificaciones);
        switchModoOscuro = findViewById(R.id.switchModoOscuro);
        checkActualizaciones = findViewById(R.id.checkActualizaciones);
        editCorreo = findViewById(R.id.editCorreo);
        spinnerIdioma = findViewById(R.id.spinnerIdioma);
        btnIdioma = findViewById(R.id.btnIdioma);
        btnCallCenter = findViewById(R.id.btnCallCenter);
        btnEnviarFeedback = findViewById(R.id.btnEnviarFeedback);

        // Cargar idiomas en el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.idiomas_disponibles,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapter);
        spinnerIdioma.setSelection(idiomaGuardado);

        // Notificaciones
        switchNotificaciones.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String msg = isChecked ? "Notificaciones activadas" : "Notificaciones desactivadas";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        // Modo oscuro
        switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // Actualizaciones automáticas
        checkActualizaciones.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String msg = isChecked ? "Actualizaciones automáticas activadas" : "Actualizaciones desactivadas";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        // Validar correo ingresado
        editCorreo.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String correo = editCorreo.getText().toString();
                if (!correo.contains("@") || !correo.contains(".")) {
                    Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Cambiar idioma y aplicar
        btnIdioma.setOnClickListener(v -> {
            int index = spinnerIdioma.getSelectedItemPosition();
            String idioma = spinnerIdioma.getSelectedItem().toString();
            String[] codigos = {"es", "en", "pt", "fr"};
            String codigo = codigos[index];

            prefs.edit().putInt("idiomaSeleccionado", index).apply();
            Toast.makeText(this, "Idioma seleccionado: " + idioma, Toast.LENGTH_SHORT).show();

            // Reiniciar actividad para aplicar idioma
            reiniciarConIdioma(codigo);
        });

        // Llamar al Call Center
        btnCallCenter.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+56912345678"));
            startActivity(intent);
        });

        // Enviar sugerencias
        btnEnviarFeedback.setOnClickListener(v -> {
            String numero = "+56912345678";
            String mensaje = "Hola, tengo una sugerencia para mejorar la app.";

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + numero));
            intent.putExtra("sms_body", mensaje);

            try {
                startActivity(intent); // omitimos resolveActivity()
            } catch (Exception e) {
                Toast.makeText(this, "No se pudo abrir la app de mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Aplica el idioma antes de cargar la vista
    private void aplicarIdioma(String codigoIdioma) {
        Locale nuevoLocale = new Locale(codigoIdioma);
        Locale.setDefault(nuevoLocale);
        Configuration config = new Configuration();
        config.setLocale(nuevoLocale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    // Reinicia la actividad con el nuevo idioma
    private void reiniciarConIdioma(String codigoIdioma) {
        aplicarIdioma(codigoIdioma);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}