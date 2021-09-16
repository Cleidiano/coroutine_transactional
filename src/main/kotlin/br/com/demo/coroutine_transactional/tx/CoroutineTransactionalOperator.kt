package br.com.demo.coroutine_transactional.tx

import br.com.demo.coroutine_transactional.domain.book.TransactionalOperator
import io.ebean.DB
import io.ebean.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import kotlin.coroutines.coroutineContext

@Component
class CoroutineTransactionalOperator : TransactionalOperator {

    override suspend fun <T> execute(block: suspend CoroutineScope.() -> T): T {
        val currentContext = coroutineContext[TransactionElement]?.also { it.referenceCount.incrementAndGet() }
        val transactionContext = currentContext ?: (coroutineContext + TransactionElement(DB.createTransaction()))
        val txScope = transactionContext[TransactionElement]!!
        val result = try {
            withContext(transactionContext, block).also { txScope.transaction.commitAndContinue() }
        } catch (e: Exception) {
            txScope.transaction.end()
            throw e
        }

        if (txScope.referenceCount.decrementAndGet() == 0) {
            txScope.transaction.commit()
        }

        return result
    }
}

suspend fun <T, R> T.inTransaction(block: T.(Transaction) -> R): R {
    val currentTransaction = coroutineContext[TransactionElement]
    return currentTransaction?.run { block(this.transaction) } ?: DB.createTransaction().use { block(it) }
}
