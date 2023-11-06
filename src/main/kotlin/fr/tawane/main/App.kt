package fr.tawane.main

import fr.tawane.one.One

class App {
    val greeting: String
        get() {
            return "Hello World! + ${ One().one() }"
        }
}

fun main() {
    println(App().greeting)
}
