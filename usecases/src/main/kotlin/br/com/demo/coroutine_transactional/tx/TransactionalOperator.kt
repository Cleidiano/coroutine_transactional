package br.com.demo.coroutine_transactional.tx

import kotlinx.coroutines.CoroutineScope

interface TransactionalOperator {
    suspend fun <T> execute(block: suspend CoroutineScope.() -> T): T
}
