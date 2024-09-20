package ar.edu.unsam.algo2.readapp

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe


class RecomendacionesTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val borges = Autor("Jorge Luis", "Borges", "J.Borges",Lenguaje.ESPANIOL, 54, 4)
    val walsh = Autor("Maria Elena", "Walsh","M.Walsh", Lenguaje.ESPANIOL, 54, 4)

    val juan = Usuario(
        "Juan",
        "Perez",
        "JPerez",
        "JPerez@gmail.com",
        1996,
        7,
        13,
        Lenguaje.ESPANIOL,
        100.00
    )

    val amigoDeJuan = Usuario(
        "Amigo",
        "deJuan",
        "amigodejuan",
        "amigodejuan@gmail.com",
        2002,
        6,
        10,
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
        300.00
    )

    val libro1 = Libro(palabras = 4000, paginas = 100, ediciones = 1, ventasSemanales = 15000, complejo = false, borges)
    val libro2 = Libro(palabras = 4000, paginas = 100, ediciones = 3, ventasSemanales = 2000, complejo = false, walsh)
    val libro3 = Libro(palabras = 30800, paginas = 200, ediciones = 2, ventasSemanales = 15000, complejo = false, borges)

    val recomendacionPublica = juan.agregarRecomendacion(true)
    val recomendacionPrivada = juan.agregarRecomendacion(false)

    describe("Crear recomendacion") {
        val nuevaRecomendacion = maria.agregarRecomendacion(true)

        it("La nueva recomendación está en la lista de recomendaciones de juan") {
            maria.misRecomendaciones shouldBe listOf(nuevaRecomendacion)
        }
    }

    describe("Permiso de lectura") {
        it("Cualquiera puede leer una recomendación pública") {
            recomendacionPublica.permisoParaLeer(amigoDeJuan) shouldBe true
            recomendacionPublica.permisoParaLeer(maria) shouldBe true
        }
        it("No puede leer una recomendación privada si no es amigo del creador") {
            recomendacionPrivada.permisoParaLeer(amigoDeJuan) shouldBe false
        }
        it("Puede leer una recomendación privada si es amigo del creador") {
            juan.agregarAmigo(amigoDeJuan)

            recomendacionPrivada.permisoParaLeer(amigoDeJuan) shouldBe true
        }
    }

    describe("Permiso para agregar libros") {
        it("No puede agregar un libro si no lo leyó") {
            juan.agregarAmigo(amigoDeJuan)

            shouldThrow<IllegalArgumentException> {
                amigoDeJuan.agregarLibroARecomendacion(
                    libro1,
                    recomendacionPublica
                )
            }
        }
        it("No puede agregar un libro que él haya leido pero el creador no") {
            juan.agregarAmigo(amigoDeJuan)
            amigoDeJuan.leerLibro(libro1)

            shouldThrow<IllegalArgumentException> { amigoDeJuan.agregarLibroARecomendacion(libro1, recomendacionPublica) }
        }
        it("El creador tampoco puede agregar un libro que no leyó") {
            shouldThrow<IllegalArgumentException> { juan.agregarLibroARecomendacion(libro2, recomendacionPublica) }
        }
        it("Lo puede agregar si ambos leyeron el libro") {
            juan.agregarAmigo(amigoDeJuan)
            amigoDeJuan.leerLibro(libro1)
            amigoDeJuan.leerLibro(libro2)
            juan.leerLibro(libro1)
            juan.leerLibro(libro2)

            amigoDeJuan.agregarLibroARecomendacion(libro1, recomendacionPublica)
            juan.agregarLibroARecomendacion(libro2, recomendacionPublica)

            recomendacionPublica.libros shouldBe (setOf(libro1, libro2))
        }
    }

    describe("Permiso de edición") {
        juan.leerLibro(libro1)
        juan.leerLibro(libro2)
        juan.agregarLibroARecomendacion(libro1, recomendacionPublica)
        juan.agregarLibroARecomendacion(libro2, recomendacionPublica)

        it("No puede editar la recomendación si es amigo pero no leyó todos los libros") {
            juan.agregarAmigo(amigoDeJuan)
            amigoDeJuan.leerLibro(libro1)

            recomendacionPublica.permisoParaEditar(amigoDeJuan) shouldBe false
        }
        it("No puede editar la recomendación si leyó todos los libros pero no es amigo") {
            amigoDeJuan.leerLibro(libro1)
            amigoDeJuan.leerLibro(libro2)

            recomendacionPublica.permisoParaEditar(amigoDeJuan) shouldBe false
        }
        it("Puede editar la recomendación si leyó todos los libros y es amigo") {
            juan.agregarAmigo(amigoDeJuan)
            amigoDeJuan.leerLibro(libro1)
            amigoDeJuan.leerLibro(libro2)

            recomendacionPublica.permisoParaEditar(amigoDeJuan) shouldBe true
        }
        it("El creador de la reseña siempre puede editar la recomendación") {
            recomendacionPublica.permisoParaEditar(juan) shouldBe true
        }
        it("Edita el texto de la reseña") {
            juan.editarReseniaDeRecomendacion(recomendacionPublica, "El libro esta bueno")
            recomendacionPublica.resenia shouldBe "El libro esta bueno"
        }
    }

    describe("Tiempo de lectura de recomendación") {
        juan.leerLibro(libro2)
        juan.leerLibro(libro3)
        juan.agregarLibroARecomendacion(libro2, recomendacionPublica)
        juan.agregarLibroARecomendacion(libro3, recomendacionPublica)

        it("Todos los libros de la recomendacion") {
            amigoDeJuan.formaDeLeer = Ansioso()
            amigoDeJuan.librosPorLeer.add(libro2) //lo agrego para que la recomendación le aparezca en el feed

            amigoDeJuan.tiempoLecturaRecomendacionTotal(recomendacionPublica) shouldBe 93.00
        }
        it("Ahorrado por haber libros ya leidos en la recomendación") {
            amigoDeJuan.formaDeLeer = Ansioso()
            amigoDeJuan.librosPorLeer.add(libro2)
            amigoDeJuan.leerLibro(libro3)

            amigoDeJuan.tiempoLecturaRecomendacionAhorrado(recomendacionPublica) shouldBe 77.00
        }
        it("Neto") {
            amigoDeJuan.formaDeLeer = Ansioso()
            amigoDeJuan.librosPorLeer.add(libro2)
            amigoDeJuan.leerLibro(libro3)

            amigoDeJuan.tiempoLecturaRecomendacionNeto(recomendacionPublica) shouldBe 16.00
        }
    }

    describe("Valoraciones") {
        juan.leerLibro(libro1)
        juan.leerLibro(libro2)
        juan.leerLibro(libro3)
        amigoDeJuan.autoresPreferidos.add(borges)
        maria.autoresPreferidos.add(borges)
        juan.agregarAmigo(maria)
        juan.agregarLibroARecomendacion(libro2, recomendacionPublica)
        juan.agregarLibroARecomendacion(libro3, recomendacionPublica)
        juan.agregarLibroARecomendacion(libro1, recomendacionPrivada)
        juan.agregarLibroARecomendacion(libro3, recomendacionPrivada)

        it("Tiene permiso para valorar por leer todos los libros de la recomendacion") {
            amigoDeJuan.leerLibro(libro2)
            amigoDeJuan.leerLibro(libro3)
            amigoDeJuan.valorarRecomendacion(recomendacionPublica, 4.00, "Muy buena recomendacion")

            recomendacionPublica.valoraciones.size shouldBe 1
        }
        it("No tiene permiso para valorar si los autores de los libros no son los mismos y no leyó todos los libros") {
            amigoDeJuan.leerLibro(libro2)

            shouldThrow<IllegalArgumentException> { recomendacionPublica.valorar(amigoDeJuan, 3.00,"Buena recomendacion") }
        }
        it("No tiene permiso para valorar si es el creador de la recomendacion") {
            shouldThrow<IllegalArgumentException> { recomendacionPublica.valorar(juan, 2.00, "Mala recomendacion") }
        }
        it("Tiene permiso para valorar si el autor de los libros es el mismo y es el autor preferido") {
            amigoDeJuan.leerLibro(libro1)
            amigoDeJuan.valorarRecomendacion(recomendacionPrivada, 3.00,"Buena recomendacion")

            val valoresDeValoraciones = recomendacionPrivada.valoraciones.map{it.valor}
            valoresDeValoraciones shouldBe listOf(3.00)
        }
        it("No tiene permiso para valorar si ya la valoró"){
            amigoDeJuan.leerLibro(libro2)
            amigoDeJuan.leerLibro(libro3)
            amigoDeJuan.valorarRecomendacion(recomendacionPublica, 4.00,"Muy buena recomendacion")

            shouldThrow<IllegalArgumentException> { recomendacionPublica.valorar(amigoDeJuan, 2.00, "Mala recomendacion") }
        }
        it("Se puede editar una valoración"){
            maria.valorarRecomendacion(recomendacionPrivada, 5.00, "asd")
            amigoDeJuan.valorarRecomendacion(recomendacionPrivada, 3.00, "asd")

            maria.editarValoracionRecomendacion(recomendacionPrivada,4.00)
            val valoresDeValoraciones = recomendacionPrivada.valoraciones.map{it.valor}
            valoresDeValoraciones shouldBe listOf(4.00,3.00)
        }
        it("No puede poner un valor que no sea entre 1 y 5") {
            amigoDeJuan.leerLibro(libro1)

            shouldThrow<IllegalArgumentException> { amigoDeJuan.valorarRecomendacion(recomendacionPrivada, -1.00, "") }
            shouldThrow<IllegalArgumentException> { amigoDeJuan.valorarRecomendacion(recomendacionPrivada, 6.00, "") }
        }
        it("Promedio de valoraciones") {
            maria.autoresPreferidos.add(borges)
            amigoDeJuan.leerLibro(libro1)
            amigoDeJuan.leerLibro(libro3)
            amigoDeJuan.valorarRecomendacion(recomendacionPrivada, 3.00, "Buena recomendacion")
            maria.valorarRecomendacion(recomendacionPrivada, 4.00, "Muy buena recomendacion")

            recomendacionPrivada.promedioValoraciones() shouldBe 3.50
        }
    }
})