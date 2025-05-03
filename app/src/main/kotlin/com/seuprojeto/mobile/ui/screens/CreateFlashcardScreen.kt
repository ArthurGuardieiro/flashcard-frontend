package com.seuprojeto.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.seuprojeto.mobile.model.Flashcard
import com.seuprojeto.mobile.model.FlashcardType
import com.seuprojeto.mobile.ui.components.FlashcardTopAppBar
import com.seuprojeto.mobile.ui.components.PrimaryButton

/**
 * Tela para criar ou editar um flashcard.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFlashcardScreen(
    onSaveFlashcard: (Flashcard) -> Unit,
    onNavigateBack: () -> Unit,
    existingFlashcard: Flashcard? = null,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Estados para o formulário
    var title by remember { mutableStateOf(existingFlashcard?.title ?: "") }
    var selectedType by remember { 
        mutableStateOf(existingFlashcard?.type ?: FlashcardType.BASIC) 
    }
    
    // Campos específicos para cada tipo de flashcard
    // Tipo básico (frente e verso)
    var front by remember { mutableStateOf(existingFlashcard?.front ?: "") }
    var back by remember { mutableStateOf(existingFlashcard?.back ?: "") }
    
    // Tipo cloze (omissão de palavras)
    var fullText by remember { mutableStateOf(existingFlashcard?.fullText ?: "") }
    var hiddenWordsText by remember { mutableStateOf(existingFlashcard?.hiddenWords?.joinToString(",") ?: "") }
    
    // Tipo digitação
    var question by remember { mutableStateOf(existingFlashcard?.question ?: "") }
    var correctAnswer by remember { mutableStateOf(existingFlashcard?.correctAnswer ?: "") }
    
    // Tipo múltipla escolha
    var mcQuestion by remember { mutableStateOf(existingFlashcard?.multipleChoiceQuestion ?: "") }
    val options = remember { 
        mutableStateListOf<String>().apply {
            existingFlashcard?.options?.let { addAll(it) } ?: addAll(List(4) { "" })
        }
    }
    var correctOptionIndex by remember { 
        mutableStateOf(existingFlashcard?.correctOptionIndex ?: 0) 
    }
    
    // Validação
    var titleError by remember { mutableStateOf<String?>(null) }
    var formError by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = if (existingFlashcard == null) "Criar Flashcard" else "Editar Flashcard",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Validar título
                    if (title.isBlank()) {
                        titleError = "O título não pode estar vazio"
                        return@FloatingActionButton
                    }
                    
                    // Criar flashcard
                    val flashcard = existingFlashcard?.copy(
                        title = title,
                        type = selectedType
                    ) ?: Flashcard(
                        title = title,
                        type = selectedType
                    )
                    
                    // Preencher campos específicos de acordo com o tipo
                    when (selectedType) {
                        FlashcardType.BASIC -> {
                            if (front.isBlank() || back.isBlank()) {
                                formError = "Preencha frente e verso do flashcard"
                                return@FloatingActionButton
                            }
                            flashcard.front = front
                            flashcard.back = back
                        }
                        
                        FlashcardType.CLOZE -> {
                            if (fullText.isBlank() || hiddenWordsText.isBlank()) {
                                formError = "Preencha o texto e as palavras a serem omitidas"
                                return@FloatingActionButton
                            }
                            flashcard.fullText = fullText
                            flashcard.hiddenWords = hiddenWordsText.split(",").map { it.trim() }
                        }
                        
                        FlashcardType.TYPING -> {
                            if (question.isBlank() || correctAnswer.isBlank()) {
                                formError = "Preencha a pergunta e a resposta correta"
                                return@FloatingActionButton
                            }
                            flashcard.question = question
                            flashcard.correctAnswer = correctAnswer
                        }
                        
                        FlashcardType.MULTIPLE_CHOICE -> {
                            if (mcQuestion.isBlank() || options.any { it.isBlank() }) {
                                formError = "Preencha a pergunta e todas as opções"
                                return@FloatingActionButton
                            }
                            flashcard.multipleChoiceQuestion = mcQuestion
                            flashcard.options = options.toList()
                            flashcard.correctOptionIndex = correctOptionIndex
                        }
                    }
                    
                    onSaveFlashcard(flashcard)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Salvar"
                )
            }
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
                    .verticalScroll(rememberScrollState())
            ) {
                // Título
                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        titleError = if (it.isBlank()) "O título não pode estar vazio" else null
                    },
                    label = { Text("Título") },
                    isError = titleError != null,
                    supportingText = titleError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Seleção de tipo
                Text(
                    text = "Tipo de Flashcard",
                    style = MaterialTheme.typography.titleMedium
                )
                
                FlashcardTypeSelector(
                    selectedType = selectedType,
                    onTypeSelected = { selectedType = it }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Mostrar campos de acordo com o tipo selecionado
                when (selectedType) {
                    FlashcardType.BASIC -> {
                        BasicFlashcardForm(
                            front = front,
                            back = back,
                            onFrontChange = { front = it },
                            onBackChange = { back = it }
                        )
                    }
                    
                    FlashcardType.CLOZE -> {
                        ClozeFlashcardForm(
                            fullText = fullText,
                            hiddenWords = hiddenWordsText,
                            onFullTextChange = { fullText = it },
                            onHiddenWordsChange = { hiddenWordsText = it }
                        )
                    }
                    
                    FlashcardType.TYPING -> {
                        TypingFlashcardForm(
                            question = question,
                            correctAnswer = correctAnswer,
                            onQuestionChange = { question = it },
                            onCorrectAnswerChange = { correctAnswer = it }
                        )
                    }
                    
                    FlashcardType.MULTIPLE_CHOICE -> {
                        MultipleChoiceFlashcardForm(
                            question = mcQuestion,
                            options = options,
                            correctOptionIndex = correctOptionIndex,
                            onQuestionChange = { mcQuestion = it },
                            onOptionChange = { index, text -> 
                                if (index < options.size) {
                                    options[index] = text
                                }
                            },
                            onCorrectOptionChange = { correctOptionIndex = it }
                        )
                    }
                }
                
                // Exibir mensagem de erro do formulário se houver
                formError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(80.dp)) // Espaço para o FloatingActionButton
            }
        }
    }
}

/**
 * Seletor para o tipo de flashcard.
 */
@Composable
fun FlashcardTypeSelector(
    selectedType: FlashcardType,
    onTypeSelected: (FlashcardType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        FlashcardType.values().forEach { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedType == type,
                    onClick = { onTypeSelected(type) }
                )
                
                Text(
                    text = when (type) {
                        FlashcardType.BASIC -> "Básico (Frente e Verso)"
                        FlashcardType.CLOZE -> "Lacunas (Omissão de Palavras)"
                        FlashcardType.TYPING -> "Digitação (Pergunta e Resposta)"
                        FlashcardType.MULTIPLE_CHOICE -> "Múltipla Escolha"
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

/**
 * Formulário para flashcard básico (frente e verso).
 */
@Composable
fun BasicFlashcardForm(
    front: String,
    back: String,
    onFrontChange: (String) -> Unit,
    onBackChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Frente e Verso",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = front,
            onValueChange = onFrontChange,
            label = { Text("Frente") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = back,
            onValueChange = onBackChange,
            label = { Text("Verso") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            )
        )
    }
}

/**
 * Formulário para flashcard de lacunas (omissão de palavras).
 */
@Composable
fun ClozeFlashcardForm(
    fullText: String,
    hiddenWords: String,
    onFullTextChange: (String) -> Unit,
    onHiddenWordsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Texto com Omissões",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = fullText,
            onValueChange = onFullTextChange,
            label = { Text("Texto completo") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            minLines = 3
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = hiddenWords,
            onValueChange = onHiddenWordsChange,
            label = { Text("Palavras a omitir (separadas por vírgula)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Exemplo: Digite um texto como 'O Brasil é um país da América do Sul' e palavras a omitir como 'Brasil,América,Sul'",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Formulário para flashcard de digitação (pergunta e resposta).
 */
@Composable
fun TypingFlashcardForm(
    question: String,
    correctAnswer: String,
    onQuestionChange: (String) -> Unit,
    onCorrectAnswerChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Pergunta e Resposta",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = question,
            onValueChange = onQuestionChange,
            label = { Text("Pergunta") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            minLines = 2
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = correctAnswer,
            onValueChange = onCorrectAnswerChange,
            label = { Text("Resposta correta") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            )
        )
    }
}

/**
 * Formulário para flashcard de múltipla escolha.
 */
@Composable
fun MultipleChoiceFlashcardForm(
    question: String,
    options: List<String>,
    correctOptionIndex: Int,
    onQuestionChange: (String) -> Unit,
    onOptionChange: (Int, String) -> Unit,
    onCorrectOptionChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Múltipla Escolha",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = question,
            onValueChange = onQuestionChange,
            label = { Text("Pergunta") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            minLines = 2
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Opções (marque a correta)",
            style = MaterialTheme.typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        options.forEachIndexed { index, option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = correctOptionIndex == index,
                    onClick = { onCorrectOptionChange(index) }
                )
                
                OutlinedTextField(
                    value = option,
                    onValueChange = { onOptionChange(index, it) },
                    label = { Text("Opção ${index + 1}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = if (index == options.size - 1) ImeAction.Done else ImeAction.Next
                    )
                )
            }
        }
        
        if (options.size < 6) {
            TextButton(
                onClick = { 
                    if (options is MutableList) {
                        options.add("")
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("+ Adicionar opção")
            }
        }
    }
} 