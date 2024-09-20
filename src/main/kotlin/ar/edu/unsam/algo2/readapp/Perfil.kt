package ar.edu.unsam.algo2.readapp

interface Perfil{
    fun esApta(recomendacion : Recomendacion, usuario : Usuario) : Boolean
}

class Precavido : Perfil {
    override fun esApta(recomendacion : Recomendacion, usuario : Usuario) =
        recomendacion.libros.any{ cadaLibro -> usuario.librosPorLeer.contains(cadaLibro) ||
                usuario.amigos.any{ cadaAmigo -> cadaAmigo.librosLeidos.contains(cadaLibro)}}
}

class Leedor : Perfil {
    override fun esApta(recomendacion: Recomendacion, usuario: Usuario) = true
}

class Poliglota : Perfil {
    override fun esApta(recomendacion: Recomendacion, usuario: Usuario): Boolean =
        recomendacion.libros.sumOf{ libro -> libro.calculoIdiomasTotales() } >= 5
}

class Nativista : Perfil {
    override fun esApta(recomendacion: Recomendacion, usuario: Usuario): Boolean =
        recomendacion.libros.any{ cadaLibro -> ((cadaLibro.idiomaOriginal) == (usuario.lenguaNativa))}
}

class Calculador : Perfil {
    private var rangoTiempoLectura : ClosedRange<Double> = 0.00.rangeTo(1000.00)
    override fun esApta(recomendacion: Recomendacion, usuario: Usuario): Boolean =
         recomendacion.tiempoLecturaRecomendacion(usuario) in rangoTiempoLectura

    fun cambioRangoTiempoLectura(nuevoMin: Double, nuevoMax: Double) {
        rangoTiempoLectura = nuevoMin.rangeTo(nuevoMax)
    }
}

class Demandante : Perfil {
    override fun esApta(recomendacion: Recomendacion, usuario: Usuario): Boolean =
        recomendacion.valoraciones.any{ cadaValoracion -> (cadaValoracion.valor in 4.0..5.0)}
}

class Experimentado : Perfil {
    override fun esApta(recomendacion: Recomendacion, usuario: Usuario): Boolean =
        recomendacion.libros.count{ cadaLibro -> cadaLibro.autor.esConsagrado()} > recomendacion.libros.size / 2
}

class Cambiante : Perfil {
    override fun esApta(recomendacion: Recomendacion, usuario: Usuario): Boolean {
        return if (usuario.edad() <= 25) {
            Leedor().esApta(recomendacion, usuario)
        } else {
            return cambiaACalculador(Calculador()).esApta(recomendacion, usuario)
        }
    }

    private fun cambiaACalculador (perfilNuevoCalculador : Calculador ) : Calculador {
        perfilNuevoCalculador.cambioRangoTiempoLectura(10000.00, 15000.00)
        return perfilNuevoCalculador
    }
}

class Multiple : Perfil {
    var perfiles: MutableSet<Perfil> = mutableSetOf()

    fun agregarPerfil(tipoPerfil: Perfil){
        perfiles.add(tipoPerfil)
    }

    fun eliminarPerfil(tipoPerfil: Perfil){
        perfiles.remove(tipoPerfil)
    }

    override fun esApta(recomendacion: Recomendacion, usuario: Usuario): Boolean {
        return perfiles.any{ perfil -> perfil.esApta(recomendacion, usuario)}
    }
}