package com.seuprojeto.mobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seuprojeto.mobile.model.Location
import com.seuprojeto.mobile.ui.components.EmptyStateMessage
import com.seuprojeto.mobile.ui.components.FlashcardTopAppBar
import com.seuprojeto.mobile.ui.components.LocationForm
import com.seuprojeto.mobile.ui.components.LocationItem
import com.seuprojeto.mobile.ui.components.PrimaryButton

/**
 * Tela para gerenciar localizações favoritas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    locations: List<Location>,
    isLoading: Boolean,
    onAddLocation: (Location) -> Unit,
    onEditLocation: (Location) -> Unit,
    onDeleteLocation: (Location) -> Unit,
    onFavoriteLocation: (Location, Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    onGetCurrentLocation: () -> Pair<Double, Double>?,
    modifier: Modifier = Modifier
) {
    var showAddLocationDialog by remember { mutableStateOf(false) }
    var locationToEdit by remember { mutableStateOf<Location?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf<Location?>(null) }
    
    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = "Localizações",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddLocationDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Localização"
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (locations.isEmpty()) {
                EmptyStateMessage(
                    message = "Você ainda não possui localizações favoritas. Clique no botão + para adicionar uma nova.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(locations) { location ->
                        LocationItem(
                            location = location,
                            onEdit = { locationToEdit = it },
                            onDelete = { showDeleteConfirmation = it },
                            onFavorite = onFavoriteLocation
                        )
                    }
                }
            }
        }
    }
    
    // Diálogo para adicionar nova localização
    if (showAddLocationDialog) {
        AlertDialog(
            onDismissRequest = { showAddLocationDialog = false },
            title = { Text("Adicionar Localização") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    LocationForm(
                        onSave = { name, latitude, longitude ->
                            val newLocation = Location(
                                name = name,
                                latitude = latitude,
                                longitude = longitude
                            )
                            onAddLocation(newLocation)
                            showAddLocationDialog = false
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    PrimaryButton(
                        text = "Usar Localização Atual",
                        onClick = {
                            onGetCurrentLocation()?.let { (latitude, longitude) ->
                                // Preencher com a localização atual, mantendo o diálogo aberto
                                showAddLocationDialog = false
                                locationToEdit = Location(
                                    name = "Minha Localização",
                                    latitude = latitude,
                                    longitude = longitude
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = { },
            dismissButton = {
                TextButton(onClick = { showAddLocationDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Diálogo para editar localização
    locationToEdit?.let { location ->
        AlertDialog(
            onDismissRequest = { locationToEdit = null },
            title = { Text("Editar Localização") },
            text = {
                LocationForm(
                    initialName = location.name,
                    initialLatitude = location.latitude,
                    initialLongitude = location.longitude,
                    onSave = { name, latitude, longitude ->
                        val updatedLocation = location.copy(
                            name = name,
                            latitude = latitude,
                            longitude = longitude
                        )
                        onEditLocation(updatedLocation)
                        locationToEdit = null
                    }
                )
            },
            confirmButton = { },
            dismissButton = {
                TextButton(onClick = { locationToEdit = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Diálogo de confirmação de exclusão
    showDeleteConfirmation?.let { location ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Deseja realmente excluir a localização '${location.name}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteLocation(location)
                        showDeleteConfirmation = null
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
} 