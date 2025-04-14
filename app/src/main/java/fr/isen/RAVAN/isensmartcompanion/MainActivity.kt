package fr.isen.RAVAN.isensmartcompanion

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import fr.isen.RAVAN.isensmartcompanion.composable.AgendaScreen
import fr.isen.RAVAN.isensmartcompanion.composable.BottomNavigationBar
import fr.isen.RAVAN.isensmartcompanion.composable.EventsScreen
import fr.isen.RAVAN.isensmartcompanion.composable.HistoryScreen
import fr.isen.RAVAN.isensmartcompanion.composable.MainScreen
import fr.isen.RAVAN.isensmartcompanion.database.AppDatabase
import fr.isen.RAVAN.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    companion object {
        const val CHANNEL_ID = "event_channel"
        const val NOTIFICATION_ID = 1
        const val REQUEST_CODE_PERMISSION = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission()
        }

        setContent {
            ISENSmartCompanionTheme {
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val context = LocalContext.current
                val db = AppDatabase.getDatabase(LocalContext.current)
                val responseList = remember { mutableStateListOf<String>() }

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController, scope)
                    }
                ) { innerPadding ->
                    Navigation(
                        navController,
                        innerPadding,
                        scope,
                        ::getGenerativeModel,
                        ::generateContent,
                        context,
                        responseList,
                        db
                    )
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Notifications"
            val descriptionText = "Channel for event notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getGenerativeModel(): GenerativeModel {
        val modelName = "gemini-1.5-flash-latest"
        val apiKey = BuildConfig.API_KEY
        return GenerativeModel(modelName, apiKey)
    }

    suspend fun generateContent(
        coroutineScope: CoroutineScope,
        generativeModel: GenerativeModel,
        userInput: String,
        onResult: (String) -> Unit
    ) {
        val prompt =
            "Tu es un assistant personnel pour les élèves de l'ISEN. Ton role est d'aider les élèves au maximum. Tu dois être concis. $userInput"
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {

                    val content = content {
                        text(prompt)
                    }

                    val response = generativeModel.generateContent(content)
                    val text = response.text

                    withContext(Dispatchers.Main) {
                        if (text != null) {
                            onResult(text)
                        } else {
                            onResult("Erreur : Réponse vide ou null")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("GenerationContent", "Erreur lors de la génération de contenu", e)
                    withContext(Dispatchers.Main) {
                        onResult("Erreur : Impossible de joindre l'IA")
                    }
                }
            }
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission", "Notification Permission Granted")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_PERMISSION
                )
            }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun sendNotification(title: String, message: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                with(NotificationManagerCompat.from(this)) {
                    notify(NOTIFICATION_ID, builder.build())
                }
            }, 10000)
        }
    }

    sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
        object Home : Screen("home", "Accueil", Icons.Filled.Home)
        object Events : Screen("events", "Événements", Icons.Filled.ThumbUp)
        object Agenda : Screen("agenda", "Agenda", Icons.Filled.DateRange)
        object History : Screen("history", "Historique", Icons.Filled.List)
    }

    @Composable
    fun Navigation(
        navController: NavHostController,
        innerPadding: PaddingValues,
        scope: CoroutineScope,
        getGenerativeModel: () -> GenerativeModel,
        generateContent: suspend (
            coroutineScope: CoroutineScope,
            generativeModel: GenerativeModel,
            userInput: String,
            onResult: (String) -> Unit
        ) -> Unit,
        context: Context,
        responseList: MutableList<String>,
        db: AppDatabase
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                MainScreen(
                    innerPadding = innerPadding,
                    generateContent = generateContent,
                    getGenerativeModel = getGenerativeModel,
                    context = context,
                    coroutineScope = scope,
                    responseList = responseList,
                    db = db
                )
            }
            composable(Screen.Events.route) { EventsScreen(navController) }
            composable(Screen.Agenda.route) { AgendaScreen(db) }
            composable(Screen.History.route) { HistoryScreen() }
            composable(
                route = "event_detail/{eventId}/{eventName}",
                arguments = listOf(
                    navArgument("eventId") { type = NavType.IntType },
                    navArgument("eventName") { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val eventId = navBackStackEntry.arguments?.getInt("eventId")
                val eventName = navBackStackEntry.arguments?.getString("eventName")
                if (eventId != null && eventName != null) {
                    fr.isen.RAVAN.isensmartcompanion.composable.EventDetailScreen(
                        eventId,
                        eventName
                    )
                } else {
                    // Gérer le cas où eventId ou eventName est null (par exemple, afficher un message d'erreur)
                    Text("Erreur : ID ou nom de l'événement introuvable")
                }
            }
        }
    }
}