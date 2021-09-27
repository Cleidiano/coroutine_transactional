package br.com.demo.coroutine_transactional.tx

import io.ebeaninternal.api.SpiTransaction
import io.ebeaninternal.server.transaction.TransactionScopeManager
import kotlinx.coroutines.ThreadContextElement
import kotlin.coroutines.CoroutineContext

class TransactionContext(
    private val transactionManager: TransactionScopeManager,
    private val spiTransaction: SpiTransaction? = transactionManager.active
) : ThreadContextElement<SpiTransaction?> {

    override val key = Key

    override fun restoreThreadContext(context: CoroutineContext, oldState: SpiTransaction?) {
        if (oldState != null) {
            transactionManager.clearExternal()
            transactionManager.replace(oldState)
        }
    }

    override fun updateThreadContext(context: CoroutineContext): SpiTransaction? {
        val oldState = transactionManager.inScope
        transactionManager.clearExternal()
        transactionManager.replace(spiTransaction)
        return oldState
    }

    companion object Key : CoroutineContext.Key<TransactionContext>
}
