package br.com.demo.coroutine_transactional.tx

import io.ebean.DB
import io.ebean.Transaction
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

data class TransactionElement(
    val transaction: Transaction,
    val referenceCount: AtomicInteger = AtomicInteger(1)
) : AbstractCoroutineContextElement(TransactionElement) {
    companion object Key : CoroutineContext.Key<TransactionElement>
}

fun CoroutineContext.getOrCreateTransaction(): Transaction =
    this[TransactionElement]?.transaction ?: DB.createTransaction()
