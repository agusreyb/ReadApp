package ar.edu.unsam.algo2.readapp


import java.time.LocalDate
import java.time.Period

class Usuario (

    val nombre: String,
    val apellido: String,
    val username: String,
    val mail: String,
    private var anioNacimiento: Int,
    private val mesNacimiento: Int,
    private val diaNacimiento: Int,
    val lenguaNativa: Lenguaje,
    private var vPromedio: Double,
    override var ID: Int = -1) : Entidad {

    var perfil : Perfil = Leedor()
    var fechaNacimiento: LocalDate = LocalDate.of(anioNacimiento, mesNacimiento, diaNacimiento)
    val amigos = mutableSetOf<Usuario>()
    val librosLeidos = mutableSetOf<Libro>()
    val librosPorLeer = mutableSetOf<Libro>()
    val misRecomendaciones = mutableListOf<Recomendacion>()
    private val recomendacionesAValorar = mutableListOf<Recomendacion>()
    val autoresPreferidos = mutableSetOf<Autor>()
    var formaDeLeer : FormaDeLeer = Promedio()
    val cantVecesLeido = mutableMapOf<Libro, Int>()

    fun edad(): Int = Period.between(fechaNacimiento, LocalDate.now()).years

    fun calculaVelocidadPromedio(libro : Libro) = if (libro.esDesafiante()) vPromedio*2 else vPromedio

    fun agregarAutorPreferido(autor: Autor){
        autoresPreferidos.add(autor)
    }

    fun agregarAmigo(amigo : Usuario){
        amigos.add(amigo)
    }

    fun leerLibro(libro : Libro){  //SE PUEDE MEJORAR CAMBIANDO LIBROS LEIDOS POR UN MAPA COMPLETO
        if (!librosLeidos.contains(libro)){
            librosLeidos.add(libro)
            cantVecesLeido[libro]=1}
        else { cantVecesLeido[libro]=(cantVecesLeido.getValue(libro) + 1) }
    }

    fun agregarLibroPorLeer(libro : Libro){
        librosPorLeer.add(libro)
    }

    fun cambiarPerfil(tipoPerfil: Perfil){
        perfil = tipoPerfil
    }


    //Tiempo de Lectura
    fun tiempoDeLecturaPromedio(libro: Libro):Double = libro.palabras / vPromedio

    fun tiempoDeLectura(libro : Libro): Double {
        return formaDeLeer.tiempoDeLectura(libro, this)
    }

    fun tiempoLecturaRecomendacionTotal(recomendacion: Recomendacion) = recomendacion.tiempoLecturaRecomendacion(this)

    fun tiempoLecturaRecomendacionAhorrado(recomendacion: Recomendacion) = recomendacion.tiempoLecturaRecomendacionAhorrado(this)

    fun tiempoLecturaRecomendacionNeto(recomendacion: Recomendacion) = recomendacion.tiempoLecturaNeto(this)

    //Recomendaciones
    private fun crearRecomendacion(esPublica : Boolean ) = Recomendacion(this, esPublica)

    fun recomendacionEsApta(recomendacion: Recomendacion): Boolean =
        perfil.esApta(recomendacion, this) and (recomendacion.permisoParaLeer(this))

    fun agregarRecomendacion(esPublica : Boolean ): Recomendacion {
        val recomendacionNueva = crearRecomendacion(esPublica)
        misRecomendaciones.add(recomendacionNueva)
        return recomendacionNueva
    }

    fun agregarLibroARecomendacion(libro: Libro, recomendacion: Recomendacion) {
        recomendacion.agregarLibro(this, libro)
    }

    fun editarReseniaDeRecomendacion(recomendacion: Recomendacion, texto: String){
        recomendacion.editarResenia(this, texto)
    }

    fun valorarRecomendacion(recomendacion: Recomendacion, valor: Double, comentario: String){
        recomendacion.valorar(this, valor, comentario)
    }

    fun editarValoracionRecomendacion(recomendacion: Recomendacion, valor: Double){
        recomendacion.editarValoracion(this, valor)
    }

    fun agregarRecomendacionAValorar(recomendacionAValorar: Recomendacion){
        recomendacionesAValorar.add(recomendacionAValorar)
    }
}




