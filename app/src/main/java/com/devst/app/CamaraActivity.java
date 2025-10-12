package com.devst.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamaraActivity extends AppCompatActivity {

    private ImageView imagenPrevia;
    private Uri urlImagen;

    private final ActivityResultLauncher<String> permisoCamaraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), carga -> {
                if (carga) tomarFoto();
                else Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            });

    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
                if (resultado.getResultCode() == RESULT_OK && urlImagen != null) {
                    imagenPrevia.setImageURI(urlImagen);
                    Toast.makeText(this, "Foto guardada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Captura cancelada", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        Button btnTomarFoto = findViewById(R.id.btnTomarFoto);
        imagenPrevia = findViewById(R.id.imgPreview);

        btnTomarFoto.setOnClickListener(v -> checkPermisoYTomar());
    }

    private void checkPermisoYTomar() {
        boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        if (granted) tomarFoto();
        else permisoCamaraLauncher.launch(Manifest.permission.CAMERA);
    }

    private void tomarFoto() {
        try {
            File archivoFoto = crearArchivoImagen();
            urlImagen = FileProvider.getUriForFile(
                    this, getPackageName() + ".fileprovider", archivoFoto);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, urlImagen);

            // Intentar forzar cámara frontal (no garantizado)
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);

            takePictureLauncher.launch(intent);

        } catch (IOException e) {
            Toast.makeText(this, "No se pudo crear el archivo de imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private File crearArchivoImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = "IMG_" + timeStamp + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(nombre, ".jpg", dir);
    }
}
