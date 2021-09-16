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
        val context =
            coroutineContext[TransactionElement]?.apply { referenceCount.incrementAndGet() }
                ?: run { coroutineContext + TransactionElement(DB.createTransaction()) }

        val result = try {
            withContext(context, block).also { context[TransactionElement]!!.transaction.commitAndContinue() }
        } catch (e: Exception) {
            context[TransactionElement]!!.transaction.end()
            throw e
        }

        if (context[TransactionElement]!!.referenceCount.decrementAndGet() == 0) {
            context[TransactionElement]!!.transaction.commit()
        }

        return result
    }
}

suspend fun <T, R> T.inTransaction(block: T.(Transaction) -> R): R {
    val currentTransaction = coroutineContext[TransactionElement]
    return currentTransaction?.run { block(this.transaction) } ?: DB.createTransaction().use { block(it) }
}
