package fr.isen.RAVAN.isensmartcompanion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.RAVAN.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.ai.client.generativeai.GenerativeModel
//import com.google.ai.client.generativeai.type.GenerateContentResponse as TypeGenerateContentResponse
// import com.google.ai.client.generativeai.type.HarmBlockThreshold
import com.google.ai.client.generativeai.type.content
import fr.isen.RAVAN.isensmartcompanion.BuildConfig.API_KEY
import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.CoroutineScope
import androidx.room.Delete
import fr.isen.RAVAN.isensmartcompanion.composable.AgendaScreen
import fr.isen.RAVAN.isensmartcompanion.composable.BottomNavigationBar
import fr.isen.RAVAN.isensmartcompanion.composable.EventsScreen
import fr.isen.RAVAN.isensmartcompanion.composable.HistoryScreen
import fr.isen.RAVAN.isensmartcompanion.composable.MainScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


@Dao
interface InteractionDao {
    @Insert
    fun insert(interaction: fr.isen.RAVAN.isensmartcompanion.database.Interaction)

    @Delete
    fun delete(interaction: fr.isen.RAVAN.isensmartcompanion.database.Interaction)

    @Query("SELECT * FROM interactions")
    fun getAll(): List<fr.isen.RAVAN.isensmartcompanion.database.Interaction>
}

@Entity(tableName = "interactions")
data class Interaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String,
    val timestamp: Date = Date()
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISENSmartCompanionTheme {
                MainApp()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Accueil", Icons.Filled.Home)
    object Events : Screen("events", "Événements", Icons.Filled.ThumbUp)
    object Agenda : Screen("agenda", "Agenda", Icons.Filled.DateRange)
    object History : Screen("history", "Historique", Icons.Filled.List)
}

@Composable
fun MainApp() {
    Log.d("MainApp", "MainApp called")
    val navController = rememberNavController()

    // Créer une CoroutineScope pour gérer les coroutines.
    val scope = rememberCoroutineScope()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, scope)
        }
    ) { innerPadding ->
        Navigation(navController, innerPadding)
    }
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { MainScreen(innerPadding) }
        composable(Screen.Events.route) { EventsScreen(navController) }
        composable(Screen.Agenda.route) { AgendaScreen() }
        composable(Screen.History.route) { HistoryScreen() }
    }
}

fun generateContent(
    generativeModel: GenerativeModel,
    userInput: String,
    onResult: (String) -> Unit
) {
    val prompt = "Tu es un assistant personnel pour les élèves de l'ISEN. Ton role est d'aider les élèves au maximum. Tu dois être concis. $userInput"
    CoroutineScope(Dispatchers.IO).launch {
        try {

            val content = content {
                text(prompt)
            }

            val response = generativeModel.generateContent(content)
            val text = response.text

            CoroutineScope(Dispatchers.Main).launch {
                if (text != null) {
                    onResult(text)
                } else {
                    onResult("Erreur : Réponse vide ou null")
                }
            }
        } catch (e: Exception) {
            Log.e("GenerationContent", "Erreur lors de la génération de contenu", e)
            CoroutineScope(Dispatchers.Main).launch {
                onResult("Erreur : Impossible de joindre l'IA")
            }
        }
    }
}

fun getGenerativeModel(): GenerativeModel {
    val modelName = "gemini-1.5-flash-latest"
    val apiKey = API_KEY
    return GenerativeModel(modelName, apiKey)
}