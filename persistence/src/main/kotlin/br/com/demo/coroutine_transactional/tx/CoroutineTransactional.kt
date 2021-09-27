package br.com.demo.coroutine_transactional.tx

import io.ebeaninternal.api.SpiEbeanServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class CoroutineTransactionalOperator(private val ebeanServer: SpiEbeanServer) : TransactionalOperator {

    override suspend fun <T> execute(block: suspend CoroutineScope.() -> T): T {
        val transactionScopeManager = ebeanServer.transactionManager.scope()

        return ebeanServer.beginTransaction().use { transaction ->
            withContext(TransactionContext(transactionScopeManager)) {
                block().also { transaction.commit() }
            }
        }
    }
}
