package com.seuprojeto.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seuprojeto.mobile.model.Location

/**
 * Item que exibe uma localização favorita do usuário.
 */
@Composable
fun LocationItem(
    location: Location,
    onEdit: (Location) -> Unit,
    onDelete: (Location) -> Unit,
    onFavorite: (Location, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone de localização
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Informações da localização
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Lat: ${location.latitude.format(6)}, Lng: ${location.longitude.format(6)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Botão de favorito
            IconButton(
                onClick = { onFavorite(location, !location.lastUsed) }
            ) {
                Icon(
                    imageVector = if (location.lastUsed) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = if (location.lastUsed) "Remover dos favoritos" else "Adicionar aos favoritos",
                    tint = if (location.lastUsed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Botão de edição
            IconButton(
                onClick = { onEdit(location) }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Botão de exclusão
            IconButton(
                onClick = { onDelete(location) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Formulário para adicionar ou editar uma localização.
 */
@Composable
fun LocationForm(
    initialName: String = "",
    initialLatitude: Double = 0.0,
    initialLongitude: Double = 0.0,
    onSave: (String, Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(initialName) }
    var latitudeText by remember { mutableStateOf(initialLatitude.toString()) }
    var longitudeText by remember { mutableStateOf(initialLongitude.toString()) }
    
    // Estados de erro
    var nameError by remember { mutableStateOf<String?>(null) }
    var latitudeError by remember { mutableStateOf<String?>(null) }
    var longitudeError by remember { mutableStateOf<String?>(null) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo de nome
        OutlinedTextField(
            value = name,
            onValueChange = { 
                name = it
                nameError = if (it.isBlank()) "O nome não pode estar vazio" else null
            },
            label = { Text("Nome da localização") },
            isError = nameError != null,
            supportingText = nameError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Campo de latitude
        OutlinedTextField(
            value = latitudeText,
            onValueChange = { 
                latitudeText = it
                latitudeError = try {
                    val lat = it.toDouble()
                    if (lat < -90 || lat > 90) "Latitude deve estar entre -90 e 90" else null
                } catch (e: NumberFormatException) {
                    "Latitude inválida"
                }
            },
            label = { Text("Latitude") },
            isError = latitudeError != null,
            supportingText = latitudeError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Campo de longitude
        OutlinedTextField(
            value = longitudeText,
            onValueChange = { 
                longitudeText = it
                longitudeError = try {
                    val lng = it.toDouble()
                    if (lng < -180 || lng > 180) "Longitude deve estar entre -180 e 180" else null
                } catch (e: NumberFormatException) {
                    "Longitude inválida"
                }
            },
            label = { Text("Longitude") },
            isError = longitudeError != null,
            supportingText = longitudeError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Botão de salvar
        PrimaryButton(
            text = "Salvar Localização",
            onClick = {
                if (name.isNotBlank() && latitudeError == null && longitudeError == null) {
                    try {
                        val latitude = latitudeText.toDouble()
                        val longitude = longitudeText.toDouble()
                        onSave(name, latitude, longitude)
                    } catch (e: NumberFormatException) {
                        // Tratado nos campos de texto
                    }
                }
            },
            enabled = name.isNotBlank() && nameError == null && latitudeError == null && longitudeError == null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Formata um número Double com a quantidade de casas decimais especificada.
 */
private fun Double.format(decimals: Int): String = "%.${decimals}f".format(this) 