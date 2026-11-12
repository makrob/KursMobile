package com.example.kursmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kursmobile.data.database.AppDatabase
import com.example.kursmobile.data.datastore.PreferencesDataStore
import com.example.kursmobile.data.network.di.NetworkModule
import com.example.kursmobile.data.repository.WeatherRepositoryImpl
import com.example.kursmobile.domain.usecases.*
import com.example.kursmobile.ui.WeatherScreen
import com.example.kursmobile.ui.WeatherViewModel
import com.example.kursmobile.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
fun WeatherApp() {
    val context = LocalContext.current

    val viewModel: WeatherViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val database = AppDatabase.getInstance(context)
                val preferencesDataStore = PreferencesDataStore(context)
                val cityCoordinatesApi = NetworkModule.provideCityApiService()
                val weatherApi = NetworkModule.provideWeatherApiService()

                val repository = WeatherRepositoryImpl(
                    database = database,
                    cityCoordinatesApi = cityCoordinatesApi,
                    weatherApi = weatherApi
                )

                WeatherViewModel(
                    searchCitiesUseCase = SearchCitiesUseCase(repository),
                    getCityCoordinatesUseCase = GetCityCoordinatesUseCase(repository),
                    getWeatherForecastUseCase = GetWeatherForecastUseCase(repository),
                    preferencesDataStore = preferencesDataStore
                )
            }
        }
    )

    WeatherScreen(viewModel = viewModel)
}