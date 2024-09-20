package ar.edu.unsam.algo2.readapp

open class Recomendacion (var creador : Usuario, private var esPublica : Boolean = true) : Permisos(), Entidad{
    override var ID : Int = -1

    var libros = mutableSetOf<Libro>()
    var resenia = ""
    val valoraciones: MutableList<Valoracion> = mutableListOf()

    //Tiempos de lectura
    private fun librosYaLeidos (usuario : Usuario) = libros.filter { cadaLibro -> cadaLibro in usuario.librosLeidos}

    fun tiempoLecturaRecomendacion(usuario: Usuario) : Double = this.libros.sumOf { cadaLibro -> usuario.tiempoDeLectura(cadaLibro) }

    fun tiempoLecturaRecomendacionAhorrado(usuario : Usuario) :Double = this.librosYaLeidos(usuario).sumOf { cadaLibro -> usuario.tiempoDeLectura(cadaLibro)}

    fun tiempoLecturaNeto(usuario: Usuario) = tiempoLecturaRecomendacion(usuario) - tiempoLecturaRecomendacionAhorrado(usuario)

    //Permisos

    fun permisoParaLeer(usuario : Usuario): Boolean {
        return if (esPublica) true
        else esAmigo(usuario, this.creador)
    }

    fun permisoParaEditar (usuario: Usuario) : Boolean{
       return (esAmigo(usuario, creador) and libroEstaLeido(usuario, this)) or (usuario == creador)
    }

    fun permisoValoracion(usuario: Usuario): Boolean {
        return (creador!= usuario &&
                (libroEstaLeido(usuario, this) || (todosTienenMismoAutor(this) && comprueboAutorPreferido(usuario, this)))) &&
                (!this.valoraciones.any { valoracion -> valoracion.creador == usuario })
    }

    //Valoracion
    private fun encuentraValoracionUsuario(usuario: Usuario): Int{
        return valoraciones.indexOfFirst { it.creador == usuario }
    }

    fun valorar(usuario: Usuario, valor: Double, comentario: String){
        val valoracion = Valoracion(usuario, valor, comentario)
        valoracion.validacionValoracion(usuario, valor, this)
        valoraciones.add(valoracion)
    }

    fun promedioValoraciones(): Double = valoraciones.sumOf { valoracion -> valoracion.valor } / valoraciones.size

    fun editarValoracion(usuario: Usuario, valor: Double){
        if (this.encuentraValoracionUsuario(usuario) == -1)
            throw IllegalArgumentException("El usuario '$usuario' no dejó ninguna recomendación")
        valoraciones[encuentraValoracionUsuario(usuario)].editarValor(valor)
    }

    //Edición
    fun agregarLibro(usuario: Usuario, libro: Libro){
        validacionPermisoEdicion(usuario)
        validacionPermisoAgregarLibros(usuario, libro)
        libros.add(libro)
    }

    //Validaciones
    private fun validacionPermisoEdicion(usuario: Usuario) {
        if (!permisoParaEditar(usuario)) {
            throw IllegalArgumentException("El usuario '$usuario' no tiene permiso para editar esta recomendación.")
        }
    }

    private fun validacionPermisoAgregarLibros(usuario: Usuario, libro: Libro) {
        if (!permisoAgregarLibro(this, usuario, libro)) {
            throw IllegalArgumentException("Usted y el autor de la reseña deben haber leído el libro")
        }
    }
    
    fun editarResenia(usuario: Usuario, texto: String) {
        validacionPermisoEdicion(usuario)
        resenia = texto
    }
}

abstract class Permisos{
    fun esAmigo(usuario : Usuario, creadorDeResenia: Usuario) : Boolean {
        return creadorDeResenia.amigos.contains(usuario)
    }
    fun libroEstaLeido(usuario: Usuario, recomendacion : Recomendacion): Boolean {
        return usuario.librosLeidos.containsAll(recomendacion.libros)
    }
    fun permisoAgregarLibro (recomendacion: Recomendacion, usuario: Usuario, libro: Libro): Boolean{
        return usuario.librosLeidos.contains(libro) and recomendacion.creador.librosLeidos.contains(libro)
    }
    fun todosTienenMismoAutor (recomendacion: Recomendacion): Boolean{
        val mapAutores = recomendacion.libros.map{it.autor}.toSet()
        return mapAutores.size == 1
    }
    fun comprueboAutorPreferido(usuario: Usuario, recomendacion: Recomendacion): Boolean{
        return usuario.autoresPreferidos.contains(recomendacion.libros.firstOrNull()?.autor)
    }
}

class Valoracion(var creador: Usuario, var valor: Double, comentario: String) {
    private fun validacionValor(valor: Double) {
        if ((valor <= 0) or (valor > 5)) {
            throw IllegalArgumentException("El numero no es válido")
        }
    }

    private fun validacionPermisoParaValorar(usuario: Usuario, recomendacion: Recomendacion) {
        if (!recomendacion.permisoValoracion(usuario)) {
            throw IllegalArgumentException("Usted no tiene permiso para valorar esta recomendacion")
        }
    }

    fun validacionValoracion(usuario: Usuario, valor: Double, recomendacion: Recomendacion) {
        validacionValor(valor)
        validacionPermisoParaValorar(usuario, recomendacion)
    }

    fun editarValor(nuevoValor: Double){
        validacionValor(nuevoValor)
        valor = nuevoValor
    }
}
