package com.seuprojeto.mobile.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.seuprojeto.mobile.model.Flashcard
import com.seuprojeto.mobile.model.FlashcardType
import com.seuprojeto.mobile.ui.theme.CardStroke
import com.seuprojeto.mobile.ui.theme.Easy
import com.seuprojeto.mobile.ui.theme.Hard
import com.seuprojeto.mobile.ui.theme.Medium

/**
 * Item de flashcard padrão que exibe detalhes conforme o tipo.
 */
@Composable
fun FlashcardItem(
    flashcard: Flashcard,
    onClick: (Flashcard) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(flashcard) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = flashcard.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Exibir conteúdo de pré-visualização com base no tipo
            when (flashcard.type) {
                FlashcardType.BASIC -> {
                    Text(
                        text = flashcard.front ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                }
                
                FlashcardType.CLOZE -> {
                    val text = flashcard.fullText ?: ""
                    Text(
                        text = text.take(100) + if (text.length > 100) "..." else "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                FlashcardType.TYPING -> {
                    Text(
                        text = flashcard.question?.take(100) ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                FlashcardType.MULTIPLE_CHOICE -> {
                    Text(
                        text = flashcard.multipleChoiceQuestion?.take(100) ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Indicador de tipo e contagem de revisão
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (flashcard.type) {
                        FlashcardType.BASIC -> "Básico"
                        FlashcardType.CLOZE -> "Lacunas"
                        FlashcardType.TYPING -> "Digitação"
                        FlashcardType.MULTIPLE_CHOICE -> "Múltipla escolha"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "${flashcard.reviewCount}x",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

/**
 * Componente para estudo do flashcard de tipo básico (frente e verso).
 */
@Composable
fun BasicFlashcardStudyCard(
    flashcard: Flashcard,
    onDifficultySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showingAnswer by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = flashcard.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, CardStroke, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .clickable { showingAnswer = !showingAnswer },
                contentAlignment = Alignment.Center
            ) {
                if (!showingAnswer) {
                    Text(
                        text = flashcard.front ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                
                if (showingAnswer) {
                    Text(
                        text = flashcard.back ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (showingAnswer) "Toque para ver a pergunta" else "Toque para ver a resposta",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botões de dificuldade (só aparecem quando a resposta é mostrada)
            if (showingAnswer) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DifficultyButton(
                        text = "Difícil",
                        color = Hard,
                        onClick = { onDifficultySelected(1) }
                    )
                    
                    DifficultyButton(
                        text = "Médio",
                        color = Medium,
                        onClick = { onDifficultySelected(2) }
                    )
                    
                    DifficultyButton(
                        text = "Fácil",
                        color = Easy,
                        onClick = { onDifficultySelected(3) }
                    )
                }
            }
        }
    }
}

/**
 * Componente para estudo do flashcard de tipo cloze (palavras omitidas).
 */
@Composable
fun ClozeFlashcardStudyCard(
    flashcard: Flashcard,
    onDifficultySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showingAnswer by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = flashcard.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, CardStroke, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .clickable { showingAnswer = !showingAnswer },
                contentAlignment = Alignment.Center
            ) {
                if (!showingAnswer) {
                    // Texto com palavras omitidas
                    val fullText = flashcard.fullText ?: ""
                    val hiddenWords = flashcard.hiddenWords ?: emptyList()
                    
                    val annotatedString = buildAnnotatedString {
                        var lastIndex = 0
                        
                        for (word in hiddenWords) {
                            val startIndex = fullText.indexOf(word, lastIndex)
                            if (startIndex == -1) continue
                            
                            // Texto antes da palavra omitida
                            append(fullText.substring(lastIndex, startIndex))
                            
                            // Palavra omitida substituída por underscores
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) {
                                append("_".repeat(word.length))
                            }
                            
                            lastIndex = startIndex + word.length
                        }
                        
                        // Restante do texto
                        if (lastIndex < fullText.length) {
                            append(fullText.substring(lastIndex))
                        }
                    }
                    
                    Text(
                        text = annotatedString,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                } else {
                    // Texto completo com palavras destacadas
                    val fullText = flashcard.fullText ?: ""
                    val hiddenWords = flashcard.hiddenWords ?: emptyList()
                    
                    val annotatedString = buildAnnotatedString {
                        var lastIndex = 0
                        
                        for (word in hiddenWords) {
                            val startIndex = fullText.indexOf(word, lastIndex)
                            if (startIndex == -1) continue
                            
                            // Texto antes da palavra destacada
                            append(fullText.substring(lastIndex, startIndex))
                            
                            // Palavra destacada
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) {
                                append(word)
                            }
                            
                            lastIndex = startIndex + word.length
                        }
                        
                        // Restante do texto
                        if (lastIndex < fullText.length) {
                            append(fullText.substring(lastIndex))
                        }
                    }
                    
                    Text(
                        text = annotatedString,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (showingAnswer) "Toque para ocultar a resposta" else "Toque para ver a resposta",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botões de dificuldade (só aparecem quando a resposta é mostrada)
            if (showingAnswer) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DifficultyButton(
                        text = "Difícil",
                        color = Hard,
                        onClick = { onDifficultySelected(1) }
                    )
                    
                    DifficultyButton(
                        text = "Médio",
                        color = Medium,
                        onClick = { onDifficultySelected(2) }
                    )
                    
                    DifficultyButton(
                        text = "Fácil",
                        color = Easy,
                        onClick = { onDifficultySelected(3) }
                    )
                }
            }
        }
    }
}

/**
 * Componente para estudo do flashcard de tipo digitação (resposta escrita).
 */
@Composable
fun TypingFlashcardStudyCard(
    flashcard: Flashcard,
    onDifficultySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var userAnswer by remember { mutableStateOf("") }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = flashcard.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Pergunta
            Text(
                text = flashcard.question ?: "",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Campo de resposta
            OutlinedTextField(
                value = userAnswer,
                onValueChange = { userAnswer = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Sua resposta") },
                enabled = !isAnswerSubmitted,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (!isAnswerSubmitted) {
                            isAnswerSubmitted = true
                            isCorrect = userAnswer.trim().equals((flashcard.correctAnswer ?: "").trim(), ignoreCase = true)
                        }
                    }
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botão de verificar resposta
            if (!isAnswerSubmitted) {
                PrimaryButton(
                    text = "Verificar Resposta",
                    onClick = {
                        isAnswerSubmitted = true
                        isCorrect = userAnswer.trim().equals((flashcard.correctAnswer ?: "").trim(), ignoreCase = true)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Resultado da verificação
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = if (isCorrect) Easy else Hard,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    Text(
                        text = if (isCorrect) "Correto!" else "Resposta correta: ${flashcard.correctAnswer}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isCorrect) Easy else Hard
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botões de dificuldade
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DifficultyButton(
                        text = "Difícil",
                        color = Hard,
                        onClick = { onDifficultySelected(1) }
                    )
                    
                    DifficultyButton(
                        text = "Médio",
                        color = Medium,
                        onClick = { onDifficultySelected(2) }
                    )
                    
                    DifficultyButton(
                        text = "Fácil",
                        color = Easy,
                        onClick = { onDifficultySelected(3) }
                    )
                }
            }
        }
    }
}

/**
 * Componente para estudo do flashcard de tipo múltipla escolha.
 */
@Composable
fun MultipleChoiceFlashcardStudyCard(
    flashcard: Flashcard,
    onDifficultySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = flashcard.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Pergunta
            Text(
                text = flashcard.multipleChoiceQuestion ?: "",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Opções
            flashcard.options?.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isAnswerSubmitted) {
                            selectedOptionIndex = index
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOptionIndex == index,
                        onClick = {
                            if (!isAnswerSubmitted) {
                                selectedOptionIndex = index
                            }
                        },
                        enabled = !isAnswerSubmitted
                    )
                    
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp),
                        color = when {
                            isAnswerSubmitted && index == flashcard.correctOptionIndex -> Easy
                            isAnswerSubmitted && index == selectedOptionIndex && index != flashcard.correctOptionIndex -> Hard
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botão de verificar resposta
            if (!isAnswerSubmitted) {
                PrimaryButton(
                    text = "Verificar Resposta",
                    onClick = {
                        if (selectedOptionIndex != null) {
                            isAnswerSubmitted = true
                            isCorrect = selectedOptionIndex == flashcard.correctOptionIndex
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedOptionIndex != null
                )
            } else {
                // Resultado da verificação
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = if (isCorrect) Easy else Hard,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    Text(
                        text = if (isCorrect) "Correto!" else "Incorreto!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isCorrect) Easy else Hard
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botões de dificuldade
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DifficultyButton(
                        text = "Difícil",
                        color = Hard,
                        onClick = { onDifficultySelected(1) }
                    )
                    
                    DifficultyButton(
                        text = "Médio",
                        color = Medium,
                        onClick = { onDifficultySelected(2) }
                    )
                    
                    DifficultyButton(
                        text = "Fácil",
                        color = Easy,
                        onClick = { onDifficultySelected(3) }
                    )
                }
            }
        }
    }
}

/**
 * Botão para classificação de dificuldade.
 */
@Composable
fun DifficultyButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Timer,
            contentDescription = null,
            tint = color,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
} 