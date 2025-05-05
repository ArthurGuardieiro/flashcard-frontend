package com.seuprojeto.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seuprojeto.mobile.model.Flashcard
import com.seuprojeto.mobile.ui.components.EmptyStateMessage
import com.seuprojeto.mobile.ui.components.FlashcardItem
import com.seuprojeto.mobile.ui.components.FlashcardTopAppBar
import kotlinx.coroutines.launch

/**
 * Tela inicial do aplicativo que lista os flashcards do usuário.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    flashcards: List<Flashcard>,
    isLoading: Boolean,
    drawerState: DrawerState,
    onAddFlashcard: () -> Unit,
    onFlashcardClick: (Flashcard) -> Unit,
    onStudyClick: () -> Unit,
    onLocationClick: () -> Unit,
    onStatsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = "Meus Flashcards",
                menuIcon = Icons.Default.Menu,
                onMenuClick = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFlashcard
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Flashcard"
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Campo de busca
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar flashcards") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            
            // Botões de ação rápida
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HomeActionButton(
                    text = "Estudar",
                    onClick = onStudyClick,
                    modifier = Modifier.weight(1f)
                )
                
                HomeActionButton(
                    text = "Localizações",
                    onClick = onLocationClick,
                    modifier = Modifier.weight(1f)
                )
                
                HomeActionButton(
                    text = "Estatísticas",
                    onClick = onStatsClick,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Estado de carregamento
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Lista de flashcards filtrada pela busca
                val filteredFlashcards = flashcards.filter { 
                    searchQuery.isEmpty() || 
                    it.title.contains(searchQuery, ignoreCase = true) ||
                    (it.front?.contains(searchQuery, ignoreCase = true) ?: false) ||
                    (it.back?.contains(searchQuery, ignoreCase = true) ?: false) ||
                    (it.question?.contains(searchQuery, ignoreCase = true) ?: false)
                }
                
                if (filteredFlashcards.isEmpty()) {
                    EmptyStateMessage(
                        message = if (searchQuery.isNotEmpty()) 
                            "Nenhum flashcard encontrado para a busca \"$searchQuery\"" 
                        else 
                            "Você ainda não possui flashcards. Clique no botão + para criar um novo.",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredFlashcards) { flashcard ->
                            FlashcardItem(
                                flashcard = flashcard,
                                onClick = onFlashcardClick
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Botão de ação para a tela inicial.
 */
@Composable
fun HomeActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
} 