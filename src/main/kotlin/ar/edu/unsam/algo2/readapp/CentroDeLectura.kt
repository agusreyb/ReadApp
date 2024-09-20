package ar.edu.unsam.algo2.readapp


import java.time.LocalDate
import kotlin.math.roundToInt

class CentroDeLectura (val direccion: String, var duracion: Double, var libro: Libro, var tipodeCentro: TipoCentro) : Entidad {

    val fechasEncuentros = mutableSetOf<LocalDate>()
    val participantes = mutableListOf<Usuario>()
    val costoReservaBase: Double = 1000.00
    override var ID: Int = -1


    fun cupoParticipantes(): Int{
        return tipodeCentro.cantMaxParticipantes(this).roundToInt()
    }

    fun costoReserva(): Double{
       return tipodeCentro.calcularPrecioReserva(this)
    }

    fun solicitarReserva(usuario: Usuario){
         if ((participantes.size) >= cupoParticipantes())
             throw IllegalArgumentException("Se alcanzó el limite de reservas!")
         else 
             participantes.add(usuario)
    }

    fun agregarFecha(ano:Int, mes: Int, dia: Int) {
        fechasEncuentros.add(LocalDate.of(ano, mes, dia))
    }

    fun estaDisponibleEncuentro(): Boolean {
        return (algunaFechaVencida()) || ((participantes.size) < cupoParticipantes() )
    }

   private fun algunaFechaVencida():Boolean{
        val fechaActual = LocalDate.now()
        return fechasEncuentros.any{ it.isBefore(fechaActual) }
   }
}

interface TipoCentro {
    fun cantMaxParticipantes(centroDeLectura: CentroDeLectura): Double
    fun calcularPrecioReserva(centroDeLectura: CentroDeLectura):Double 
}

class Particular: TipoCentro {
    private var cantidadParticipantesMax: Double = 20.00
    private var limitePromocionalPorcentual: Double = 20.00 // /100 * cant participantes

    override fun cantMaxParticipantes(centroDeLectura: CentroDeLectura): Double = cantidadParticipantesMax

    fun cambiarCantidadParticipantes(cant : Double){ cantidadParticipantesMax = cant }
    
    override fun calcularPrecioReserva(centroDeLectura: CentroDeLectura):Double {
        return if ((centroDeLectura.participantes.size <= (limitePromocionalPorcentual/100 * cantidadParticipantesMax)))
                   centroDeLectura.costoReservaBase
               else centroDeLectura.costoReservaBase + 500
    }
}

class Editorial: TipoCentro {
    var costoLimiteReserva: Double = 20000.00
    var estaPresenteAutor: Boolean = true

    fun modificarPresenciaAutor() {
        if (estaPresenteAutor)
            estaPresenteAutor = false
        else
            estaPresenteAutor = true
    }

    override fun cantMaxParticipantes(centroDeLectura: CentroDeLectura): Double {
        return (costoLimiteReserva / calcularPrecioReserva(centroDeLectura))
    }

    override fun calcularPrecioReserva(centroDeLectura: CentroDeLectura):Double{
        return if(!estaPresenteAutor) {
            centroDeLectura.costoReservaBase + 800 
        } else costoSiEstaAutor(centroDeLectura)
    }

    fun cambiarCostoLimite(limite : Double){
        costoLimiteReserva = limite
    }

    private fun costoSiEstaAutor(centroDeLectura: CentroDeLectura):Double{
        return if (!centroDeLectura.libro.esBestSeller())
                 centroDeLectura.costoReservaBase + 800 + 200
        else ((centroDeLectura.costoReservaBase + 800) + (centroDeLectura.libro.ventasSemanales * 0.1))
    }
}    
class Biblioteca: TipoCentro {

    var dimensionSala: Double = 60.00
    private var gastosFijos = mutableListOf<Double>()

    fun agregarGastoFijo(valor: Double) { gastosFijos.add(valor) }

    fun cambiarDimensionSala(cant : Double){ dimensionSala = cant }

    override fun cantMaxParticipantes(centroDeLectura: CentroDeLectura): Double =  dimensionSala //por cada mt2 se necesita 1 mt2 (es lo mismo)

    override fun calcularPrecioReserva(centroDeLectura: CentroDeLectura):Double {
        return if (centroDeLectura.fechasEncuentros.size < 5)
                calculoValorAgregado(centroDeLectura) + centroDeLectura.costoReservaBase
               else calculoValorAgregado50(centroDeLectura) + centroDeLectura.costoReservaBase
    }

    private fun calculoValorAgregado(centroDeLectura: CentroDeLectura): Double {
        return ((((gastosFijos.sum() * 0.1) * (centroDeLectura.fechasEncuentros.size)) + gastosFijos.sum()) / centroDeLectura.participantes.size)
        //ej: ((30000 gasto * 10% * 4 fechas) + 30000 gasto) /  participantes
    }

    private fun calculoValorAgregado50(centroDeLectura: CentroDeLectura): Double {
        return ((gastosFijos.sum() * 0.5) + gastosFijos.sum()) / centroDeLectura.participantes.size
    }
}

