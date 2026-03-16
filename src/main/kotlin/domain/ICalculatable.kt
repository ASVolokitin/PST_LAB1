package org.example.domain

import java.math.BigDecimal

interface ICalculatable {
    operator fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal
    operator fun invoke(x: BigDecimal): BigDecimal
}