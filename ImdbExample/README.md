![](https://github.com/LeoIsasmendi/KotlinExamples/workflows/imdbexample/badge.svg)

# Aplicación The Movie Database API

Esta aplicación obtiene una lista de películas y al seleccionar una se muestra el detalle de la misma. En la vista de detalles se puede guardar la misma como favorita.
La aplicación cuenta con una pantalla para buscar películas y otra para listar las que hayan sido guardadas como favoritas.

## Herramientas usadas:
- Kotlin
- Retrofit2
- SharePreferences

## Requisitos:
- Tener una **Api Key** de [The Movie Database](https://www.themoviedb.org/)
- Una vez obtenida la **Api Key** reemplazar en el archivo [Helper.kt](app/src/main/java/com/example/imdbexample/Services/Helper.kt) la variable:

```kotlin
object Helper {
 ...
    const val API_KEY = ""
}
```
