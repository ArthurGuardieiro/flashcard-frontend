package com.seuprojeto.mobile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.seuprojeto.mobile.model.Flashcard
import com.seuprojeto.mobile.model.FlashcardType
import com.seuprojeto.mobile.model.Location
import com.seuprojeto.mobile.ui.screens.CreateFlashcardScreen
import com.seuprojeto.mobile.ui.screens.HomeScreen
import com.seuprojeto.mobile.ui.screens.LocationScreen
import com.seuprojeto.mobile.ui.screens.StatsScreen
import com.seuprojeto.mobile.ui.screens.StudyScreen
import com.seuprojeto.mobile.ui.screens.StudyStats
import com.seuprojeto.mobile.ui.theme.FlashcardsTheme
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.UUID

class MainActivity : ComponentActivity() {
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    
    // Solicitação de permissão de localização
    private val requestLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            // Permissões concedidas, inicializar componentes de localização
        } else {
            // Mostrar mensagem sobre permissões faltantes
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar o cliente de localização
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        // Solicitar permissões
        requestLocationPermissions()
        
        // Configurar a interface do aplicativo
        setContent {
            FlashcardsApp {
                getCurrentLocation()
            }
        }
    }
    
    /**
     * Solicita as permissões de localização necessárias.
     */
    private fun requestLocationPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        
        requestLocationPermission.launch(permissions)
    }
    
    /**
     * Obtém a localização atual do dispositivo.
     */
    private fun getCurrentLocation(): Pair<Double, Double>? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions()
            return null
        }
        
        var locationResult: Pair<Double, Double>? = null
        
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                locationResult = Pair(it.latitude, it.longitude)
            }
        }
        
        return locationResult
    }
}

/**
 * Composable principal que configura a navegação e o layout do aplicativo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsApp(
    onGetCurrentLocation: () -> Pair<Double, Double>?
) {
    // Estado da navegação
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Dados temporários para demonstração
    val flashcards = remember { generateSampleFlashcards() }
    val locations = remember { generateSampleLocations() }
    val stats = remember { generateSampleStats() }
    
    // Estados de carregamento
    var isLoading by remember { mutableStateOf(false) }
    
    FlashcardsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppDrawerContent(
                        navController = navController,
                        drawerState = drawerState
                    )
                }
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    // Tela inicial
                    composable("home") {
                        HomeScreen(
                            flashcards = flashcards,
                            isLoading = isLoading,
                            drawerState = drawerState,
                            onAddFlashcard = { navController.navigate("create_flashcard") },
                            onFlashcardClick = { flashcard ->
                                navController.navigate("edit_flashcard/${flashcard.id}")
                            },
                            onStudyClick = { navController.navigate("study") },
                            onLocationClick = { navController.navigate("location") },
                            onStatsClick = { navController.navigate("stats") }
                        )
                    }
                    
                    // Tela de criação de flashcard
                    composable("create_flashcard") {
                        CreateFlashcardScreen(
                            onSaveFlashcard = { flashcard ->
                                // Implementar salvar flashcard
                                navController.popBackStack()
                            },
                            onNavigateBack = { navController.popBackStack() },
                            isLoading = isLoading
                        )
                    }
                    
                    // Tela de edição de flashcard
                    composable("edit_flashcard/{flashcardId}") { backStackEntry ->
                        val flashcardId = backStackEntry.arguments?.getString("flashcardId")
                        val flashcard = flashcards.find { it.id == flashcardId }
                        
                        if (flashcard != null) {
                            CreateFlashcardScreen(
                                onSaveFlashcard = { updatedFlashcard ->
                                    // Implementar atualizar flashcard
                                    navController.popBackStack()
                                },
                                onNavigateBack = { navController.popBackStack() },
                                existingFlashcard = flashcard,
                                isLoading = isLoading
                            )
                        } else {
                            LaunchedEffect(Unit) {
                                navController.popBackStack()
                            }
                        }
                    }
                    
                    // Tela de estudo
                    composable("study") {
                        StudyScreen(
                            flashcards = flashcards,
                            isLoading = isLoading,
                            onNavigateBack = { navController.popBackStack() },
                            onFinishStudy = { reviewedFlashcards ->
                                // Implementar finalização de estudo
                            },
                            onUpdateDifficulty = { flashcard, difficulty ->
                                // Implementar atualização de dificuldade
                            }
                        )
                    }
                    
                    // Tela de localização
                    composable("location") {
                        LocationScreen(
                            locations = locations,
                            isLoading = isLoading,
                            onAddLocation = { location ->
                                // Implementar adicionar localização
                            },
                            onEditLocation = { location ->
                                // Implementar editar localização
                            },
                            onDeleteLocation = { location ->
                                // Implementar excluir localização
                            },
                            onFavoriteLocation = { location, isFavorite ->
                                // Implementar marcar como favorito
                            },
                            onNavigateBack = { navController.popBackStack() },
                            onGetCurrentLocation = onGetCurrentLocation
                        )
                    }
                    
                    // Tela de estatísticas
                    composable("stats") {
                        StatsScreen(
                            stats = stats,
                            isLoading = isLoading,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Conteúdo da gaveta de navegação lateral.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerContent(
    navController: NavHostController,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    
    ModalDrawerSheet {
        Box(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
            Text(
                text = "Flashcards App",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Início") },
            label = { Text("Início") },
            selected = false,
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
                scope.launch { drawerState.close() }
            }
        )
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.School, contentDescription = "Estudar") },
            label = { Text("Estudar") },
            selected = false,
            onClick = {
                navController.navigate("study")
                scope.launch { drawerState.close() }
            }
        )
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Localizações") },
            label = { Text("Localizações") },
            selected = false,
            onClick = {
                navController.navigate("location")
                scope.launch { drawerState.close() }
            }
        )
        
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Info, contentDescription = "Estatísticas") },
            label = { Text("Estatísticas") },
            selected = false,
            onClick = {
                navController.navigate("stats")
                scope.launch { drawerState.close() }
            }
        )
    }
}

/**
 * Gera flashcards de exemplo para demonstração.
 */
private fun generateSampleFlashcards(): List<Flashcard> {
    return listOf(
        Flashcard(
            id = UUID.randomUUID().toString(),
            title = "Capitais do Brasil",
            type = FlashcardType.BASIC,
            createdAt = LocalDateTime.now().minusDays(10),
            lastReviewed = LocalDateTime.now().minusDays(2),
            reviewCount = 5
        ).apply {
            front = "Qual é a capital do Brasil?"
            back = "Brasília"
        },
        
        Flashcard(
            id = UUID.randomUUID().toString(),
            title = "Verbos em Inglês",
            type = FlashcardType.CLOZE,
            createdAt = LocalDateTime.now().minusDays(7),
            lastReviewed = LocalDateTime.now().minusDays(1),
            reviewCount = 3
        ).apply {
            fullText = "The teacher went to the school yesterday."
            hiddenWords = listOf("went", "yesterday")
        },
        
        Flashcard(
            id = UUID.randomUUID().toString(),
            title = "Fórmula da Água",
            type = FlashcardType.TYPING,
            createdAt = LocalDateTime.now().minusDays(5),
            reviewCount = 2
        ).apply {
            question = "Qual é a fórmula química da água?"
            correctAnswer = "H2O"
        },
        
        Flashcard(
            id = UUID.randomUUID().toString(),
            title = "História do Brasil",
            type = FlashcardType.MULTIPLE_CHOICE,
            createdAt = LocalDateTime.now().minusDays(3),
            reviewCount = 1
        ).apply {
            multipleChoiceQuestion = "Em que ano o Brasil foi descoberto pelos portugueses?"
            options = listOf("1492", "1500", "1503", "1550")
            correctOptionIndex = 1
        }
    )
}

/**
 * Gera localizações de exemplo para demonstração.
 */
private fun generateSampleLocations(): List<Location> {
    return listOf(
        Location(
            id = UUID.randomUUID().toString(),
            name = "Casa",
            latitude = -23.550520,
            longitude = -46.633308,
            lastUsed = true
        ),
        
        Location(
            id = UUID.randomUUID().toString(),
            name = "Faculdade",
            latitude = -23.559976,
            longitude = -46.731215,
            lastUsed = false
        ),
        
        Location(
            id = UUID.randomUUID().toString(),
            name = "Biblioteca",
            latitude = -23.544889,
            longitude = -46.642231,
            lastUsed = false
        )
    )
}

/**
 * Gera estatísticas de exemplo para demonstração.
 */
private fun generateSampleStats(): StudyStats {
    val today = LocalDate.now()
    
    val reviewsLast7Days = mapOf(
        today.minusDays(6) to 5,
        today.minusDays(5) to 8,
        today.minusDays(4) to 3,
        today.minusDays(3) to 10,
        today.minusDays(2) to 7,
        today.minusDays(1) to 12,
        today to 6
    )
    
    val reviewsByType = mapOf(
        FlashcardType.BASIC to 25,
        FlashcardType.CLOZE to 15,
        FlashcardType.TYPING to 8,
        FlashcardType.MULTIPLE_CHOICE to 12
    )
    
    val reviewsByDifficulty = mapOf(
        "Fácil" to 30,
        "Médio" to 20,
        "Difícil" to 10
    )
    
    return StudyStats(
        totalReviews = 60,
        totalFlashcards = 20,
        totalStudyTime = 240, // 4 horas
        streakDays = 7,
        reviewsLast7Days = reviewsLast7Days,
        reviewsByType = reviewsByType,
        reviewsByDifficulty = reviewsByDifficulty
    )
}