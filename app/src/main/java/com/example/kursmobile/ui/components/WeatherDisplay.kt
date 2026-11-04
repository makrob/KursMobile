package com.example.kursmobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kursmobile.domain.models.WeatherForecast
import com.example.kursmobile.R
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WeatherDisplay(forecast: WeatherForecast) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${forecast.cityName}, ${forecast.country}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.forecast_title),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(forecast.dailyForecasts) { daily ->
            DailyForecastCard(daily)
        }
    }
}

@Composable
fun DailyForecastCard(daily: com.example.kursmobile.domain.models.DailyForecast) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatDate(daily.date),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${daily.maxTemp.toInt()}°",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "${daily.minTemp.toInt()}°",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Avg: ${daily.avgTemp.toInt()}°",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())
        dateString
    } catch (e: Exception) {
        dateString
    }
}