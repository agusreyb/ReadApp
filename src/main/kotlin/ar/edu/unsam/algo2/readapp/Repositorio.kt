package ar.edu.unsam.algo2.readapp
import kotlinx.serialization.json.Json

interface Entidad{
    var ID : Int
}
class Repositorio<T : Entidad>(val criterioBusqueda: TipoCriterio<T>) {
    var elementos: MutableList<T> = mutableListOf()
    var IDActual = 0

    fun create(elemento: T) {
        elemento.ID = IDActual
        IDActual += 1
        elementos.add(elemento)
    }

    fun delete(elemento: T) {
        elementos.remove(elemento)
    }

    fun update(elemento: T){
       val indiceAModificar = elementos.indexOfFirst {it.ID == elemento.ID}
        if (indiceAModificar == -1)
            throw IllegalArgumentException("No se encontró elemento con la misma ID")
        elementos.remove(elementos[indiceAModificar])
        elementos.add(elemento)

    }
    fun getByID(IDbuscada: Int): T? {
        val elementoEncontrado = elementos.find { it.ID == IDbuscada }
        if (elementoEncontrado == null)
            throw IllegalArgumentException("No se encontró la ID")
        return elementoEncontrado
    }

    fun search(valor: String): List<T> {
        return criterioBusqueda.filtrarBusqueda(valor, this)
    }
    
    fun listarCentrosLectura(repositorio: Repositorio<CentroDeLectura>): List<CentroDeLectura> {
        return repositorio.elementos.toList()
    }
}




interface TipoCriterio<T : Entidad> {
    fun filtrarBusqueda(valor: String, repositorio: Repositorio<T>): List<T>
}

object CriterioLibros : TipoCriterio<Libro> {
    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<Libro>): List<Libro> {
        return repositorio.elementos.filter { it.titulo.contains(valor, ignoreCase = true) ||
                it.autor.apellido.contains(valor, ignoreCase = true) }
    }
}

object CriterioUsuarios: TipoCriterio<Usuario>{
    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<Usuario>): List<Usuario> {
        return repositorio.elementos.filter { it.nombre.contains(valor, ignoreCase = true) ||
                it.apellido.contains(valor, ignoreCase = true) ||
                it.username == valor
        }
    }
}

object CriterioAutores: TipoCriterio<Autor>{
    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<Autor>): List<Autor> {
        return repositorio.elementos.filter { it.nombre.contains(valor, ignoreCase = true) ||
                it.apellido.contains(valor, ignoreCase = true) ||
                it.seudonimo == valor
        }
    }
}

object CriterioRecomendaciones: TipoCriterio<Recomendacion>{
    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<Recomendacion>): List<Recomendacion> {
        return repositorio.elementos.filter {it.creador.apellido.contains(valor, ignoreCase = true) ||
                it.libros.any{ it.titulo.contains(valor, ignoreCase = true) }
        }
    }
}

object CriterioCentrosDeLectura: TipoCriterio<CentroDeLectura>{

    override fun filtrarBusqueda(valor: String, repositorio: Repositorio<CentroDeLectura>): List<CentroDeLectura> {
        return repositorio.elementos.filter{ it.libro.titulo == valor }
    }
}




