package cub.taifin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cub.taifin.ui.MovieAndBookApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Setting content")
        setContent {
            MovieAndBookApp("http://10.0.2.2:8080")
        }
    }
}
