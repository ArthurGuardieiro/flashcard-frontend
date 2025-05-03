package com.seuprojeto.mobile.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seuprojeto.mobile.ui.theme.DividerColor

/**
 * Barra de navegação superior com título centralizado e botões opcionais.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardTopAppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    menuIcon: ImageVector? = null,
    onMenuClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = { 
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            }
        },
        actions = {
            if (menuIcon != null && onMenuClick != null) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = menuIcon,
                        contentDescription = "Menu"
                    )
                }
            }
        }
    )
}

/**
 * Botão primário com texto, usado para ações principais.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(text = text)
    }
}

/**
 * Botão secundário com contorno, usado para ações secundárias.
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(text = text)
    }
}

/**
 * Botão flutuante de ação, geralmente usado para adicionar novos itens.
 */
@Composable
fun AddFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Adicionar"
        )
    }
}

/**
 * Componente de carregamento centralizado.
 */
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

/**
 * Mensagem de estado vazio, usada quando não há dados para exibir.
 */
@Composable
fun EmptyStateMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Divisor horizontal simples.
 */
@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier,
        color = DividerColor,
        thickness = 1.dp
    )
} 