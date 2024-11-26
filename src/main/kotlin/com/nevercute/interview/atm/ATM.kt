package com.nevercute.interview.atm

import java.util.Comparator
import java.util.TreeMap

/**
 * Банкомат.
 * Инициализируется набором купюр и умеет выдавать
 * купюры для заданной суммы, либо отвечать отказом.
 * При выдаче купюры списываются с баланса банкомата.
 * Допустимые номиналы: 50₽, 100₽, 500₽, 1000₽, 5000₽.
 */

class ATM(val nominalsMap: MutableMap<Int, Int>) {

    init {
        checkNominalsMap()
    }

    fun withdraw(amount: Int): WithdrawResponse {
        val availableNominals = nominalsMap.filter { it.value != 0 }.toSortedMap(Comparator.reverseOrder())
        val maxAvailableAmount = availableNominals.entries.sumOf { it.key * it.value }
        val nominalsMapToWithdraw: MutableMap<Int, Int> = mutableMapOf()

        performPreCheck(amount, maxAvailableAmount)
        computeWithdrawNominals(amount, availableNominals, nominalsMapToWithdraw)
        actuateAvailableATMNominals(nominalsMapToWithdraw)
        checkNominalsMap() // Защита от дурака
        return WithdrawResponse(nominalsMapToWithdraw)
    }

    private fun checkNominalsMap() {
        nominalsMap.forEach { (nominal, count) ->
            if (!availableNominalsList.contains(nominal)) {
                throw RuntimeException("Недопустимое значение номинала ($nominal)")
            }
            if (count < 0) {
                throw RuntimeException("Количество купюр не может быть меньше 0")
            }
        }
    }


    private fun performPreCheck(
        amount: Int,
        maxAvailableAmount: Int,
    ) {
        if (maxAvailableAmount == 0) {
            throw RuntimeException("Закончились купюры")
        }

        if (amount <= 0) {
            throw RuntimeException("Сумма снятия должна быть больше 0")
        }

        if (amount > MAX_WITHDRAW) {
            throw RuntimeException("Сумма снятия должна быть не больше $MAX_WITHDRAW")
        }

        if (maxAvailableAmount < amount) {
            throw RuntimeException("Для снятия доступно $maxAvailableAmount")
        }

        if (amount % MIN_WITHDRAW != 0 ) {
            throw RuntimeException("Сумма должна быть кратна $MIN_WITHDRAW")
        }
    }

    private fun computeWithdrawNominals(
        amount: Int,
        availableNominals: Map<Int, Int>,
        nominalsMapToWithdraw: MutableMap<Int, Int>
    ) {
        var remainAmountToWithdraw = amount
        availableNominals.forEach { (nom, count) ->
            val nomCount: Int = remainAmountToWithdraw / nom
            if (nomCount == 0 || remainAmountToWithdraw == 0 || count == 0) {
                return@forEach
            }

            if (count >= nomCount) {
                nominalsMapToWithdraw[nom] = nomCount
                remainAmountToWithdraw -= nom * nomCount
            } else {
                nominalsMapToWithdraw[nom] = count
                remainAmountToWithdraw -= nom * count
            }
        }
        if (remainAmountToWithdraw != 0) {
            throw RuntimeException("Недостаточно необходимых купюр для выдачи")
        }
        if (nominalsMapToWithdraw.entries.sumOf { it.value } > MAX_WITHDRAW_STACK_COUNT) {
            throw RuntimeException("Превышено допустимое количество купюр для выдачи")
        }
    }

    private fun actuateAvailableATMNominals(nominalsMapToWithdraw: MutableMap<Int, Int>) {
        nominalsMapToWithdraw.forEach { entry ->
            nominalsMap[entry.key]?.let {
                nominalsMap[entry.key] = it - entry.value
            }
        }
    }

    companion object {
        const val MAX_WITHDRAW = 100000
        const val MIN_WITHDRAW = 50
        const val MAX_WITHDRAW_STACK_COUNT = 200
        val availableNominalsList: List<Int> = listOf(50, 100, 500, 1000, 5000)
    }
}

data class WithdrawResponse(
    val nominalsMap: MutableMap<Int, Int> = TreeMap()
)
