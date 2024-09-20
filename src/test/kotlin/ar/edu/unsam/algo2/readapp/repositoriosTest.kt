package ar.edu.unsam.algo2.readapp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class RepositoriosTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    val maria = Usuario("Maria", "Perez", "MPerez", "MPerez@gmail.com", 1990, 4, 22, Lenguaje.ESPANIOL, 300.00)

    val borges = Autor("Jorge Luis", "Borges","J.Borges" ,Lenguaje.ESPANIOL, 54, 4)
    val walsh = Autor("Maria Elena", "Walsh","M.Walsh", Lenguaje.ESPANIOL, 54, 4)

    val libro1 = Libro(palabras = 4001, paginas = 100, ediciones = 1, ventasSemanales = 15000, complejo = false, borges, "El Inmortal")
    val libro2 = Libro(palabras = 4000, paginas = 100, ediciones = 3, ventasSemanales = 2000, complejo = false, walsh, "La Reina Batata")

//Repositorio de libros
    val repositorioLibros = Repositorio(CriterioLibros)
    val libro3 = Libro(1400,300,1,9000,true, walsh, "El Mono Liso")
    val libro4 = Libro(1403,300,1,9000,true, borges, "libroBorges")

    repositorioLibros.create(libro3)
    repositorioLibros.create(libro4)



//Repositorio de usuarios
    val repositorioUsuarios = Repositorio(CriterioUsuarios)
    val juan = Usuario("Juan","Perez","JPerez","JPerez@gmail.com",1996,7,13,Lenguaje.ESPANIOL,200.00)
    val pedro = Usuario("Pedro","Gonzales","PGonzales","pedrogonzales@gmail.com",1996,7,13,Lenguaje.ESPANIOL,200.00)

    repositorioUsuarios.create(juan)
    repositorioUsuarios.create(pedro)

//Repositorio de autores
    val repositorioAutores = Repositorio(CriterioAutores)
    repositorioAutores.create(borges)
    repositorioAutores.create(walsh)


//Repositorio de recomendaciones
    val repositorioRecomendaciones = Repositorio(CriterioRecomendaciones)
    val recomendacionDeMaria = Recomendacion(maria, true)

    repositorioRecomendaciones.create(recomendacionDeMaria)
    maria.leerLibro(libro1)
    maria.leerLibro(libro2)
    recomendacionDeMaria.agregarLibro(maria, libro1)
    recomendacionDeMaria.agregarLibro(maria, libro2)

//Repositorios de centros de lectura
    val repositorioCentros = Repositorio(CriterioCentrosDeLectura)
    val centroDeLectura = CentroDeLectura("direccion", 2.00,libro1, Particular())
    repositorioCentros.create(centroDeLectura)

    describe("Funciones de crear, eliminar, y buscar por ID") {
        it("crear elemento") {
            repositorioLibros.create (libro1)


            repositorioLibros.elementos shouldBe listOf(libro3,libro4,libro1)
        }
        it("eliminar elemento") {
            repositorioLibros.delete(libro4)

            repositorioLibros.elementos.size shouldBe 1
        }
        it ("Actualizar un elemento"){
            val libroIDRepetida= Libro(1400,300,1,9000,true, walsh, "libro")
            libroIDRepetida.ID = 1
            repositorioLibros.update(libroIDRepetida)
            repositorioLibros.elementos shouldBe listOf(libro3,libroIDRepetida)
        }
        it ("Búsqueda por ID"){
            repositorioLibros.getByID(1)?.titulo shouldBe "libroBorges"
        }
        it ("Buscar una ID que no existe da error"){
            shouldThrow<IllegalArgumentException> {repositorioLibros.getByID(99)}
        }
    }

    describe ("Búsqueda por string de libros"){
        it ("Buscar por título del libro"){
            val monoRayado = Libro(1400,300,1,9000,true, walsh, "El Mono Rayado")
            repositorioLibros.create (monoRayado)

            repositorioLibros.search("el mono").toList() shouldBe listOf(libro3, monoRayado)
        }
        it ("Buscar por apellido del autor"){


            repositorioLibros.search("bor").toList() shouldBe listOf(libro4)
        }
    }

    describe ("Búsqueda por string de usuarios"){
        it ("Búsqueda que coincide parcialmente"){
            repositorioUsuarios.search("ju").toList() shouldBe listOf(juan)
        }
        it ("Busqueda por username que coincide exactamente"){
            repositorioUsuarios.search("PGonzales").toList() shouldBe listOf(pedro)
        }
        it ("Si el username sólo coincide parcialmente, no lo encuentra"){
            repositorioUsuarios.search("pGonzales").toList().isEmpty() shouldBe true
            repositorioUsuarios.search("PGon").toList().isEmpty() shouldBe true
        }
    }

    describe ("Búsqueda por string de autor"){
        it ("Búsqueda que coincide parcialmente"){
            repositorioAutores.search("wals").toList() shouldBe listOf(walsh)
        }
        it ("Busqueda por username que coincide exactamente"){
            repositorioAutores.search("M.Walsh").toList() shouldBe listOf(walsh)
        }
        it ("Si el seudónimo sólo coincide parcialmente, no lo encuentra"){
            repositorioAutores.search("M.W").toList().isEmpty() shouldBe true
            repositorioAutores.search("ash").toList().isEmpty() shouldBe true
        }
    }

    describe ("Búsqueda de recomendaciones"){
        it ("Busco título incluido en la recomendación"){
            repositorioRecomendaciones.search("la reina").toList() shouldBe listOf(recomendacionDeMaria)
        }
    }

    describe ("Búsqueda de repositorio"){
        it ("La búsqueda coincide exactamente con el título del libro"){
            repositorioCentros.search("El Inmortal").toList() shouldBe listOf(centroDeLectura)
        }
    }
})