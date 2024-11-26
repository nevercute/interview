package com.nevercute.interview.unit

import java.util.TreeMap
import kotlin.test.Test
import kotlin.test.assertEquals
import com.nevercute.interview.atm.ATM
import mu.KLogging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.assertThrows

class ATMTests {

    companion object: KLogging()

	// Что выдается нужная сумма и уменьшаются счетчики
    @Test
    fun shouldWithdrawRequestedAmountWith5000() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 1,
                    100 to 1,
                    500 to 1,
                    1000 to 1,
                    5000 to 1
                )
            )
        )

        // when
        val response = atm.withdraw(5000)
        logger.info { "Ответ от АТМ $response" }
        logger.info { "Остаток в АТМ ${atm.nominalsMap}" }
        Assertions.assertThat(response.nominalsMap.size).isEqualTo(1)
        Assertions.assertThat(response.nominalsMap.entries.first().key).isEqualTo(5000)
        Assertions.assertThat(response.nominalsMap.entries.first().value).isEqualTo(1)
        Assertions.assertThat(atm.nominalsMap[5000]).isEqualTo(0)
        Assertions.assertThat(atm.nominalsMap.filter { it.value != 0 }.size).isEqualTo(4)
    }

    @Test
    fun shouldWithdrawRequestedAmountWith1000() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 1,
                    100 to 1,
                    500 to 1,
                    1000 to 5,
                    5000 to 0
                )
            )
        )

        // when
        val response = atm.withdraw(5000)
        logger.info { "Ответ от АТМ $response" }
        logger.info { "Остаток в АТМ ${atm.nominalsMap}" }
        Assertions.assertThat(response.nominalsMap.size).isEqualTo(1)
        Assertions.assertThat(response.nominalsMap.entries.first().key).isEqualTo(1000)
        Assertions.assertThat(response.nominalsMap.entries.first().value).isEqualTo(5)
        Assertions.assertThat(atm.nominalsMap[5000]).isEqualTo(0)
        Assertions.assertThat(atm.nominalsMap.filter { it.value != 0 }.size).isEqualTo(3)
    }

    @Test
    fun shouldWithdrawRequestedAmountFor1650() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 2,
                    100 to 2,
                    500 to 2,
                    1000 to 2,
                    5000 to 2
                )
            )
        )

        // when
        val response = atm.withdraw(1650)
        logger.info { "Ответ от АТМ $response" }
        logger.info { "Остаток в АТМ ${atm.nominalsMap}" }
        Assertions.assertThat(response.nominalsMap.size).isEqualTo(4)
        Assertions.assertThat(atm.nominalsMap[50]).isEqualTo(1)
        Assertions.assertThat(atm.nominalsMap[100]).isEqualTo(1)
        Assertions.assertThat(atm.nominalsMap[500]).isEqualTo(1)
        Assertions.assertThat(atm.nominalsMap[1000]).isEqualTo(1)
        Assertions.assertThat(atm.nominalsMap[5000]).isEqualTo(2)
    }

    @Test
    fun shouldReturnExceptionOnUnsufficientNominals() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 0,
                    100 to 3,
                    500 to 0,
                    1000 to 0,
                    5000 to 0
                )
            )
        )

        // when
        val exception = assertThrows<RuntimeException> { atm.withdraw(250) }

        // then
        assertEquals(exception.message, "Недостаточно необходимых купюр для выдачи")
    }

    @Test
    fun shouldReturnExceptionOnUnsufficientAvailableAmount() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 0,
                    100 to 3,
                    500 to 0,
                    1000 to 0,
                    5000 to 0
                )
            )
        )

        // when
        val exception = assertThrows<RuntimeException> { atm.withdraw(500) }

        // then
        assertEquals(exception.message, "Для снятия доступно 300")
    }

    @Test
    fun shouldReturnExceptionOnEmptyNominals() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 0,
                    100 to 0,
                    500 to 0,
                    1000 to 0,
                    5000 to 0
                )
            )
        )

        // when
        val exception = assertThrows<RuntimeException> { atm.withdraw(250) }

        // then
        assertEquals(exception.message, "Закончились купюры")
    }

    @Test
    fun shouldReturnExceptionOnNegativeAmount() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 0,
                    100 to 3,
                    500 to 0,
                    1000 to 0,
                    5000 to 0
                )
            )
        )

        // when
        val exception = assertThrows<RuntimeException> { atm.withdraw(-250) }

        // then
        assertEquals(exception.message, "Сумма снятия должна быть больше 0")
    }

    @Test
    fun shouldReturnExceptionOnHugeAmount() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 0,
                    100 to 3,
                    500 to 0,
                    1000 to 0,
                    5000 to 0
                )
            )
        )

        // when
        val exception = assertThrows<RuntimeException> { atm.withdraw(250000) }

        // then
        assertEquals(exception.message, "Сумма снятия должна быть не больше 100000")
    }

    @Test
    fun shouldReturnExceptionOnHugeNominalsCount() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 1000,
                    100 to 3,
                    500 to 0,
                    1000 to 0,
                    5000 to 0
                )
            )
        )

        // when
        val exception = assertThrows<RuntimeException> { atm.withdraw(16000) }

        // then
        assertEquals(exception.message, "Превышено допустимое количество купюр для выдачи")
    }

    @Test
    fun shouldReturnExceptionWhenAmountNotAMultipleOf() {
        // given
        val atm = ATM(
            TreeMap<Int, Int>(
                mapOf(
                    50 to 0,
                    100 to 3,
                    500 to 0,
                    1000 to 0,
                    5000 to 0
                )
            )
        )

        // when
        val exception = assertThrows<RuntimeException> { atm.withdraw(42) }

        // then
        assertEquals(exception.message, "Сумма должна быть кратна 50")
    }

    @Test
    fun shouldReturnExceptionOnUnsupportedNominal() {
        // given
        // when
        val exception = assertThrows<RuntimeException> {
            ATM(TreeMap<Int, Int>(mapOf(5001 to 0)))
        }

        // then
        assertEquals(exception.message, "Недопустимое значение номинала (5001)")
    }

    @Test
    fun shouldReturnExceptionOnUnsupportedNominalCount() {
        // given
        // when
        val exception = assertThrows<RuntimeException> {
            ATM(TreeMap<Int, Int>(mapOf(5000 to -2)))
        }

        // then
        assertEquals(exception.message, "Количество купюр не может быть меньше 0")
    }
}
