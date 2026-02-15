package com.sashka.utils

class CalcSqrt {
    companion object {
        fun sqrt(x: Double): Double {
            if (x < 0.0) return Double.NaN
            if (x == 0.0 || x == Double.POSITIVE_INFINITY) return x
            if (x.isNaN()) return Double.NaN


            var res = if (x > 1.0) x else 1.0
            var lastRes: Double

            while (true) {
                lastRes = res
                res = 0.5 * (res + x / res)

                if (res == lastRes) break
            }

            return res
        }
    }
}