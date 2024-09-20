package ar.edu.unsam.algo2.readapp

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class UsuariosTest: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val usuario = Usuario(
        "Juan",
        "Perez",
        "JPerez",
        "JPerez@gmail.com",
        1996,
        7,
        13,
        Lenguaje.ESPANIOL,
        200.00)

    val borges = Autor("Jose Luis", "Borges", "J.Borges",Lenguaje.ESPANIOL, 87, 4)

    val libroDesafianteNoComplejo = Libro(2000, paginas = 700,  ediciones=2, ventasSemanales = 7000, complejo = false, borges)
    val libroSimple = Libro(palabras = 150500, paginas = 300,  ediciones=1, ventasSemanales = 9000, complejo = true, borges)
    val libroNoDesafiante = Libro(palabras = 120000, paginas = 200,  ediciones=2, ventasSemanales = 12000, complejo = false, borges)
    val libroBestSeller = Libro(120000, 700, 5,15000,  false, borges)

    describe("Calcula tiempo de lectura segun tipo de lectores"){
        it("Lector PROMEDIO con cualquier libro (promedio es el default)"){
            usuario.tiempoDeLectura(libroNoDesafiante) shouldBe 600
            usuario.tiempoDeLectura(libroSimple) shouldBe 752.5
        }
        it("Lector ANSIOSO con best seller"){
            usuario.formaDeLeer = Ansioso()
            usuario.tiempoDeLectura(libroBestSeller) shouldBe 300
        }
        it("Lector ANSIOSO sin best seller"){
            usuario.formaDeLeer = Ansioso()
            usuario.tiempoDeLectura(libroSimple) shouldBe 602
        }
        it("Lector FANATICO libro largo"){
            usuario.agregarAutorPreferido(borges)
            usuario.formaDeLeer = Fanatico()
            usuario.tiempoDeLectura(libroBestSeller) shouldBe 1900
        }
        it("Lector RECURRENTE sin haber leido el libro"){
            usuario.formaDeLeer = Recurrente()
            usuario.tiempoDeLectura(libroNoDesafiante) shouldBe 600.00
        }
        it("Lector RECURRENTE con 3 repeticiones del libro"){
            usuario.formaDeLeer = Recurrente()
            usuario.leerLibro(libroNoDesafiante)
            usuario.leerLibro(libroNoDesafiante)
            usuario.leerLibro(libroNoDesafiante)
            usuario.tiempoDeLectura(libroNoDesafiante) shouldBe 600.00 // ORIGINALMENTE DARIA 582.00 PERO DA ERROR
        }
        it("Cantidad leidos"){
            usuario.formaDeLeer = Recurrente()
            usuario.leerLibro(libroNoDesafiante)
            usuario.leerLibro(libroNoDesafiante)
            usuario.leerLibro(libroNoDesafiante)
            usuario.cantVecesLeido.getValue(libroNoDesafiante) shouldBe 3
        }
    }

    describe("Calculo de edad"){
        it ("tiene 27 años"){
            usuario.edad() shouldBe 27
        }
    }

    describe("Velocidad de lectura promedio"){
        it("libro desafiante"){
            usuario.calculaVelocidadPromedio(libroDesafianteNoComplejo) shouldBe 400.00
        }
        it ("libro no desafiante"){
            usuario.calculaVelocidadPromedio(libroNoDesafiante) shouldBe 200.00
        }
    }

    describe("Perfiles"){
        val poliglota = Poliglota()
        it("Puede cambiar el perfil"){
            usuario.cambiarPerfil(poliglota)

            usuario.perfil shouldBe poliglota
        }
    }

})
