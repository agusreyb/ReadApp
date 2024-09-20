package ar.edu.unsam.algo2.readapp

class Libro (
    val palabras: Int,
    val paginas: Int,
    var ediciones: Int,
    var ventasSemanales: Int,
    val complejo: Boolean,
    val autor: Autor,
    val titulo : String = "") : Entidad {

    override var ID = -1
    val traducciones = mutableListOf<Lenguaje>()
    val idiomaOriginal = autor.lenguaNativa
    var paginasLargo = 600

    fun calculoIdiomasTotales() = traducciones.plus(idiomaOriginal).toMutableSet().size

    fun esLargo() = this.paginas >= paginasLargo
    fun esDesafiante() = complejo || this.esLargo()

    fun agregarIdioma(idiomaIngresado: Lenguaje) {
        traducciones.add(idiomaIngresado)
    }

   fun esBestSeller() = ventasSemanales > 10000 && (ediciones >= 2 || traducciones.size >=5)
}

class Autor(

    val nombre: String,
    val apellido: String,
    val seudonimo: String,
    val lenguaNativa: Lenguaje,
    private var edad: Int,
    private var cantPremios: Int
) : Entidad {

    override var ID = -1
    fun esConsagrado()= edad >= 50 && cantPremios >= 1


}