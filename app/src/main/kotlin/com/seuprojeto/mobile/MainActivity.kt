package com.seuprojeto.mobile

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.seuprojeto.mobile.receiver.GeofenceBroadcastReceiver
import java.util.*

data class LocalFavorito(
    val nome: String,
    val latitude: Double,
    val longitude: Double
)

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var requestForegroundPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>
    private lateinit var requestBackgroundPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)

        configurarSolicitacaoPermissoes()
        solicitarPermissaoForeground()

        setContent {
            val context = LocalContext.current
            var nomeInput by remember { mutableStateOf("") }
            var locaisFavoritos by remember { mutableStateOf(listOf<LocalFavorito>()) }

            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.padding(16.dp).fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Localizador GPS com Favoritos", fontSize = 22.sp)

                        OutlinedTextField(
                            value = nomeInput,
                            onValueChange = { nomeInput = it },
                            label = { Text("Nome do local") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (nomeInput.isNotBlank()) {
                                    fusedLocationClient.lastLocation
                                        .addOnSuccessListener { location ->
                                            if (location != null) {

                                                val locationString = "${location.latitude},${location.longitude}"
                                                Log.d("LOCALIZACAO_STRING", "Localização formatada: $locationString")


                                                locaisFavoritos = locaisFavoritos + LocalFavorito(
                                                    nomeInput,
                                                    location.latitude,
                                                    location.longitude
                                                )
                                                adicionarGeofence(location)
                                                nomeInput = ""
                                            } else {
                                                Toast.makeText(context, "Localização indisponível. Aguarde sinal de GPS.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(context, "Digite o nome do local!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Salvar Local Favorito")
                        }

                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(locaisFavoritos) { local ->
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .animateContentSize(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = local.nome, fontSize = 16.sp)
                                            Text(
                                                text = "Lat: ${local.latitude}, Lng: ${local.longitude}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configurarSolicitacaoPermissoes() {
        requestForegroundPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                iniciarCapturaLocalizacao()
                solicitarPermissaoBackground()
            } else {
                Toast.makeText(this, "Permissão de localização necessária.", Toast.LENGTH_SHORT).show()
            }
        }

        requestBackgroundPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                Toast.makeText(this, "Localização em segundo plano não foi concedida.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun solicitarPermissaoForeground() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestForegroundPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            iniciarCapturaLocalizacao()
            solicitarPermissaoBackground()
        }
    }

    private fun solicitarPermissaoBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestBackgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }
    }

    private fun iniciarCapturaLocalizacao() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                // localização capturada para consultas
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    private fun adicionarGeofence(local: Location) {
        val geofence = Geofence.Builder()
            .setRequestId(UUID.randomUUID().toString())
            .setCircularRegion(local.latitude, local.longitude, 100f)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener { Log.d("GEOFENCE", "Geofence adicionada") }
                .addOnFailureListener { Log.e("GEOFENCE", "Erro ao adicionar", it) }
        }
    }
}
