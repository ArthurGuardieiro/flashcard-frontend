package com.seuprojeto.mobile.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seuprojeto.mobile.model.Flashcard
import com.seuprojeto.mobile.model.FlashcardType
import com.seuprojeto.mobile.ui.components.FlashcardTopAppBar
import com.seuprojeto.mobile.ui.components.HorizontalDivider
import com.seuprojeto.mobile.ui.theme.Easy
import com.seuprojeto.mobile.ui.theme.Hard
import com.seuprojeto.mobile.ui.theme.Medium
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import kotlin.math.min

/**
 * Tela que exibe estatísticas de estudo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    stats: StudyStats,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            FlashcardTopAppBar(
                title = "Estatísticas",
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
                    .verticalScroll(rememberScrollState())
            ) {
                // Cards de resumo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatsSummaryCard(
                        icon = Icons.Default.Refresh,
                        title = "Total de Revisões",
                        value = "${stats.totalReviews}",
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatsSummaryCard(
                        icon = Icons.Default.Star,
                        title = "Flashcards",
                        value = "${stats.totalFlashcards}",
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Mais cards de resumo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatsSummaryCard(
                        icon = Icons.Default.DateRange,
                        title = "Sequência",
                        value = "${stats.streakDays} dias",
                        modifier = Modifier.weight(1f)
                    )
                    
                    StatsSummaryCard(
                        icon = Icons.Default.TrendingUp,
                        title = "Tempo Total",
                        value = formatStudyTime(stats.totalStudyTime),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Gráfico de revisões nos últimos 7 dias
                Text(
                    text = "Revisões nos últimos 7 dias",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        WeeklyReviewsChart(
                            data = stats.reviewsLast7Days,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Distribuição por tipo de flashcard
                Text(
                    text = "Revisões por tipo de flashcard",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        stats.reviewsByType.forEach { (type, count) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (type) {
                                        FlashcardType.BASIC -> "Básico"
                                        FlashcardType.CLOZE -> "Lacunas"
                                        FlashcardType.TYPING -> "Digitação"
                                        FlashcardType.MULTIPLE_CHOICE -> "Múltipla escolha"
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                
                                Text(
                                    text = "$count",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            if (type != stats.reviewsByType.keys.last()) {
                                HorizontalDivider()
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Distribuição por dificuldade
                Text(
                    text = "Distribuição por dificuldade",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DifficultyPieChart(
                            data = stats.reviewsByDifficulty,
                            modifier = Modifier.size(100.dp)
                        )
                        
                        Column(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            LegendItem(
                                label = "Fácil",
                                count = stats.reviewsByDifficulty["Fácil"] ?: 0,
                                color = Easy
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            LegendItem(
                                label = "Médio",
                                count = stats.reviewsByDifficulty["Médio"] ?: 0,
                                color = Medium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            LegendItem(
                                label = "Difícil",
                                count = stats.reviewsByDifficulty["Difícil"] ?: 0,
                                color = Hard
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Card que exibe um resumo estatístico.
 */
@Composable
fun StatsSummaryCard(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Gráfico de barras para revisões semanais.
 */
@Composable
fun WeeklyReviewsChart(
    data: Map<LocalDate, Int>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.values.maxOrNull() ?: 0
    val today = LocalDate.now()
    val dayFormatter = DateTimeFormatter.ofPattern("E")
    val primaryColor = MaterialTheme.colorScheme.primary
    
    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val barWidth = size.width / 8 // Reservar espaço para 7 barras com margens
            val usableWidth = size.width - barWidth
            val barSpace = usableWidth / 7
            val maxBarHeight = size.height * 0.8f
            
            // Desenhar as barras
            for (i in 0 until 7) {
                val date = today.minusDays(6L - i)
                val value = data[date] ?: 0
                val normalizedHeight = if (maxValue > 0) (value.toFloat() / maxValue) * maxBarHeight else 0f
                
                val startX = barSpace * i + barSpace / 2
                val startY = size.height
                val endY = size.height - normalizedHeight
                
                // Barra
                drawLine(
                    color = primaryColor,
                    start = Offset(startX, startY),
                    end = Offset(startX, endY),
                    strokeWidth = barWidth * 0.8f
                )
            }
        }
        
        // Rótulos dos dias
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0 until 7) {
                val date = today.minusDays(6L - i)
                Text(
                    text = dayFormatter.format(date),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (date == today) primaryColor else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Gráfico de pizza para distribuição de dificuldade.
 */
@Composable
fun DifficultyPieChart(
    data: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val total = data.values.sum().toFloat().coerceAtLeast(1f)
    val easyCount = data["Fácil"] ?: 0
    val mediumCount = data["Médio"] ?: 0
    val hardCount = data["Difícil"] ?: 0
    
    val easyAngle = (easyCount / total) * 360f
    val mediumAngle = (mediumCount / total) * 360f
    val hardAngle = (hardCount / total) * 360f
    
    val whiteColor = Color.White
    
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = min(size.width, size.height) / 2 * 0.8f
        
        var startAngle = 0f
        
        // Desenhar setor "Fácil"
        if (easyCount > 0) {
            drawArc(
                color = Easy,
                startAngle = startAngle,
                sweepAngle = easyAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
            startAngle += easyAngle
        }
        
        // Desenhar setor "Médio"
        if (mediumCount > 0) {
            drawArc(
                color = Medium,
                startAngle = startAngle,
                sweepAngle = mediumAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
            startAngle += mediumAngle
        }
        
        // Desenhar setor "Difícil"
        if (hardCount > 0) {
            drawArc(
                color = Hard,
                startAngle = startAngle,
                sweepAngle = hardAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
        }
        
        // Círculo de contorno
        drawCircle(
            color = whiteColor,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

/**
 * Item de legenda para os gráficos.
 */
@Composable
fun LegendItem(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .padding(2.dp)
                .background(color = color, shape = RoundedCornerShape(4.dp))
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = "$label: $count",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Formata o tempo de estudo em formato legível.
 */
private fun formatStudyTime(timeInMinutes: Long): String {
    val hours = timeInMinutes / 60
    val minutes = timeInMinutes % 60
    
    return when {
        hours > 0 -> "$hours h $minutes min"
        else -> "$minutes min"
    }
} 