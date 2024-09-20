package ar.edu.unsam.algo2.readapp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LibrosTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val borges = Autor("Jorge Luis", "Borges","j.Borges", Lenguaje.ESPANIOL, 54, 4)
    val walsh = Autor("Maria Elena", "Walsh", "M.Walsh", Lenguaje.ESPANIOL, 49, 4)
    val shakespeare = Autor("William", "Shakespeare","W.Shakespeare", Lenguaje.INGLES , 52 , 0)

    val libroDesafianteNoComplejo = Libro(2000,700,2,7000,false, borges)
    val libroComplejo = Libro(1400,300,1,9000,true, walsh)
    val libroNoDesafiante = Libro(120000,200,2,12000,false, borges)
    val libroIdiomas = Libro(4000,100,1,15000,false, shakespeare)

    describe("Libros desafiantes"){
        it("libroDesafianteNoComplejo deberia ser desafiante por tener mas de 600 paginas"){
            libroDesafianteNoComplejo.esDesafiante() shouldBe true
        }
        it("libroComplejo deberia ser desafiante por ser complejo"){
            libroComplejo.esDesafiante() shouldBe true
        }
        it("libroNoDesafiante no deberia ser desafiante"){
            libroNoDesafiante.esDesafiante() shouldBe false
        }
    }

    describe("Best seller"){
        it("si hay mas de 10000 ventas semanales y más de 5 traducciones, aunque menos de 2 ediciones"){
            libroIdiomas.agregarIdioma(Lenguaje.JAPONES)
            libroIdiomas.agregarIdioma(Lenguaje.INGLES)
            libroIdiomas.agregarIdioma(Lenguaje.ESPANIOL)
            libroIdiomas.agregarIdioma(Lenguaje.ALEMAN)
            libroIdiomas.agregarIdioma(Lenguaje.ITALIANO)

            libroIdiomas.esBestSeller() shouldBe true
        }
        it("tiene mas de 10000 ventas semanales y mas de 2 ediciones, aunque menos de 5 traducciones"){
            libroNoDesafiante.agregarIdioma(Lenguaje.HINDI)
            libroNoDesafiante.agregarIdioma(Lenguaje.PORTUGUES)

            libroNoDesafiante.esBestSeller() shouldBe true
        }
        it("tiene menos de 10000 ventas, menos de 2 ediciones y menos de 5 traducciones"){
            libroComplejo.agregarIdioma(Lenguaje.BENGALI)

            libroComplejo.esBestSeller() shouldBe false
        }
        it("tiene mas de 10000 ventas pero menos de 2 ediciones y menos de 5 traducciones"){
            libroIdiomas.agregarIdioma(Lenguaje.MANDARIN)
            libroIdiomas.agregarIdioma(Lenguaje.ARABE)
            libroIdiomas.agregarIdioma(Lenguaje.FRANCES)
            libroIdiomas.agregarIdioma(Lenguaje.RUSO)
            libroIdiomas.esBestSeller() shouldBe false
        }
    }

    describe("Idioma"){
        it("Idioma original es lengua nativa del autor"){
            libroIdiomas.idiomaOriginal shouldBe Lenguaje.INGLES
        }
        it("Suma de idiomas: 4 traducciones + idioma original"){
            libroIdiomas.agregarIdioma(Lenguaje.MANDARIN)
            libroIdiomas.agregarIdioma(Lenguaje.ARABE)
            libroIdiomas.agregarIdioma(Lenguaje.FRANCES)
            libroIdiomas.agregarIdioma(Lenguaje.RUSO)
            libroIdiomas.calculoIdiomasTotales() shouldBe 5
        }
    }

    describe("Autor"){
        it("Autor es consagrado"){
            libroNoDesafiante.autor.esConsagrado() shouldBe true
        }
        it("Autor no es consagrado por tener menos de 50 años"){
            libroComplejo.autor.esConsagrado() shouldBe false
        }
        it("Autor no es consagrado por tener menos de 1 premio"){
            libroIdiomas.autor.esConsagrado() shouldBe false
        }
    }
})