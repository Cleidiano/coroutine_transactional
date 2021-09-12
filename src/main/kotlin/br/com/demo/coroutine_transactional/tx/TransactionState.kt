package br.com.demo.coroutine_transactional.tx

import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

typealias TSM = TransactionSynchronizationManager

internal data class TransactionState(
    val isTransactionActive: Boolean = TSM.isActualTransactionActive(),
    val currentTransactionReadOnly: Boolean = TSM.isCurrentTransactionReadOnly(),
    val currentTransactionIsolationLevel: Int? = TSM.getCurrentTransactionIsolationLevel(),
    val currentTransactionName: String? = TSM.getCurrentTransactionName(),
    val resourceMap: Map<Any, Any> = TSM.getResourceMap().toMap(), // need to copy the Map!
    val synchronizationActive: Boolean = TSM.isSynchronizationActive(),
    val synchronizations: List<TransactionSynchronization>? = if (synchronizationActive) TSM.getSynchronizations() else null
) {
    init {
        if (synchronizationActive) require(synchronizations != null)
        else require(synchronizations == null)
    }

    fun isEmpty(): Boolean = !isTransactionActive
            && !currentTransactionReadOnly
            && currentTransactionIsolationLevel == null
            && currentTransactionName == null
            && resourceMap.isEmpty()
            && !synchronizationActive
            && synchronizations == null

    fun applyOnCurrentThread(): TransactionState {
        val oldState = TransactionState()
        TSM.clear()
        // clear() clears everything except the resources
        oldState.resourceMap.keys.forEach { TSM.unbindResource(it) }

        TSM.setActualTransactionActive(isTransactionActive)
        TSM.setCurrentTransactionReadOnly(currentTransactionReadOnly)
        TSM.setCurrentTransactionIsolationLevel(currentTransactionIsolationLevel)
        TSM.setCurrentTransactionName(currentTransactionName)
        resourceMap.forEach { (k, v) ->
            TSM.bindResource(k, v)
        }

        if (synchronizationActive) {
            require(synchronizations != null)
            TSM.initSynchronization()
            synchronizations.forEach { TSM.registerSynchronization(it) }
        } else {
            require(synchronizations == null)
        }

        require(this == TransactionState())

        return oldState
    }
}
