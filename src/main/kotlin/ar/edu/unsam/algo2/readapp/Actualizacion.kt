package ar.edu.unsam.algo2.readapp

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

interface ServiceLibros {

    fun getLibros() : String
}

@Serializable
class SerializableLibro(
    val ID : Int,
    val ediciones : Int,
    val ventasSemanales: Int
){}

class ActualizacionLibros(private val repositorioLibros : Repositorio<Libro>, private val servicioLibros : ServiceLibros) {

    fun actualizarRepositorio() {
        deserializarLibrosJson(servicioLibros.getLibros()).forEach {
            libro ->
            var libroRepositorio = repositorioLibros.getByID(libro.ID)!!
            libroRepositorio.ediciones = libro.ediciones
            libroRepositorio.ventasSemanales =  libro.ventasSemanales
        }

    }

    fun deserializarLibrosJson(jsonString : String): List<SerializableLibro> =
        Json.decodeFromString<List<SerializableLibro>>(jsonString)
}