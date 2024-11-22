@file:Suppress("FunctionName")

package cub.taifin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MovieAndBookApp() {
    var selectedMode by remember { mutableStateOf(DisplayMode.MOVIES) }

    Column {
        Box(modifier = Modifier.weight(0.9F)) {
            when (selectedMode) {
                DisplayMode.BOOKS -> BooksList()
                DisplayMode.MOVIES -> MoviesList()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(0.1F),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { selectedMode = DisplayMode.MOVIES },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Movie Icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Movies")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { selectedMode = DisplayMode.BOOKS },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Book Icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Books")
            }
        }
    }
}