package io.github.svbgabriel.transaction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CajuApplication

fun main(args: Array<String>) {
    runApplication<CajuApplication>(*args)
}
