package cub.taifin

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import cub.taifin.ui.BooksList
import cub.taifin.ui.MovieAndBookApp

fun main() =
    singleWindowApplication(
        title = "InfiniteList",
        state = WindowState(size = DpSize(500.dp, 800.dp))
    ) {
        MovieAndBookApp()
    }