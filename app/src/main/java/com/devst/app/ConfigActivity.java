package com.devst.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ConfigActivity extends AppCompatActivity {

    private Switch switchNotificaciones, switchModoOscuro;
    private Button btnIdioma, btnCallCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        switchNotificaciones = findViewById(R.id.switchNotificaciones);
        switchModoOscuro = findViewById(R.id.switchModoOscuro);
        btnIdioma = findViewById(R.id.btnIdioma);
        btnCallCenter = findViewById(R.id.btnCallCenter);

        // Activar/desactivar notificaciones (solo ejemplo)
        switchNotificaciones.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String msg = isChecked ? "Notificaciones activadas" : "Notificaciones desactivadas";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        // Cambiar modo oscuro
        switchModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        // Cambiar idioma (ejemplo: abrir configuración del sistema)
        btnIdioma.setOnClickListener(v -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        });

        // Llamar al Call Center (Intent implícito)
        btnCallCenter.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+56912345678"));
            startActivity(intent);
        });
    }
}
