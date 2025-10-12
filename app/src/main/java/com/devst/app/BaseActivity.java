package com.devst.app;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("config", MODE_PRIVATE);
        int index = prefs.getInt("idiomaSeleccionado", 0);
        String[] codigos = {"es", "en", "pt", "fr"};
        String idioma = codigos[index];

        super.attachBaseContext(LocaleHelper.wrap(newBase, idioma));
    }
}