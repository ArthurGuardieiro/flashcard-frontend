package com.seuprojeto.mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.seuprojeto.mobile.model.Deck
import com.seuprojeto.mobile.ui.components.FlashcardTopAppBar

/**
 * Tela para criação ou edição de um baralho.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDeckScreen(
    onSaveDeck: (Deck) -> Unit,
    onNavigateBack: () -> Unit,
    existingDeck: Deck? = null,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isEditing = existingDeck != null
    
    var name by remember { mutableStateOf(existingDeck?.name ?: "") }
    var description by remember { mutableStateOf(existingDeck?.description ?: "") }
    var selectedColor by remember { mutableStateOf(existingDeck?.coverColor ?: 0xFF2196F3) }
    
    val scrollState = rememberScrollState()
    
    // Lista de cores para escolher
    val colorOptions = remember {
        listOf(
            0xFF2196F3, // Azul
            0xFF4CAF50, // Verde
            0xFFFF9800, // Laranja
            0xFFE91E63, // Rosa
            0xFF9C27B0, // Roxo
            0xFF795548, // Marrom
            0xFF607D8B, // Azul acinzentado
            0xFFF44336  // Vermelho
        )
    }
    
    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = if (isEditing) "Editar Baralho" else "Novo Baralho",
                onBackClick = onNavigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Visualização da cor selecionada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(selectedColor)),
                    contentAlignment = Alignment.Center
                ) {
                    if (name.isNotEmpty()) {
                        Text(
                            text = name.take(1).uppercase(),
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.White
                        )
                    }
                }
                
                // Seleção de cor
                Text(
                    text = "Escolha uma cor",
                    style = MaterialTheme.typography.titleMedium
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(colorOptions) { color ->
                        ColorOption(
                            color = Color(color),
                            isSelected = color == selectedColor,
                            onClick = { selectedColor = color }
                        )
                    }
                }
                
                // Campo de nome
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do baralho") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // Campo de descrição
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Botão de salvar
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val deck = existingDeck?.copy(
                                name = name,
                                description = description,
                                coverColor = selectedColor
                            ) ?: Deck(
                                name = name,
                                description = description,
                                coverColor = selectedColor
                            )
                            onSaveDeck(deck)
                        }
                    },
                    enabled = name.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isEditing) "Salvar Alterações" else "Criar Baralho")
                }
            }
        }
    }
}

/**
 * Opção de cor para seleção.
 */
@Composable
fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selecionado",
                tint = Color.White
            )
        }
    }
}
