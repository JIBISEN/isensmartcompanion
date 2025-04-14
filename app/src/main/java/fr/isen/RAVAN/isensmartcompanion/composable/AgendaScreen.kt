package fr.isen.RAVAN.isensmartcompanion.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.RAVAN.isensmartcompanion.database.Agenda
import fr.isen.RAVAN.isensmartcompanion.database.AppDatabase
import androidx.compose.ui.platform.LocalContext
import kotlin.collections.addAll
import kotlin.text.clear

@Composable
fun AgendaScreen(db: AppDatabase) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val agendaList = remember { mutableStateListOf<Agenda>() }
    LaunchedEffect(Unit) {
        db.agendaDao().getAll().collect {
            agendaList.clear()
            agendaList.addAll(it)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Agenda")
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(agendaList) { item ->
                Text(text = item.name)
            }
        }
    }
}