package com.example.practica2_appi

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.practica2_appi.ui.theme.Practica2_AppiTheme
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Practica2_AppiTheme {
                // A surface container using the 'background' color from the theme
                            FootballScreen()
            }
        }
    }
}


data class FootballTeam(
    val name: String,
    val country: String
)

// Define la interfaz para la API utilizando Retrofit
interface FootballApi {
    @GET("teams")
    suspend fun getTeams(): List<FootballTeam>
}

// Define un objeto Retrofit para crear instancias de la interfaz de la API
object RetrofitClient {
    private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/1/"
    val instance: FootballApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FootballApi::class.java)
    }
}

// Define un ViewModel para manejar la lógica de negocio
class FootballViewModel : ViewModel() {
    var teams by mutableStateOf<List<FootballTeam>>(emptyList())

    fun getTeams() {
        viewModelScope.launch {
            teams = RetrofitClient.instance.getTeams()
        }
    }
}

// Define la pantalla que muestra la lista de equipos de fútbol
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FootballScreen(viewModel: FootballViewModel = ViewModelProvider(LocalContext.current as ComponentActivity).get(FootballViewModel::class.java)) {
    val teams = viewModel.teams

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Football Teams") })
        }
    ) {
        TeamList(teams)
    }
}

// Define una composable para mostrar la lista de equipos de fútbol
@Composable
fun TeamList(teams: List<FootballTeam>) {
    Column(modifier = Modifier.padding(16.dp)) {
        for (team in teams) {
            Text(text = "${team.name} - ${team.country}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun  Practica2_AppiTheme (content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Practica2_AppiTheme {

    }
}