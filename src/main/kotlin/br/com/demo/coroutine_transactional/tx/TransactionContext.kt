package br.com.demo.coroutine_transactional.tx

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ThreadContextElement
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

private class TransactionContext(
    private val state: TransactionState = TransactionState()
) : ThreadContextElement<TransactionState>, AbstractCoroutineContextElement(Key) {
    companion object Key : CoroutineContext.Key<TransactionContext>

    override fun updateThreadContext(context: CoroutineContext): TransactionState {
        return state.applyOnCurrentThread()
    }

    override fun restoreThreadContext(context: CoroutineContext, oldState: TransactionState) {
        oldState.applyOnCurrentThread()
    }
}

suspend fun <T> withTransactionContext(block: suspend CoroutineScope.() -> T): T {
    return withContext(TransactionContext(), block) // To capture the existing ongoing transaction
//    return withContext(currentCoroutineContext(), block) // Uncomment and run the test to see wrong state appear.
}
