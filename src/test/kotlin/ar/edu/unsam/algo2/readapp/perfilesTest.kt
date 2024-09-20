package ar.edu.unsam.algo2.readapp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class PerfilesTest: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val juan = Usuario(
        "Juan",
        "Perez",
        "JPerez",
        "JPerez@gmail.com",
        1996,
        7,
        13,
        Lenguaje.ESPANIOL,
        200.00
    )

    val maria = Usuario(
        "Maria",
        "Perez",
        "MPerez",
        "MPerez@gmail.com",
        1990,
        4,
        22,
        Lenguaje.ESPANIOL,
        200.00
    )

    val amigoDeJuan = Usuario(
        "Amigo",
        "deJuan",
        "amigodejuan",
        "amigodejuan@gmail.com",
        2002,
        6,
        10,
        Lenguaje.INGLES,
        200.00
    )

    val steve = Usuario(
        "Steve",
        "Johnson",
        "SteveJohnson",
        "sjohnson@gmail.com",
        1999,
        3,
        4,
        Lenguaje.INGLES,
        200.00
    )

    val juanjose = Usuario(
        "Juan Jose",
        "Perez",
        "doublejj",
        "jjjjj@gmail.com",
        2004,
        3,
        5,
        Lenguaje.INGLES,
        200.00
    )

    val borges = Autor("Jose Luis", "Borges", "J.Borges",Lenguaje.ESPANIOL , 40 , 2)
    val shakespeare = Autor("William", "Shakespeare","W.Shakespeare", Lenguaje.INGLES , 99 , 2)

    val libroComedia = Libro(palabras = 2100000, paginas = 400,  ediciones=1, ventasSemanales = 15000, complejo = false, borges)
    val libroRomance = Libro(palabras = 8000000, paginas = 600,  ediciones=1, ventasSemanales = 15000, complejo = false, shakespeare)
    val libroRomance2 = Libro(palabras = 20000000, paginas = 800,  ediciones=1, ventasSemanales = 15000, complejo = false, shakespeare)
    val libroQuimica = Libro(palabras = 13000000, paginas = 600,  ediciones=1, ventasSemanales = 15000, complejo = false, borges)

    val precavido = Precavido()
    val leedor = Leedor()
    val poliglota = Poliglota()
    val nativista = Nativista()
    val calculador = Calculador()
    val demandante = Demandante()
    val experimentado = Experimentado()
    val cambiante = Cambiante()
    val multiple = Multiple()

    describe("Perfil 'Precavido'"){
        juan.cambiarPerfil(precavido)
        juan.agregarAmigo(amigoDeJuan)

        val recomendacionComedia = amigoDeJuan.agregarRecomendacion(true)
        amigoDeJuan.leerLibro(libroComedia)
        amigoDeJuan.agregarLibroARecomendacion(libroComedia, recomendacionComedia)
        val recomendacionRomance = maria.agregarRecomendacion(true)
        maria.leerLibro(libroRomance)
        maria.leerLibro(libroRomance2)
        maria.agregarLibroARecomendacion(libroRomance, recomendacionRomance)
        maria.agregarLibroARecomendacion(libroRomance2, recomendacionRomance)

        it("La única recomendación apta es recomendacionComedia porque contiene un libro que él tiene en librosPorLeer"){
            juan.agregarLibroPorLeer(libroComedia)
            juan.recomendacionEsApta(recomendacionComedia) shouldBe true
        }
        it("La única recomendación apta es recomendacionComedia porque el amigo de juan tiene leido un libro de esa recomendacion (libroComedia)"){
            juan.recomendacionEsApta(recomendacionComedia) shouldBe true
        }
        it("recomendacionRomance no es apta para él"){
            juan.recomendacionEsApta(recomendacionRomance) shouldBe false
        }
    }

    describe("Perfil 'Leedor'"){
        val recomendacionA = juan.agregarRecomendacion(true)
        val recomendacionB = amigoDeJuan.agregarRecomendacion(true)

        it("Todas las recomendaciones son aptas porque leedor no tiene preferencias"){
            maria.recomendacionEsApta(recomendacionA) shouldBe true
            maria.recomendacionEsApta(recomendacionB) shouldBe true
        }
    }

    describe("Perfil 'Poliglota'"){
        amigoDeJuan.cambiarPerfil(poliglota)

        val recomendacionSeisIdiomas = juan.agregarRecomendacion(true)
        val recomendacionDosIdiomas = maria.agregarRecomendacion(true)

        libroQuimica.agregarIdioma(Lenguaje.PORTUGUES)
        libroQuimica.agregarIdioma(Lenguaje.JAPONES)
        libroQuimica.agregarIdioma(Lenguaje.MANDARIN)
        libroQuimica.agregarIdioma(Lenguaje.HINDI)
        libroQuimica.agregarIdioma(Lenguaje.RUSO)
        juan.leerLibro(libroQuimica)
        juan.agregarLibroARecomendacion(libroQuimica, recomendacionSeisIdiomas)
        libroComedia.agregarIdioma(Lenguaje.INGLES)
        maria.leerLibro(libroComedia)
        maria.agregarLibroARecomendacion(libroComedia, recomendacionDosIdiomas)

        it("La única recomendacion apta es RecomendacionSeisIdiomas porque le gustan las recomendaciones con más de 5 idiomas"){
            amigoDeJuan.recomendacionEsApta(recomendacionSeisIdiomas) shouldBe true
        }
        it("recomendacionDosIdiomas no es apta para él porque tiene pocos idiomas"){
            amigoDeJuan.recomendacionEsApta(recomendacionDosIdiomas) shouldBe false
        }
    }

    describe("Perfil 'Nativista'"){
        steve.cambiarPerfil(nativista)

        val recomendacionConEspaniol = juan.agregarRecomendacion(true)
        val recomendacionConIngles = juan.agregarRecomendacion(true)

        juan.leerLibro(libroRomance)
        juan.agregarLibroARecomendacion(libroRomance, recomendacionConIngles)
        juan.leerLibro(libroComedia)
        juan.agregarLibroARecomendacion(libroComedia, recomendacionConEspaniol)

        it("La única recomendacion apta es RecomendacionConIngles porque lee libros cuyo idioma original es el mismo nativo de él"){
            steve.recomendacionEsApta(recomendacionConIngles) shouldBe true
        }
        it("recomendacionConEspaniol no es apta para él"){
            steve.recomendacionEsApta(recomendacionConEspaniol) shouldBe false
        }
    }

    describe("Perfil 'Calculador'") {

        juanjose.cambiarPerfil(calculador)

        val recomendacionComedia = juan.agregarRecomendacion(true)
        val recomendacionRomance = juan.agregarRecomendacion(true)
        val recomendacionQuimica = juan.agregarRecomendacion(true)

        calculador.cambioRangoTiempoLectura(30000.0, 60000.0)

        juan.leerLibro(libroComedia)
        juan.leerLibro(libroRomance)
        juan.leerLibro(libroQuimica)
        juan.agregarLibroARecomendacion(libroComedia, recomendacionComedia) //tiempo de lectura para juanjose: 10.500
        juan.agregarLibroARecomendacion(libroRomance, recomendacionRomance) //tiempo de lectura para juanjose: 40.000
        juan.agregarLibroARecomendacion(libroQuimica, recomendacionQuimica) //tiempo de lectura para juanjose: 65.000

        it("La única recomendación apta es recomendacionRomance porque el tiempo de lectura de la recomendacion es 40.000") {
            juanjose.recomendacionEsApta(recomendacionRomance) shouldBe true
        }
        it("Cambia su rango de tiempo de lectura entonces más recomendaciones son aptas para él") {
            calculador.cambioRangoTiempoLectura(40000.0, 90000.0)

            juanjose.recomendacionEsApta(recomendacionRomance) shouldBe true
            juanjose.recomendacionEsApta(recomendacionQuimica) shouldBe true
        }
        it("Si se agrega un libro con mayor tiempo de lectura deja de ser apta para él la recomendación") {
            calculador.cambioRangoTiempoLectura(40000.0, 90000.0)

            juan.leerLibro(libroRomance2)
            juan.agregarLibroARecomendacion(libroRomance2, recomendacionRomance) //tiempo de lectura para juanjose: 140.000
            juanjose.recomendacionEsApta(recomendacionRomance) shouldBe false
        }
        it("recomendacionComedia no es apta para él por tener un tiempo de lectura muy bajo") {
            juanjose.recomendacionEsApta(recomendacionComedia) shouldBe false
        }
    }


    describe("Perfil 'Demandante'"){
        juan.cambiarPerfil(demandante)

        val recomendacionComedia = maria.agregarRecomendacion(true)
        val recomendacionRomance = maria.agregarRecomendacion(true)

        maria.leerLibro(libroComedia)
        maria.leerLibro(libroRomance)
        maria.leerLibro(libroRomance2)
        maria.agregarLibroARecomendacion(libroComedia, recomendacionComedia)
        maria.agregarLibroARecomendacion(libroRomance, recomendacionRomance)
        maria.agregarLibroARecomendacion(libroRomance2, recomendacionRomance)
        amigoDeJuan.leerLibro(libroComedia)
        amigoDeJuan.valorarRecomendacion(recomendacionComedia, 4.00,"")
        steve.leerLibro(libroComedia)
        steve.valorarRecomendacion(recomendacionComedia, 4.50,"")
        juanjose.leerLibro(libroRomance)
        juanjose.leerLibro(libroRomance2)
        juanjose.valorarRecomendacion(recomendacionRomance, 2.00, "")

        it("La única recomendación apta es recomendacionComedia porque tiene una valoracion promedio de entre 4 y 5 puntos"){
            juan.recomendacionEsApta(recomendacionComedia) shouldBe true
        }
        it("recomendacionRomance no es apta porque tiene una valoración muy baja"){
            juan.recomendacionEsApta(recomendacionRomance) shouldBe false
        }
    }

    describe("Perfil 'Experimentado'"){
        steve.cambiarPerfil(experimentado)

        val recomendacionAutorConsagrado = amigoDeJuan.agregarRecomendacion(true)
        val recomendacionAutorNoConsagrado = amigoDeJuan.agregarRecomendacion(true)

        amigoDeJuan.leerLibro(libroComedia)
        amigoDeJuan.leerLibro(libroRomance)
        amigoDeJuan.agregarLibroARecomendacion(libroComedia, recomendacionAutorNoConsagrado)
        amigoDeJuan.agregarLibroARecomendacion(libroRomance, recomendacionAutorConsagrado)

        it("La única recomendación apta es recomendacionAutorConsagrado"){
            steve.recomendacionEsApta(recomendacionAutorConsagrado) shouldBe true
        }
        it("Si se agrega un libro de un autor no consagrado a la recomendación, deja de ser apta para él"){
            amigoDeJuan.leerLibro(libroQuimica)
            amigoDeJuan.agregarLibroARecomendacion(libroQuimica, recomendacionAutorConsagrado)
            steve.recomendacionEsApta(recomendacionAutorConsagrado) shouldBe false
        }
        it("recomendacionNoConsagrado no es apta para él"){
            steve.recomendacionEsApta(recomendacionAutorNoConsagrado) shouldBe false
        }
    }

    describe("Perfil 'Cambiante'"){
        maria.cambiarPerfil(cambiante)
        val recomendacionComedia = juan.agregarRecomendacion(true)
        val recomendacionRomance = juan.agregarRecomendacion(true)
        val recomendacionQuimica = juan.agregarRecomendacion(true)
        juan.leerLibro(libroComedia)
        juan.leerLibro(libroRomance)
        juan.leerLibro(libroQuimica)
        juan.agregarLibroARecomendacion(libroComedia, recomendacionComedia) //tiempo de lectura para maria: 10.500
        juan.agregarLibroARecomendacion(libroRomance, recomendacionRomance) //tiempo de lectura para maria: 40.000
        juan.agregarLibroARecomendacion(libroQuimica, recomendacionQuimica) //tiempo de lectura para maria: 65.000

        it("Como tiene perfil Calculador por tener más de 25 años, la única recomendación apta es recomendacionComedia porque tiene un tiempo de lectura entre 10.000 y 15.000"){
            maria.recomendacionEsApta(recomendacionComedia) shouldBe true
        }
        it("Si cambio la edad a menor de 25, pasa a ser Leedor, entonces todas las recomendaciones son aptas para ella"){
            maria.fechaNacimiento = LocalDate.of(2002, 3, 10)
            maria.recomendacionEsApta(recomendacionComedia) shouldBe true
            maria.recomendacionEsApta(recomendacionRomance) shouldBe true
            maria.recomendacionEsApta(recomendacionQuimica) shouldBe true
        }
    }

    describe("Perfil 'Multiple'"){
        steve.cambiarPerfil(multiple)

        it("Puede agregar perfiles a la lista"){
            multiple.agregarPerfil(poliglota) //hace todo desde multiple como objeto para no castear

            multiple.perfiles shouldBe setOf(poliglota)
        }
        it("Puede eliminar perfiles"){
            multiple.agregarPerfil(nativista)
            multiple.agregarPerfil(leedor)
            multiple.perfiles shouldBe setOf(nativista, leedor)

            multiple.eliminarPerfil(nativista)
            multiple.perfiles shouldBe setOf(leedor)
        }
        it("Agrego perfil Poliglota y Nativista entonces la recomendacionComedia no es apta para el usuario, pero recomendacionRomance y recomendacionSeisIdiomas si"){
            multiple.agregarPerfil(poliglota)
            multiple.agregarPerfil(nativista)

            val recomendacionComedia = amigoDeJuan.agregarRecomendacion(true)
            amigoDeJuan.leerLibro(libroComedia)
            amigoDeJuan.agregarLibroARecomendacion(libroComedia, recomendacionComedia)

            val recomendacionRomance = maria.agregarRecomendacion(true)
            maria.leerLibro(libroRomance)
            maria.leerLibro(libroRomance2)
            maria.agregarLibroARecomendacion(libroRomance, recomendacionRomance)
            maria.agregarLibroARecomendacion(libroRomance2, recomendacionRomance)

            val recomendacionCincoIdiomas = juan.agregarRecomendacion(true)
            libroQuimica.agregarIdioma(Lenguaje.PORTUGUES)
            libroQuimica.agregarIdioma(Lenguaje.JAPONES)
            libroQuimica.agregarIdioma(Lenguaje.MANDARIN)
            libroQuimica.agregarIdioma(Lenguaje.HINDI)
            juan.leerLibro(libroQuimica)
            juan.agregarLibroARecomendacion(libroQuimica, recomendacionCincoIdiomas)

            steve.recomendacionEsApta(recomendacionComedia) shouldBe false
            steve.recomendacionEsApta(recomendacionRomance) shouldBe true //porque el idioma original del libro es el mismo nativo de él
            steve.recomendacionEsApta(recomendacionCincoIdiomas) shouldBe true //porque tiene más de 5 idiomas
        }

        it("Agrego Experimentado y Precavido entonces son aptas las recomendaciones de acuerdo a esos perfiles"){
            multiple.agregarPerfil(experimentado)
            multiple.agregarPerfil(precavido)
            steve.agregarAmigo(juan)

            val recomendacionConEspaniol = juan.agregarRecomendacion(true)
            juan.leerLibro(libroComedia)
            juan.agregarLibroARecomendacion(libroComedia, recomendacionConEspaniol)

            val recomendacionDosIdiomas = juanjose.agregarRecomendacion(true)
            libroQuimica.agregarIdioma(Lenguaje.INGLES)
            juanjose.leerLibro(libroQuimica)
            juanjose.agregarLibroARecomendacion(libroQuimica, recomendacionDosIdiomas)

            val recomendacionAutorNoConsagrado = amigoDeJuan.agregarRecomendacion(true)
            amigoDeJuan.leerLibro(libroComedia)
            amigoDeJuan.agregarLibroARecomendacion(libroComedia, recomendacionAutorNoConsagrado)

            val recomendacionAutorConsagrado = maria.agregarRecomendacion(true)
            maria.leerLibro(libroRomance)
            maria.agregarLibroARecomendacion(libroRomance, recomendacionAutorConsagrado)

            steve.recomendacionEsApta(recomendacionConEspaniol) shouldBe true //porque aunque está en español, un amigo, juan, leyó el libro
            steve.recomendacionEsApta(recomendacionDosIdiomas) shouldBe false
            steve.recomendacionEsApta(recomendacionAutorNoConsagrado) shouldBe true //porque aunque el libro no es de un autor consagrado, un amigo, juan, leyó el libro
            steve.recomendacionEsApta(recomendacionAutorConsagrado) shouldBe true //porque el libro es de un autor consagrado
        }
    }
})