# Readme

## Task 1
To run server, use 
```bash
./gradlew :server:run 
```

You need an environmental variable `MOVIES_API_TOKEN` to be set to read-only API key of https://www.themoviedb.org/. For your convinience, there is a dedicated read-only api token --that I do not care about and thus expose it consciously-- that can be safely used:
```bash
MOVIES_API_TOKEN=eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYmQ3MTFhODk2ZWNiZDFiYTE4NDk3M2IyMzIwOWFiNyIsIm5iZiI6MTczMTM0MTc4Ni43NDM4NDI0LCJzdWIiOiI2NzI3ZjZjNTM5OGM5MDQzZjgwZDM3NjciLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.n1vtDISVWL9NCN4xIZncBz_2CC-UaTCcrUhMmjxzqho ./gradlew :server:run
```

To run desktop app, execute
```bash
./gradlew :app:desktopApp:jvmRun -DmainClass=cub.taifin.MainKt
```

To run android app, please use Android Studio/IntelliJ run configuration `app.AndroidApp`. 

You may need to explicitly click one of the buttons (movies/books) to trigger the loading of the desired list for the first time. 

## Task 2

Gradle tasks `commit-info`, `build-info` and `sources-info` are available for a project that uses `cub.taifin.projectInfo` gradle plugin, e.g. root project. 
