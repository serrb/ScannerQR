package com.serrb.scannerqr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.integration.android.IntentIntegrator
import com.serrb.scannerqr.ui.theme.ScannerQRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScannerQRTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pasamos el contexto de la actividad actual
                    Greeting(context = this)
                }
            }
        }
    }

    @Composable
    fun Greeting(context: Context) { //le pasamos el contexto
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) { //centramos el boton
            Button(
                onClick = { initScanner(context) }, // Usamos el contexto proporcionado
                shape = RoundedCornerShape(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF8D363),
                    contentColor = Color(0xFF000000)
                ),
                modifier = Modifier
                    .height(220.dp).width(220.dp)
                    .height(170.dp),
                content = {
                    Text(
                        text = "ESCANEAR QR",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(0.6f)
                    )
                }
            )
        }
    }

    //iniciamos funcionalidad de escaneo de la biblioteca IntentIntegrator de ZXING
    private fun initScanner(context: Context) {
        val integrator = IntentIntegrator(context as ComponentActivity)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // establecemos que solo queremos escanear QR's
        integrator.setPrompt("Bienvenido")
        integrator.setTorchEnabled(false) //opcion de activar el flash
        integrator.setBeepEnabled(true) //aviso de que lo escanea correctamente
        integrator.initiateScan()
    }

    //manejamos la respuesta una vez escaneemos el QR
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) { //cuando ha fallado
            if (result.contents == null) { // si es igual a null
                Toast.makeText(this, "cancelado", Toast.LENGTH_SHORT).show()
            } else {
                handleQRCodeResult(result.contents, this)
            }
        }
    }

    //funcion para abrir navegador si es una URL:
    private fun handleQRCodeResult(result: String, context: Context) {
        if (result.startsWith("http://") || result.startsWith("https://")) {
            // Si el resultado es una URL, abrir en el navegador web
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result))
            context.startActivity(intent)
        } else {
            // Si el resultado no es una URL, mostrar un mensaje o realizar alguna otra acción
            Toast.makeText(context, "El valor escaneado no es una URL,El valor escaneado es $result ", Toast.LENGTH_SHORT).show()
        }
    }
}
