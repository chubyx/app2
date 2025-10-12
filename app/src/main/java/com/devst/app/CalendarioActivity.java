package com.devst.app;


import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CalendarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        Button btnAgregarEvento = findViewById(R.id.btnAgregarEvento);
        btnAgregarEvento.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType("vnd.android.cursor.item/event");

            Calendar inicio = Calendar.getInstance();
            inicio.set(2025, Calendar.OCTOBER, 17, 11, 45);

            Calendar fin = Calendar.getInstance();
            fin.set(2025, Calendar.OCTOBER, 17, 2, 0);

            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, inicio.getTimeInMillis());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, fin.getTimeInMillis());
            intent.putExtra(CalendarContract.Events.TITLE, "Reunión de planificación");
            intent.putExtra(CalendarContract.Events.DESCRIPTION, "Definir estructura del proyecto");
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Santiago centro");

            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "No se pudo abrir el calendario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
