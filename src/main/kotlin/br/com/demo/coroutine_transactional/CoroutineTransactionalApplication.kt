package br.com.demo.coroutine_transactional

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoroutineTransactionalApplication

fun main(args: Array<String>) {
	runApplication<CoroutineTransactionalApplication>(*args)
}
