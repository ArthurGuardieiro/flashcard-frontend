package com.seuprojeto.mobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seuprojeto.mobile.model.Flashcard
import com.seuprojeto.mobile.model.FlashcardType
import com.seuprojeto.mobile.ui.components.BasicFlashcardStudyCard
import com.seuprojeto.mobile.ui.components.ClozeFlashcardStudyCard
import com.seuprojeto.mobile.ui.components.EmptyStateMessage
import com.seuprojeto.mobile.ui.components.FlashcardTopAppBar
import com.seuprojeto.mobile.ui.components.MultipleChoiceFlashcardStudyCard
import com.seuprojeto.mobile.ui.components.PrimaryButton
import com.seuprojeto.mobile.ui.components.TypingFlashcardStudyCard

/**
 * Tela de estudo que exibe flashcards para revisão.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    flashcards: List<Flashcard>,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    onFinishStudy: (List<Flashcard>) -> Unit,
    onUpdateDifficulty: (Flashcard, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Controle da sessão de estudo
    var currentIndex by remember { mutableIntStateOf(0) }
    var showFinishDialog by remember { mutableStateOf(false) }
    var reviewedFlashcards by remember { mutableStateOf(mutableListOf<Flashcard>()) }
    
    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = "Estudar",
                onBackClick = {
                    if (reviewedFlashcards.isNotEmpty()) {
                        showFinishDialog = true
                    } else {
                        onNavigateBack()
                    }
                }
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
        } else if (flashcards.isEmpty()) {
            EmptyStateMessage(
                message = "Não há flashcards para estudar agora. Crie novos flashcards ou volte mais tarde.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Indicador de progresso
                LinearProgressIndicator(
                    progress = (currentIndex + 1).toFloat() / flashcards.size.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Card do flashcard atual
                    val currentFlashcard = flashcards[currentIndex]
                    
                    when (currentFlashcard.type) {
                        FlashcardType.BASIC -> {
                            BasicFlashcardStudyCard(
                                flashcard = currentFlashcard,
                                onDifficultySelected = { difficulty ->
                                    handleDifficultySelection(
                                        flashcard = currentFlashcard,
                                        difficulty = difficulty,
                                        onUpdateDifficulty = onUpdateDifficulty,
                                        reviewedFlashcards = reviewedFlashcards
                                    )
                                    
                                    if (currentIndex < flashcards.size - 1) {
                                        currentIndex++
                                    } else {
                                        showFinishDialog = true
                                    }
                                }
                            )
                        }
                        
                        FlashcardType.CLOZE -> {
                            ClozeFlashcardStudyCard(
                                flashcard = currentFlashcard,
                                onDifficultySelected = { difficulty ->
                                    handleDifficultySelection(
                                        flashcard = currentFlashcard,
                                        difficulty = difficulty,
                                        onUpdateDifficulty = onUpdateDifficulty,
                                        reviewedFlashcards = reviewedFlashcards
                                    )
                                    
                                    if (currentIndex < flashcards.size - 1) {
                                        currentIndex++
                                    } else {
                                        showFinishDialog = true
                                    }
                                }
                            )
                        }
                        
                        FlashcardType.TYPING -> {
                            TypingFlashcardStudyCard(
                                flashcard = currentFlashcard,
                                onDifficultySelected = { difficulty ->
                                    handleDifficultySelection(
                                        flashcard = currentFlashcard,
                                        difficulty = difficulty,
                                        onUpdateDifficulty = onUpdateDifficulty,
                                        reviewedFlashcards = reviewedFlashcards
                                    )
                                    
                                    if (currentIndex < flashcards.size - 1) {
                                        currentIndex++
                                    } else {
                                        showFinishDialog = true
                                    }
                                }
                            )
                        }
                        
                        FlashcardType.MULTIPLE_CHOICE -> {
                            MultipleChoiceFlashcardStudyCard(
                                flashcard = currentFlashcard,
                                onDifficultySelected = { difficulty ->
                                    handleDifficultySelection(
                                        flashcard = currentFlashcard,
                                        difficulty = difficulty,
                                        onUpdateDifficulty = onUpdateDifficulty,
                                        reviewedFlashcards = reviewedFlashcards
                                    )
                                    
                                    if (currentIndex < flashcards.size - 1) {
                                        currentIndex++
                                    } else {
                                        showFinishDialog = true
                                    }
                                }
                            )
                        }
                    }
                }
                
                // Navegação entre flashcards
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botão Anterior
                    IconButton(
                        onClick = { 
                            if (currentIndex > 0) {
                                currentIndex--
                            }
                        },
                        enabled = currentIndex > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Anterior"
                        )
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Contador de flashcards
                    Text(
                        text = "${currentIndex + 1} de ${flashcards.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Botão Próximo
                    IconButton(
                        onClick = { 
                            if (currentIndex < flashcards.size - 1) {
                                currentIndex++
                            }
                        },
                        enabled = currentIndex < flashcards.size - 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Próximo"
                        )
                    }
                }
            }
        }
    }
    
    // Diálogo de finalização do estudo
    if (showFinishDialog) {
        AlertDialog(
            onDismissRequest = { showFinishDialog = false },
            title = { Text("Finalizar Estudo") },
            text = { 
                Text(
                    text = if (reviewedFlashcards.isEmpty()) 
                        "Deseja sair sem estudar nenhum flashcard?" 
                    else 
                        "Você revisou ${reviewedFlashcards.size} de ${flashcards.size} flashcards. Deseja finalizar a sessão?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onFinishStudy(reviewedFlashcards)
                        showFinishDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Finalizar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showFinishDialog = false }
                ) {
                    Text("Continuar Estudando")
                }
            }
        )
    }
}

/**
 * Função auxiliar para lidar com a seleção de dificuldade de um flashcard.
 */
private fun handleDifficultySelection(
    flashcard: Flashcard,
    difficulty: Int,
    onUpdateDifficulty: (Flashcard, Int) -> Unit,
    reviewedFlashcards: MutableList<Flashcard>
) {
    // Atualizar o flashcard com a dificuldade selecionada
    onUpdateDifficulty(flashcard, difficulty)
    
    // Adicionar à lista de flashcards revisados
    if (!reviewedFlashcards.contains(flashcard)) {
        reviewedFlashcards.add(flashcard)
    }
} 