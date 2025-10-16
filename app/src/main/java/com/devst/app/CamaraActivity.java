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

    //  Permiso de cámara
    private final ActivityResultLauncher<String> permisoCamaraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), carga -> {
                if (carga) tomarFoto();
                else Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            });

    //  Lanzador de cámara
    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
                if (resultado.getResultCode() == RESULT_OK && urlImagen != null) {
                    imagenPrevia.setImageURI(urlImagen);
                    Toast.makeText(this, "Foto guardada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Captura cancelada", Toast.LENGTH_SHORT).show();
                }
            });

    //  Lanzador de galería
    private final ActivityResultLauncher<Intent> abrirGaleriaLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
                if (resultado.getResultCode() == RESULT_OK && resultado.getData() != null) {
                    Uri imagenSeleccionada = resultado.getData().getData();
                    imagenPrevia.setImageURI(imagenSeleccionada);
                    Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Selección cancelada", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        // 🔗 Referencias de vista
        Button btnTomarFoto = findViewById(R.id.btnTomarFoto);
        Button btnAbrirGaleria = findViewById(R.id.btnAbrirGaleria);
        imagenPrevia = findViewById(R.id.imgPreview);

        //  Acciones de botones
        btnTomarFoto.setOnClickListener(v -> checkPermisoYTomar());
        btnAbrirGaleria.setOnClickListener(v -> abrirGaleria());
    }

    //  Verifica permiso antes de abrir cámara
    private void checkPermisoYTomar() {
        boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        if (granted) tomarFoto();
        else permisoCamaraLauncher.launch(Manifest.permission.CAMERA);
    }

    //  Toma la foto y guarda en archivo temporal
    private void tomarFoto() {
        try {
            File archivoFoto = crearArchivoImagen();
            urlImagen = FileProvider.getUriForFile(
                    this, getPackageName() + ".fileprovider", archivoFoto);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, urlImagen);

            // Intentar usar cámara frontal (no garantizado)
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);

            takePictureLauncher.launch(intent);

        } catch (IOException e) {
            Toast.makeText(this, "No se pudo crear el archivo de imagen", Toast.LENGTH_SHORT).show();
        }
    }

    // Abre la galería para seleccionar imagen
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        abrirGaleriaLauncher.launch(intent);
    }

    //  Crea archivo temporal para guardar la foto
    private File crearArchivoImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = "IMG_" + timeStamp + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(nombre, ".jpg", dir);
    }
}